package me.bramhaag.owoandroid.listeners

import android.view.View
import me.bramhaag.owoandroid.components.CircularButton
import me.bramhaag.owoandroid.record.ScreenRecorder


class RecordListener(val screenRecorder: ScreenRecorder) : View.OnClickListener {

    private var recording = false

    override fun onClick(view: View) {
        val button = view as CircularButton

        if(recording) {
            screenRecorder.stopRecording()
            recording = false
            button.text.text =  "Record"

        } else {
            screenRecorder.startRecording()
            recording = true
            button.text.text =  "Stop recording"
        }
    }

}