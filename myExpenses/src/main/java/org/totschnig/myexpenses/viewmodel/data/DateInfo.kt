package org.totschnig.myexpenses.viewmodel.data

import android.content.ContentResolver
import android.database.Cursor
import org.totschnig.myexpenses.provider.DatabaseConstants
import org.totschnig.myexpenses.provider.DatabaseConstants.KEY_MAX_VALUE
import org.totschnig.myexpenses.provider.DatabaseConstants.KEY_THIS_DAY
import org.totschnig.myexpenses.provider.DatabaseConstants.KEY_THIS_MONTH
import org.totschnig.myexpenses.provider.DatabaseConstants.KEY_THIS_WEEK
import org.totschnig.myexpenses.provider.DatabaseConstants.KEY_THIS_YEAR
import org.totschnig.myexpenses.provider.DatabaseConstants.KEY_THIS_YEAR_OF_MONTH_START
import org.totschnig.myexpenses.provider.DatabaseConstants.KEY_THIS_YEAR_OF_WEEK_START
import org.totschnig.myexpenses.provider.DatabaseConstants.KEY_WEEK_START
import org.totschnig.myexpenses.provider.TransactionProvider
import org.totschnig.myexpenses.provider.getIntIfExistsOr0
import org.totschnig.myexpenses.provider.getLocalDateIfExists
import java.time.LocalDate
import javax.annotation.concurrent.Immutable

@Immutable
data class DateInfo(
    val day: Int, val week: Int, val month: Int, val year: Int,
    val yearOfWeekStart: Int, val yearOfMonthStart: Int
) {
    companion object {
        val EMPTY = DateInfo(0, 0, 0 ,0, 0, 0)

        fun load(contentResolver: ContentResolver) =
            contentResolver.dateInfoQuery!!.use {
                it.moveToFirst()
                fromCursor(it)
            }

        val ContentResolver.dateInfoQuery
            get() = query(
                TransactionProvider.DUAL_URI,
                arrayOf(
                    "${DatabaseConstants.getThisYearOfWeekStart()} AS $KEY_THIS_YEAR_OF_WEEK_START",
                    "${DatabaseConstants.getThisYearOfMonthStart()} AS $KEY_THIS_YEAR_OF_MONTH_START",
                    "${DatabaseConstants.THIS_YEAR} AS $KEY_THIS_YEAR",
                    "${DatabaseConstants.getThisMonth()} AS $KEY_THIS_MONTH",
                    "${DatabaseConstants.getThisWeek()} AS $KEY_THIS_WEEK",
                    "${DatabaseConstants.THIS_DAY} AS $KEY_THIS_DAY"
                ),
                null, null, null, null
            )
        fun fromCursor(cursor: Cursor) = with(cursor) {
            DateInfo(
                getIntIfExistsOr0(KEY_THIS_DAY),
                getIntIfExistsOr0(KEY_THIS_WEEK),
                getIntIfExistsOr0(KEY_THIS_MONTH),
                getIntIfExistsOr0(KEY_THIS_YEAR),
                getIntIfExistsOr0(KEY_THIS_YEAR_OF_WEEK_START),
                getIntIfExistsOr0(KEY_THIS_YEAR_OF_MONTH_START)
            )
        }
    }
}

@Immutable
data class DateInfoExtra(
    val maxValue: Int, val weekStart: LocalDate?,
) {
    companion object {
        fun fromCursor(cursor: Cursor) = with(cursor) {
            DateInfoExtra(
                getIntIfExistsOr0(KEY_MAX_VALUE),
                getLocalDateIfExists(KEY_WEEK_START)
            )
        }
    }
}