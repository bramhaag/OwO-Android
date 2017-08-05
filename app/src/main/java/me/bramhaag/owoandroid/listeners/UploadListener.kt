package me.bramhaag.owoandroid.listeners

import android.Manifest
import android.app.Activity
import android.content.Intent
import android.content.pm.PackageManager
import android.support.v4.app.ActivityCompat
import android.view.View
import me.bramhaag.owoandroid.util.ID


class UploadListener(val activity: Activity) : View.OnClickListener {

    override fun onClick(view: View) {
        if(ActivityCompat.checkSelfPermission(activity, Manifest.permission.READ_EXTERNAL_STORAGE) == PackageManager.PERMISSION_DENIED) {
            ActivityCompat.requestPermissions(activity, arrayOf(Manifest.permission.READ_EXTERNAL_STORAGE), ID.UPLOAD_PERMISSION)
        } else {
            openGallery()
        }
    }

    fun openGallery() {
        val intent = Intent(Intent.ACTION_GET_CONTENT)
                .putExtra(Intent.EXTRA_ALLOW_MULTIPLE, true)
                .setType("*/*")

        ActivityCompat.startActivityForResult(activity, intent, ID.GALLERY_REQUEST, null)
    }

    fun upload(data: Intent) {

    }

}