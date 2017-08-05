package me.bramhaag.owoandroid.activities

import android.Manifest
import android.app.Activity
import android.app.Dialog
import android.content.Intent
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.support.v4.app.ActivityCompat
import android.support.v7.app.AppCompatDelegate
import android.view.Menu
import android.view.MenuItem
import android.view.Window
import kotlinx.android.synthetic.main.activity_main.*
import me.bramhaag.owoandroid.listeners.RecordListener
import me.bramhaag.owoandroid.listeners.UploadListener
import me.bramhaag.owoandroid.record.ScreenRecorder
import me.bramhaag.owoandroid.util.ID
import me.bramhaag.owouploader.R

class MainActivity : AppCompatActivity() {

    private val uploadListener = UploadListener(this)

    private lateinit var screenRecorder: ScreenRecorder

    override fun onCreate(savedInstanceState: Bundle?) {
        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES)
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        setSupportActionBar(toolbar_main)

        ActivityCompat.requestPermissions(this, arrayOf(Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.READ_EXTERNAL_STORAGE), 1)

        screenRecorder = ScreenRecorder(this)
        circular_button_record.setOnClickListener(RecordListener(screenRecorder))
        circular_button_upload.setOnClickListener({
            UploadActivity().show(supportFragmentManager, "")
        })

        //startActivity(Intent(this, UploadActivity::class.java))
    }

    override fun onDestroy() {
        super.onDestroy()
        screenRecorder.destroy()
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        menuInflater.inflate(R.menu.menu_main, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.actionbar_settings -> {
                startActivity(Intent(this, SettingsActivity::class.java))
                return true
            }
            R.id.actionbar_about -> {
                return true
            }
            else -> return super.onOptionsItemSelected(item)
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent) {
        if (resultCode != Activity.RESULT_OK) return

        when(requestCode) {
            ID.UPLOAD_PERMISSION -> {
                uploadListener.openGallery()
            }

            ID.RECORD_PERMISSION -> {
                screenRecorder.setUpMediaProjection(resultCode, data)
                screenRecorder.startRecording()
            }

            ID.GALLERY_REQUEST -> {
                uploadListener.upload(data)
            }
        }
    }
}
