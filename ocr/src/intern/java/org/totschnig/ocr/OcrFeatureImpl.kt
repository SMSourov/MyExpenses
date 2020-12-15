package org.totschnig.ocr

import android.content.Context
import androidx.annotation.Keep
import org.totschnig.myexpenses.activity.BaseActivity
import org.totschnig.myexpenses.preference.PrefHandler
import org.totschnig.ocr.OcrHandlerImpl.Companion.getEngine

@Keep
class OcrFeatureImpl(val prefHandler: PrefHandler): OcrFeature() {
    override fun downloadTessData(context: Context, prefHandler: PrefHandler) =
            (getEngine(context, prefHandler) as? TesseractEngine)?.downloadTessData(context, prefHandler)

    override fun isAvailable(context: Context) =
            (getEngine(context, prefHandler) as? TesseractEngine)?.tessDataExists(context, prefHandler) ?: true

    override fun offerInstall(baseActivity: BaseActivity) {
        (getEngine(baseActivity, prefHandler) as? TesseractEngine)?.offerTessDataDownload(baseActivity)
    }

    override fun configureTesseractLanguagePref(listPreference: androidx.preference.ListPreference) {
        (getEngine(ENGINE_TESSERACT) as? TesseractEngine)?.let {
            listPreference.isVisible = true
            listPreference.setEntries(it.getLanguageArray(listPreference.context))
        }
                ?: kotlin.run { super.configureTesseractLanguagePref(listPreference) }
    }

    fun tesseractModuleInstalled(): Boolean {
        return getEngine(ENGINE_TESSERACT) != null
    }
}