/*   This file is part of My Expenses.
 *   My Expenses is free software: you can redistribute it and/or modify
 *   it under the terms of the GNU General Public License as published by
 *   the Free Software Foundation, either version 3 of the License, or
 *   (at your option) any later version.
 *
 *   My Expenses is distributed in the hope that it will be useful,
 *   but WITHOUT ANY WARRANTY; without even the implied warranty of
 *   MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *   GNU General Public License for more details.
 *
 *   You should have received a copy of the GNU General Public License
 *   along with My Expenses.  If not, see <http://www.gnu.org/licenses/>.
 */
package org.totschnig.myexpenses.provider.filter

import kotlinx.parcelize.IgnoredOnParcel
import kotlinx.parcelize.Parcelize
import org.totschnig.myexpenses.R
import org.totschnig.myexpenses.provider.DatabaseConstants
import org.totschnig.myexpenses.provider.filter.WhereFilter.Operation

const val ACCOUNT_COLUMN = DatabaseConstants.KEY_ACCOUNTID

@Parcelize
class AccountCriterion(
    override val label: String,
    override val values: Array<Long>) : IdCriterion() {
    constructor(label: String, vararg values: Long) : this(label, values.toTypedArray())

    @IgnoredOnParcel
    override val id = R.id.FILTER_ACCOUNT_COMMAND
    @IgnoredOnParcel
    override val column = ACCOUNT_COLUMN
    @IgnoredOnParcel
    override val title = R.string.account

    override fun getSelection(forExport: Boolean): String {
        val selection = operation.getOp(values.size)
        return "$column $selection"
    }

    companion object {
        fun fromStringExtra(extra: String) = parseStringExtra(extra)?.let {
            AccountCriterion(it.first, *it.second)
        }
    }
}