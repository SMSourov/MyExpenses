package org.totschnig.myexpenses.testutils

import androidx.annotation.StringRes
import androidx.compose.ui.semantics.SemanticsProperties
import androidx.compose.ui.test.*
import androidx.compose.ui.test.junit4.createEmptyComposeRule
import org.junit.Rule
import org.totschnig.myexpenses.activity.ProtectedFragmentActivity
import org.totschnig.myexpenses.compose.TEST_TAG_LIST
import org.totschnig.myexpenses.compose.amountProperty
import org.totschnig.myexpenses.compose.headerProperty
import timber.log.Timber

abstract class BaseComposeTest<A: ProtectedFragmentActivity>: BaseUiTest<A>() {
    val listNode: SemanticsNodeInteraction
        get() = composeTestRule.onNodeWithTag(TEST_TAG_LIST)

    @get:Rule
    val composeTestRule = createEmptyComposeRule()

    fun assertTextAtPosition(text: String, position: Int, substring: Boolean = true) {
        composeTestRule.onNodeWithTag(TEST_TAG_LIST).assertTextAstPosition(text, position, substring)
    }

    fun SemanticsNodeInteraction.assertTextAstPosition(text: String, position: Int, substring: Boolean = true) {
        onChildren()[position].assertTextContains(
            text,
            substring = substring
        )
    }

    private fun hasCollectionInfo(expectedColumnCount: Int, expectedRowCount: Int) =
        SemanticsMatcher("Collection has $expectedColumnCount columns, $expectedRowCount rows") {
            with(it.config[SemanticsProperties.CollectionInfo]) {
                val result = columnCount == expectedColumnCount && rowCount == expectedRowCount
                if(!result) { Timber.d("Actual colums/rows: %d/%d", columnCount, rowCount)}
                result
            }
        }

    fun hasRowCount(expectedRowCount: Int) = hasCollectionInfo(1, expectedRowCount)
    fun hasColumnCount(expectedColumnCount: Int) = hasCollectionInfo(expectedColumnCount, 1)

    fun hasAmount(amount: Long) = SemanticsMatcher.expectValue(amountProperty, amount)

    fun hasHeaderId(headerId: Int) = SemanticsMatcher.expectValue(headerProperty, headerId)

    fun clickContextItem(
        @StringRes resId: Int,
        node: SemanticsNodeInteraction = listNode,
        position: Int = 0,
        onLongClick: Boolean = false
    ) {
        node.onChildren()[position].performTouchInput {
            if (onLongClick) longClick() else click()
        }
        if (!isOrchestrated) {
            Thread.sleep(200)
        }
        composeTestRule.onNodeWithText(getString(resId)).performClick()
    }

    fun clickContextItem(
        command: String,
        node: SemanticsNodeInteraction = listNode,
        position: Int = 0,
        onLongClick: Boolean = false
    ) {
        node.onChildren()[position].performTouchInput {
            if (onLongClick) longClick() else click()
        }
        composeTestRule.onNodeWithTag(command).performClick()
    }
}