package me.bramhaag.owoandroid.activities

import android.content.Intent
import android.os.Bundle
import android.preference.PreferenceManager
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.RecyclerView
import android.view.Menu
import android.view.MenuItem
import me.bramhaag.owoandroid.R
import me.bramhaag.owoandroid.api.OwO
import me.bramhaag.owoandroid.data.files.FilesDbHelper
import me.bramhaag.owoandroid.data.urls.UrlDbHelper
import me.bramhaag.owoandroid.listeners.ShortenButtonListener
import me.bramhaag.owoandroid.listeners.ShortenExpandableLayoutListener
import me.bramhaag.owoandroid.listeners.UploadButtonListener
import me.bramhaag.owoandroid.listeners.UploadExpandableLayoutListener
import me.bramhaag.owoandroid.managers.RecyclerViewManager
import net.cachapa.expandablelayout.ExpandableLayout
import java.util.function.BiConsumer


class MainActivity : AppCompatActivity() {

    lateinit var owo: OwO
    val resultConsumerMap = HashMap<Int, BiConsumer<Int, Intent?>>()

    lateinit var filesDbHelper: FilesDbHelper
    lateinit var urlDbHelper: UrlDbHelper

    lateinit var mRecycleViewManager: RecyclerViewManager

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        filesDbHelper = FilesDbHelper(this)
        urlDbHelper = UrlDbHelper(this)

        //TODO force to set key
        owo = OwO(PreferenceManager.getDefaultSharedPreferences(this).getString("pref_key", "Invalid Key"))

        findViewById(R.id.upload_button).setOnClickListener(UploadButtonListener(this))
        findViewById(R.id.shorten_button).setOnClickListener(ShortenButtonListener(this))

        findViewById(R.id.button_expand_upload).setOnClickListener(UploadExpandableLayoutListener(findViewById(R.id.expandable_layout_upload) as ExpandableLayout, findViewById(R.id.expandable_layout_shorten) as ExpandableLayout))
        findViewById(R.id.button_expand_shorten).setOnClickListener(ShortenExpandableLayoutListener(findViewById(R.id.expandable_layout_upload) as ExpandableLayout, findViewById(R.id.expandable_layout_shorten) as ExpandableLayout))

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
        resultConsumerMap[requestCode]?.accept(resultCode, data)
    }
}
