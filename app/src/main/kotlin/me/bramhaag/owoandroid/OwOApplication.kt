package me.bramhaag.owoandroid

import android.app.Application
import android.content.Context
import org.acra.ACRA
import org.acra.ReportingInteractionMode
import org.acra.annotation.ReportsCrashes
import org.acra.sender.HttpSender

//These hardcoded things are perfectly fine
@ReportsCrashes(
        httpMethod = HttpSender.Method.PUT,
        reportType = HttpSender.Type.JSON,
        formUri = "http://bramhagens.me:5984/acra-owo/_design/acra-storage/_update/report",
        formUriBasicAuthLogin = "reporter",
        formUriBasicAuthPassword = "password",
        mode = ReportingInteractionMode.TOAST,
        resToastText = R.string.crash_toast_text
)
class OwOApplication : Application() {

    override fun attachBaseContext(base: Context) {
        super.attachBaseContext(base)

        if(!BuildConfig.DEBUG) {
            ACRA.init(this)
        }
    }
}