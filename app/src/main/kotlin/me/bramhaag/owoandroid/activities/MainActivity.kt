package me.bramhaag.owoandroid.activities

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.preference.PreferenceManager
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.RecyclerView
import android.text.InputType
import android.view.Menu
import android.view.MenuItem
import com.afollestad.materialdialogs.MaterialDialog
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
import net.cachapa.expandablelayout.ExpandableLayout

class MainActivity : AppCompatActivity() {

    companion object {
        lateinit var owo: OwO
    }

    val resultConsumerMap = HashMap<Int, Consumer<Intent?>>()

    lateinit var filesDbHelper: FilesDbHelper
    lateinit var urlDbHelper: UrlDbHelper

    lateinit var mRecycleViewManager: RecyclerViewManager

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        filesDbHelper = FilesDbHelper(this)
        urlDbHelper = UrlDbHelper(this)

        val key = PreferenceManager.getDefaultSharedPreferences(this).getString("pref_key", null)
        if(key == null || key.isBlank()) {
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
                    }
                    .cancelable(false)
                    .canceledOnTouchOutside(false)
                    .autoDismiss(true)
                    .show()
        } else owo = OwO(key)

        val data = intent.getParcelableExtra<Uri>(Intent.EXTRA_STREAM)
        if(intent.getParcelableExtra<Uri>(Intent.EXTRA_STREAM) != null) {
            val mServiceIntent = Intent(this, QuickUploadService::class.java)
            mServiceIntent.putExtra(Intent.EXTRA_STREAM, data)
            this.startService(mServiceIntent)

            finish()
            return
        } else {
            setContentView(R.layout.activity_main)
        }

        findViewById(R.id.upload_button).setOnClickListener(UploadButtonListener(this))
        findViewById(R.id.shorten_button).setOnClickListener(ShortenButtonListener(this))

        val expandableLayoutListener = ExpandableLayoutListener(findViewById(R.id.expandable_layout_upload) as ExpandableLayout, findViewById(R.id.expandable_layout_shorten) as ExpandableLayout)
        findViewById(R.id.button_expand_upload).setOnClickListener(expandableLayoutListener)
        findViewById(R.id.button_expand_shorten).setOnClickListener(expandableLayoutListener)


        mRecycleViewManager = RecyclerViewManager(findViewById(R.id.upload_recycler_view) as RecyclerView, findViewById(R.id.shorten_recycler_view) as RecyclerView, applicationContext)
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
}
