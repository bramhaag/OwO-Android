package me.bramhaag.owoandroid.activities

import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.content.Intent
import android.preference.PreferenceManager
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.RecyclerView
import android.widget.Button
import me.bramhaag.owoandroid.R
import me.bramhaag.owoandroid.api.OwO
import me.bramhaag.owoandroid.data.files.FilesDbHelper
import me.bramhaag.owoandroid.data.urls.UrlDbHelper
import me.bramhaag.owoandroid.listeners.ShortenButtonListener
import me.bramhaag.owoandroid.listeners.UploadButtonListener
import me.bramhaag.owoandroid.managers.RecyclerViewManager
import net.cachapa.expandablelayout.ExpandableLayout
import java.util.function.BiConsumer
import kotlin.collections.HashMap


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

        (findViewById(R.id.upload_button) as Button).setOnClickListener(UploadButtonListener(this))
        (findViewById(R.id.shorten_button) as Button).setOnClickListener(ShortenButtonListener(this))

        findViewById(R.id.button_expand_upload).setOnClickListener({
            (findViewById(R.id.expandable_layout_upload) as ExpandableLayout).let { if (it.isExpanded) it.collapse() else it.expand() }
        })

        findViewById(R.id.button_expand_shorten).setOnClickListener({
            (findViewById(R.id.expandable_layout_shorten) as ExpandableLayout).let { if (it.isExpanded) it.collapse() else it.expand() }
        })

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
