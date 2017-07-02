package me.bramhaag.owoandroid.activities

import android.Manifest
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Bundle
import android.preference.PreferenceManager
import android.support.v4.app.ActivityCompat
import android.support.v4.content.ContextCompat
import android.support.v7.app.AppCompatActivity
import android.support.v7.app.AppCompatDelegate
import android.support.v7.app.AppCompatDelegate.MODE_NIGHT_NO
import android.support.v7.app.AppCompatDelegate.MODE_NIGHT_YES
import android.text.InputType
import android.view.Menu
import android.view.MenuItem
import com.afollestad.materialdialogs.MaterialDialog
import kotlinx.android.synthetic.main.activity_main.*
import me.bramhaag.owoandroid.R
import me.bramhaag.owoandroid.api.OwO
import me.bramhaag.owoandroid.data.files.FilesDbHelper
import me.bramhaag.owoandroid.data.urls.UrlDbHelper
import me.bramhaag.owoandroid.listeners.ExpandableLayoutListener
import me.bramhaag.owoandroid.listeners.ShortenButtonListener
import me.bramhaag.owoandroid.listeners.UploadButtonListener
import me.bramhaag.owoandroid.managers.RecyclerViewManager
import me.bramhaag.owoandroid.services.QuickUploadService
import me.bramhaag.owoandroid.util.Consumer

class MainActivity : AppCompatActivity() {

    companion object {
        lateinit var owo: OwO
        lateinit var mRecycleViewManager: RecyclerViewManager
    }

    val resultConsumerMap = HashMap<Int, Consumer<Intent?>>()

    lateinit var filesDbHelper: FilesDbHelper
    lateinit var urlDbHelper: UrlDbHelper
    override fun onCreate(savedInstanceState: Bundle?) {

        AppCompatDelegate.setDefaultNightMode(
                if (PreferenceManager.getDefaultSharedPreferences(this).getBoolean("pref_dark_theme", false)) MODE_NIGHT_YES
                else MODE_NIGHT_NO
        )
        super.onCreate(savedInstanceState)

        filesDbHelper = FilesDbHelper(this)
        urlDbHelper = UrlDbHelper(this)

        val firstLaunch = PreferenceManager.getDefaultSharedPreferences(this).getBoolean("first_launch", true)
        if (firstLaunch) handleFirstLaunch()
        else owo = OwO(PreferenceManager.getDefaultSharedPreferences(this).getString("pref_key", ""))

        if (!firstLaunch && handleShare(intent.getParcelableExtra<Uri>(Intent.EXTRA_STREAM))) return
        else setContentView(R.layout.activity_main)

        upload_button.setOnClickListener(UploadButtonListener(this))
        shorten_button.setOnClickListener(ShortenButtonListener(this))

        val expandableLayoutListener = ExpandableLayoutListener(expandable_layout_upload, expandable_layout_shorten)
        button_expand_upload.setOnClickListener(expandableLayoutListener)
        button_expand_shorten.setOnClickListener(expandableLayoutListener)


        mRecycleViewManager = RecyclerViewManager(upload_recycler_view, shorten_recycler_view, applicationContext)
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.main_menu, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem?): Boolean {
        when (item?.itemId) {
            R.id.action_settings -> {
                startActivity(Intent(this, SettingsActivity::class.java))
                return true
            }
            else -> return super.onOptionsItemSelected(item)
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        resultConsumerMap[requestCode]?.accept(data)
    }

    private fun handleFirstLaunch() {
        MaterialDialog.Builder(this)
                .title(getString(R.string.dialog_welcome_title))
                .content(getString(R.string.dialog_welcome_content))
                .positiveText(getString(R.string.dialog_button_continue))
                .inputType(InputType.TYPE_CLASS_TEXT)
                .input(getString(R.string.dialog_welcome_input), null, false, { _, _ -> })
                .checkBoxPrompt(getString(R.string.dialog_welcome_checkbox), true, { _, isChecked -> PreferenceManager.getDefaultSharedPreferences(this@MainActivity).edit().putBoolean("acra.disable", isChecked).apply() })
                .onPositive { dialog, _ ->
                    val prefKey = dialog.inputEditText?.text.toString()
                    PreferenceManager.getDefaultSharedPreferences(this@MainActivity).edit().putString("pref_key", prefKey).apply()
                    owo = OwO(prefKey)

                    PreferenceManager.getDefaultSharedPreferences(this).edit().putBoolean("first_launch", false).apply()
                }
                .cancelable(false)
                .canceledOnTouchOutside(false)
                .autoDismiss(true)
                .show()
    }

    private fun handleShare(data: Uri?): Boolean {
        if(data == null) return false

        ActivityCompat.requestPermissions(this, arrayOf(Manifest.permission.READ_EXTERNAL_STORAGE), 1)
        if(ContextCompat.checkSelfPermission(applicationContext, android.Manifest.permission.READ_EXTERNAL_STORAGE) == PackageManager.PERMISSION_DENIED) return false

        val mServiceIntent = Intent(this, QuickUploadService::class.java)
        mServiceIntent.putExtra(Intent.EXTRA_STREAM, data)
        this.startService(mServiceIntent)
        finish()

        return true
    }
}
