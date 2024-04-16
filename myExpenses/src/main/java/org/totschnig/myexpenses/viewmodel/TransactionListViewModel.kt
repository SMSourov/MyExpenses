package org.totschnig.myexpenses.viewmodel

import android.app.Application
import android.os.Parcelable
import androidx.lifecycle.SavedStateHandle
import app.cash.copper.flow.mapToOne
import app.cash.copper.flow.observeQuery
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.filterNotNull
import kotlinx.coroutines.withContext
import kotlinx.parcelize.Parcelize
import org.totschnig.myexpenses.db2.FLAG_NEUTRAL
import org.totschnig.myexpenses.db2.asCategoryType
import org.totschnig.myexpenses.db2.tagMapFlow
import org.totschnig.myexpenses.model.CurrencyUnit
import org.totschnig.myexpenses.model.Grouping
import org.totschnig.myexpenses.provider.DataBaseAccount.Companion.isHomeAggregate
import org.totschnig.myexpenses.provider.DataBaseAccount.Companion.uriBuilderForTransactionList
import org.totschnig.myexpenses.provider.DatabaseConstants.KEY_AMOUNT
import org.totschnig.myexpenses.provider.DatabaseConstants.KEY_CATID
import org.totschnig.myexpenses.provider.DatabaseConstants.VIEW_COMMITTED
import org.totschnig.myexpenses.provider.DatabaseConstants.WHERE_NOT_SPLIT
import org.totschnig.myexpenses.provider.DatabaseConstants.WHERE_NOT_VOID
import org.totschnig.myexpenses.provider.DatabaseConstants.getAmountHomeEquivalent
import org.totschnig.myexpenses.provider.DbUtils
import org.totschnig.myexpenses.provider.effectiveTypeExpression
import org.totschnig.myexpenses.provider.effectiveTypeExpressionIncludeTransfers
import org.totschnig.myexpenses.viewmodel.data.Transaction2

const val KEY_LOADING_INFO = "loadingInfo"

class TransactionListViewModel(
    application: Application,
    private val savedStateHandle: SavedStateHandle
) : ContentResolvingAndroidViewModel(application) {

    val loadingInfo
        get() = savedStateHandle.get<LoadingInfo>(KEY_LOADING_INFO)!!

    @Parcelize
    data class LoadingInfo(
        val accountId: Long,
        val currency: CurrencyUnit,
        val catId: Long = 0,
        val grouping: Grouping?,
        val groupingClause: String?,
        val groupingArgs: List<String> = emptyList(),
        val label: String?,
        val type: Boolean,
        val aggregateNeutral: Boolean = false,
        val withTransfers: Boolean = false,
        val icon: String? = null
    ) : Parcelable

    val sum: Flow<Long>
        get() = with(loadingInfo) {
            val (selection, selectionArgs) = selectionInfo
            contentResolver.observeQuery(
                transactionUri, arrayOf("sum($amountCalculation)"), selection, selectionArgs
            ).mapToOne {
                it.getLong(0)
            }
        }

    private val transactionUri
        get() = uriBuilderForTransactionList(
            loadingInfo.accountId,
            loadingInfo.currency.code,
            shortenComment = true,
            extended = false
        ).apply {
            if (loadingInfo.catId != 0L) {
                appendQueryParameter(KEY_CATID, loadingInfo.catId.toString())
            }
        }.build()


    val transactions: Flow<List<Transaction2>>
        get() = with(loadingInfo) {
            val (selection, selectionArgs) = selectionInfo
            combine(
                flow = contentResolver.tagMapFlow,
                flow2 = contentResolver.observeQuery(
                    transactionUri,
                    Transaction2.projection(
                        accountId,
                        Grouping.NONE,
                        currencyContext.homeCurrencyString,
                        prefHandler,
                        extended = false
                    ),
                    selection,
                    selectionArgs
                )
            ) { tags, query ->
                withContext(Dispatchers.IO) {
                    query.run()?.use { cursor ->
                        buildList {
                            while (cursor.moveToNext()) {
                                add(
                                    Transaction2.fromCursor(
                                        currencyContext,
                                        cursor,
                                        currency,
                                        tags
                                    )
                                )
                            }
                        }
                    }
                }
            }.filterNotNull()
        }

    private val amountCalculation: String
        get() = if (isHomeAggregate(loadingInfo.accountId))
            getAmountHomeEquivalent(VIEW_COMMITTED, currencyContext.homeCurrencyString)
        else KEY_AMOUNT

    private val selectionInfo: Pair<String, Array<String>>
        get() = with(loadingInfo) {
            val selectionParts = mutableListOf<String>()
            val selectionArgs = mutableListOf<String>()
            selectionParts += WHERE_NOT_VOID
            if (catId == 0L) {
                selectionParts += WHERE_NOT_SPLIT
            }
            groupingClause?.takeIf { it.isNotEmpty() }?.let {
                selectionParts += it
                selectionArgs.addAll(groupingArgs.toTypedArray())
            }
            val typeWithFallback = DbUtils.typeWithFallBack(prefHandler)
            val typeExpression = when {
                aggregateNeutral -> "$typeWithFallback IN (${type.asCategoryType}, $FLAG_NEUTRAL)"
                withTransfers -> effectiveTypeExpressionIncludeTransfers(typeWithFallback) + " = " + type.asCategoryType
                else -> effectiveTypeExpression(typeWithFallback) + " = " + type.asCategoryType
            }
            selectionParts += typeExpression
            selectionParts.joinToString(" AND ") to selectionArgs.toTypedArray()
        }
}

