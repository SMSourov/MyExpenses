package org.totschnig.myexpenses.activity

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.view.MenuItem
import androidx.activity.result.contract.ActivityResultContract
import eltos.simpledialogfragment.input.SimpleInputDialog
import org.totschnig.myexpenses.R
import org.totschnig.myexpenses.dialog.AmountFilterDialog
import org.totschnig.myexpenses.dialog.DateFilterDialog
import org.totschnig.myexpenses.dialog.select.SelectCrStatusDialogFragment
import org.totschnig.myexpenses.dialog.select.SelectMethodDialogFragment
import org.totschnig.myexpenses.dialog.select.SelectMultipleAccountDialogFragment
import org.totschnig.myexpenses.dialog.select.SelectTransferAccountDialogFragment
import org.totschnig.myexpenses.fragment.TagList.Companion.KEY_TAG_LIST
import org.totschnig.myexpenses.model.AccountType
import org.totschnig.myexpenses.provider.DatabaseConstants
import org.totschnig.myexpenses.provider.DatabaseConstants.KEY_ACCOUNTID
import org.totschnig.myexpenses.provider.filter.AccountCriterion
import org.totschnig.myexpenses.provider.filter.AmountCriterion
import org.totschnig.myexpenses.provider.filter.CategoryCriterion
import org.totschnig.myexpenses.provider.filter.CommentCriterion
import org.totschnig.myexpenses.provider.filter.CrStatusCriterion
import org.totschnig.myexpenses.provider.filter.Criterion
import org.totschnig.myexpenses.provider.filter.DateCriterion
import org.totschnig.myexpenses.provider.filter.IdCriterion
import org.totschnig.myexpenses.provider.filter.KEY_SELECTION
import org.totschnig.myexpenses.provider.filter.MethodCriterion
import org.totschnig.myexpenses.provider.filter.NULL_ITEM_ID
import org.totschnig.myexpenses.provider.filter.PayeeCriterion
import org.totschnig.myexpenses.provider.filter.TagCriterion
import org.totschnig.myexpenses.provider.filter.TransferCriterion
import org.totschnig.myexpenses.util.checkMenuIcon
import org.totschnig.myexpenses.util.setEnabledAndVisible
import org.totschnig.myexpenses.viewmodel.SumInfoLoaded
import org.totschnig.myexpenses.viewmodel.data.Tag

class FilterHandler(private val activity: BaseMyExpenses) {
    fun configureSearchMenu(searchMenu: MenuItem) {
        with(activity) {
            searchMenu.setEnabledAndVisible((sumInfo as? SumInfoLoaded)?.hasItems == true)
            (sumInfo as? SumInfoLoaded)?.let { sumInfo ->
                val whereFilter = currentFilter.whereFilter
                searchMenu.isChecked = !whereFilter.isEmpty
                checkMenuIcon(searchMenu)
                val filterMenu = searchMenu.subMenu!!
                for (i in 0 until filterMenu.size()) {
                    val filterItem = filterMenu.getItem(i)
                    var enabled = true
                    when (filterItem.itemId) {
                        R.id.FILTER_CATEGORY_COMMAND -> {
                            enabled = sumInfo.mappedCategories
                        }
                        R.id.FILTER_STATUS_COMMAND -> {
                            enabled =
                                currentAccount!!.isAggregate || currentAccount!!.type != AccountType.CASH
                        }
                        R.id.FILTER_PAYEE_COMMAND -> {
                            enabled = sumInfo.mappedPayees
                        }
                        R.id.FILTER_METHOD_COMMAND -> {
                            enabled = sumInfo.mappedMethods
                        }
                        R.id.FILTER_TRANSFER_COMMAND -> {
                            enabled = sumInfo.hasTransfers
                        }
                        R.id.FILTER_TAG_COMMAND -> {
                            enabled = sumInfo.hasTags
                        }
                        R.id.FILTER_ACCOUNT_COMMAND -> {
                            enabled = currentAccount!!.isAggregate
                        }
                    }
                    val c: Criterion<*>? = whereFilter[filterItem.itemId]
                    filterItem.setEnabledAndVisible(enabled || c != null)
                    if (c != null) {
                        filterItem.isChecked = true
                        filterItem.title = c.prettyPrint(this)
                    }
                }
            }
        }
    }

    fun handleFilter(itemId: Int, edit: Criterion<*>? = null): Boolean {
        with(activity) {
            if (accountCount == 0) return false
            if (edit == null && removeFilter(itemId)) return true
            val accountId = currentAccount?.id ?: return false
            when (itemId) {
                R.id.FILTER_CATEGORY_COMMAND -> getCategory.launch(
                    accountId to edit as? CategoryCriterion
                    )
                R.id.FILTER_PAYEE_COMMAND -> getPayee.launch(
                    accountId to edit as? PayeeCriterion
                )
                R.id.FILTER_TAG_COMMAND -> getTags.launch(
                    accountId to edit as? TagCriterion
                )
                R.id.FILTER_AMOUNT_COMMAND -> AmountFilterDialog.newInstance(
                    currentAccount!!.currencyUnit, edit as? AmountCriterion
                ).show(supportFragmentManager, "AMOUNT_FILTER")
                R.id.FILTER_DATE_COMMAND -> DateFilterDialog.newInstance(
                    edit as? DateCriterion
                ).show(supportFragmentManager, "DATE_FILTER")
                R.id.FILTER_COMMENT_COMMAND -> SimpleInputDialog.build()
                    .title(R.string.search_comment)
                    .pos(R.string.menu_search)
                    .text((edit as? CommentCriterion)?.searchString)
                    .neut()
                    .show(this, FILTER_COMMENT_DIALOG)
                R.id.FILTER_STATUS_COMMAND -> SelectCrStatusDialogFragment.newInstance(
                    edit as? CrStatusCriterion
                ).show(supportFragmentManager, "STATUS_FILTER")
                R.id.FILTER_METHOD_COMMAND -> SelectMethodDialogFragment.newInstance(
                    accountId, edit as? MethodCriterion
                ).show(supportFragmentManager, "METHOD_FILTER")
                R.id.FILTER_TRANSFER_COMMAND -> SelectTransferAccountDialogFragment.newInstance(
                    accountId, edit as? TransferCriterion
                ).show(supportFragmentManager, "TRANSFER_FILTER")
                R.id.FILTER_ACCOUNT_COMMAND -> SelectMultipleAccountDialogFragment.newInstance(
                    currentAccount!!.currency, edit as? AccountCriterion
                )
                    .show(supportFragmentManager, "ACCOUNT_FILTER")
                else -> return false
            }
            return true
        }
    }

    private val getCategory =
        activity.registerForActivityResult(PickObjectContract<CategoryCriterion>(FILTER_CATEGORY_REQUEST)) {}
    private val getPayee =
        activity.registerForActivityResult(PickObjectContract<PayeeCriterion>(FILTER_PAYEE_REQUEST)) {}
    private val getTags =
        activity.registerForActivityResult(PickObjectContract<TagCriterion>(FILTER_TAGS_REQUEST)) {}

    private inner class PickObjectContract<T: IdCriterion>(private val requestKey: String) :
        ActivityResultContract<Pair<Long, T?>, Unit>() {
        override fun createIntent(context: Context, input: Pair<Long, T?>) =
            Intent(
                context, when (requestKey) {
                    FILTER_CATEGORY_REQUEST -> ManageCategories::class.java
                    FILTER_PAYEE_REQUEST -> ManageParties::class.java
                    FILTER_TAGS_REQUEST -> ManageTags::class.java
                    else -> throw IllegalArgumentException()
                }
            ).apply {
                action = Action.SELECT_FILTER.name
                putExtra(KEY_ACCOUNTID, input.first)
                putExtra(KEY_SELECTION, input.second?.values?.toLongArray())
            }

        override fun parseResult(resultCode: Int, intent: Intent?) {
            val accountId = intent?.getLongExtra(KEY_ACCOUNTID, 0) ?: 0L
            if (resultCode == Activity.RESULT_OK) {
                if (requestKey == FILTER_TAGS_REQUEST) {
                    intent?.getParcelableArrayListExtra<Tag>(KEY_TAG_LIST)?.let { tagList ->
                        val ids = tagList.map { it.id }.toLongArray()
                        val labels = tagList.joinToString { it.label }
                        activity.addFilterCriterion(TagCriterion(labels, *ids), accountId)
                    }
                } else {
                    intent?.extras?.let {
                        val rowId = it.getLong(DatabaseConstants.KEY_ROWID)
                        val label = it.getString(DatabaseConstants.KEY_LABEL)
                        if (rowId != 0L && label != null) {
                            when (requestKey) {
                                FILTER_CATEGORY_REQUEST -> addCategoryFilter(accountId, label, rowId)
                                FILTER_PAYEE_REQUEST -> addPayeeFilter(accountId, label, rowId)
                            }
                        }
                    }
                }
            }
            if (resultCode == Activity.RESULT_FIRST_USER) {
                intent?.extras?.let {
                    val rowIds = it.getLongArray(DatabaseConstants.KEY_ROWID)
                    val label = it.getString(DatabaseConstants.KEY_LABEL)
                    if (rowIds != null && label != null) {
                        when (requestKey) {
                            FILTER_CATEGORY_REQUEST -> addCategoryFilter(accountId, label, *rowIds)
                            FILTER_PAYEE_REQUEST -> addPayeeFilter(accountId, label, *rowIds)
                        }
                    }
                }
            }
        }
    }

    private fun addCategoryFilter(accountId: Long, label: String, vararg catIds: Long) {
        with(activity) {
            addFilterCriterion(
                if (catIds.size == 1 && catIds[0] == NULL_ITEM_ID) CategoryCriterion() else
                    CategoryCriterion(label, *catIds),
                accountId
            )
        }
    }

    private fun addPayeeFilter(accountId: Long, label: String, vararg catIds: Long) {
        with(activity) {
            addFilterCriterion(
                if (catIds.size == 1 && catIds[0] == NULL_ITEM_ID) PayeeCriterion() else
                    PayeeCriterion(label, *catIds),
                accountId
            )
        }
    }

    companion object {
        const val FILTER_CATEGORY_REQUEST = "filterCategory"
        const val FILTER_PAYEE_REQUEST = "filterPayee"
        const val FILTER_TAGS_REQUEST = "filterTags"
        const val FILTER_COMMENT_DIALOG = "dialogFilterComment"
    }
}