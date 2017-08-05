package me.bramhaag.owoandroid.record

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.hardware.display.DisplayManager
import android.hardware.display.VirtualDisplay
import android.media.MediaRecorder
import android.media.projection.MediaProjection
import android.media.projection.MediaProjectionManager
import android.os.Environment
import android.util.DisplayMetrics
import me.bramhaag.owoandroid.util.ID
import java.io.File


class ScreenRecorder(val activity: Activity) {

    private val mediaProjectionManager: MediaProjectionManager
    private var mediaProjection: MediaProjection? = null
    private var virtualDisplay: VirtualDisplay? = null

    private lateinit var mediaRecorder: MediaRecorder

    private val width: Int
    private val height: Int
    private val dpi: Int

    init {
        val displayMetrics = DisplayMetrics()
        activity.windowManager.defaultDisplay.getMetrics(displayMetrics)

        width = displayMetrics.widthPixels
        height = displayMetrics.heightPixels
        dpi = displayMetrics.densityDpi

        mediaProjectionManager = activity.getSystemService(Context.MEDIA_PROJECTION_SERVICE) as MediaProjectionManager
    }

    fun startRecording() {
        if(mediaProjection == null) {
            activity.startActivityForResult(mediaProjectionManager.createScreenCaptureIntent(), ID.RECORD_PERMISSION)
            return
        }

        setUpMediaRecorder()
        setUpVirtualDisplay()

        mediaRecorder.start()
    }

    fun stopRecording() {
        if(virtualDisplay == null) {
            return
        }

        virtualDisplay!!.release()
        virtualDisplay = null

        mediaRecorder.stop()
        mediaRecorder.reset()
        mediaRecorder.release()
    }

    fun cancelRecording() {

    }

    fun destroy() {
        if(mediaProjection != null) {
            mediaProjection!!.stop()
            mediaProjection = null
        }
    }

    fun setUpMediaRecorder() {
        mediaRecorder = MediaRecorder()
        mediaRecorder.apply {
            setVideoSource(MediaRecorder.VideoSource.SURFACE)
            setOutputFormat(MediaRecorder.OutputFormat.MPEG_4)
            setVideoEncoder(MediaRecorder.VideoEncoder.H264)
            setVideoEncodingBitRate(3000000)
            setVideoFrameRate(30)
            setVideoSize(width, height)
            setOutputFile(File(Environment.getExternalStorageDirectory(), "owo.mp4").path)
            setMaxFileSize(10E9.toLong())
        }

        mediaRecorder.prepare()
    }

    fun setUpMediaProjection(resultCode: Int, data: Intent) {
        mediaProjection = mediaProjectionManager.getMediaProjection(resultCode, data)
    }

    private fun setUpVirtualDisplay() {
        val displayMetrics = DisplayMetrics()
        activity.windowManager.defaultDisplay.getMetrics(displayMetrics)

        virtualDisplay = mediaProjection!!.createVirtualDisplay("ScreenCapture",
                displayMetrics.widthPixels,
                displayMetrics.heightPixels,
                displayMetrics.densityDpi,
                DisplayManager.VIRTUAL_DISPLAY_FLAG_AUTO_MIRROR,
                mediaRecorder.surface,
                null,
                null)
    }
}