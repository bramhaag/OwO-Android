package me.bramhaag.owoandroid

import android.app.Application
import android.content.Context
import me.bramhaag.owoandroid.components.CircularButton
import me.bramhaag.owouploader.BuildConfig;
import me.bramhaag.owouploader.R
import org.acra.ACRA
import org.acra.ReportingInteractionMode
import org.acra.annotation.ReportsCrashes
import org.acra.sender.HttpSender

@ReportsCrashes(
        httpMethod = HttpSender.Method.PUT,
        reportType = HttpSender.Type.JSON,
        formUri = "http://bramhagens.me:5984/acra-owo/_design/acra-storage/_update/report",
        formUriBasicAuthLogin = "reporter",
        formUriBasicAuthPassword = "password",
        mode = ReportingInteractionMode.DIALOG,
        resDialogTheme = R.style.Theme_AppCompat_DayNight_Dialog,
        resDialogTitle = R.string.dialog_crash_title,
        resDialogText = R.string.dialog_crash_text,
        resDialogPositiveButtonText = R.string.dialog_crash_positive_text,
        resDialogCommentPrompt = R.string.dialog_crash_comment_prompt,
        resDialogOkToast = R.string.toast_crash_ok
)
class OwOApplication : Application() {

    override fun attachBaseContext(base: Context) {
        super.attachBaseContext(base)

        if (!BuildConfig.DEBUG) {
            ACRA.init(this)
        }
    }
}