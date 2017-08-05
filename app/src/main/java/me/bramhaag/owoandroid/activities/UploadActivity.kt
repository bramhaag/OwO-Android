package me.bramhaag.owoandroid.activities

import android.app.Dialog
import android.os.Bundle
import android.support.v4.app.DialogFragment
import android.support.v7.widget.LinearLayoutManager
import kotlinx.android.synthetic.main.dialog_upload.*
import me.bramhaag.owoandroid.adapters.UploadItemAdapter
import me.bramhaag.owoandroid.data.UploadItem
import android.support.v7.app.AlertDialog
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import kotlinx.android.synthetic.main.dialog_upload.view.*
import me.bramhaag.owouploader.R

class UploadActivity : DialogFragment() {

//    recyclerView = v.recycler_view_upload.apply {
//        layoutManager = LinearLayoutManager(activity)
//        adapter = UploadItemAdapter(listOf(
//                UploadItem("aaaaaaaaaaaaaaaa", 10, 100),
//                UploadItem("bbbbbbb", 60, 200),
//                UploadItem("cccccccccccccccccccc", 50, 100),
//                UploadItem("dddd", 50, 200),
//                UploadItem("eeeeee", 80, 1000),
//                UploadItem("aaffaaaaaa", 1, 10)
//        ), activity)
//    }

//    lateinit var recyclerView: RecyclerView
//
//    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
//        val v = inflater.inflate(R.layout.dialog_upload, container, false)
//        println("rview == null? ${recycler_view_upload == null}")
//        println("v.rview == null? ${v.recycler_view_upload == null}")
//        println("activity == null? ${activity == null}")
//
//        recyclerView = v.recycler_view_upload.apply {
//            layoutManager = LinearLayoutManager(activity)
//            adapter = UploadItemAdapter(listOf(
//                    UploadItem("aaaaaaaaaaaaaaaa", 10, 100),
//                    UploadItem("bbbbbbb", 60, 200),
//                    UploadItem("cccccccccccccccccccc", 50, 100),
//                    UploadItem("dddd", 50, 200),
//                    UploadItem("eeeeee", 80, 1000),
//                    UploadItem("aaffaaaaaa", 1, 10)
//            ), activity)
//        }
//
//        return v
//    }

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        val rootView = activity.layoutInflater.inflate(R.layout.dialog_upload, null)
        rootView.recycler_view_upload.apply {
            layoutManager = LinearLayoutManager(activity)
            adapter = UploadItemAdapter(listOf(
                    UploadItem("screenshot_08_05_2017_1004688381", 10.0, 100.0),
                    UploadItem("not_100mb_of_porn", 50_000_000.0, 100_000_000.0),
                    UploadItem("dankest_memes", 50.0, 100.0),
                    UploadItem("i_dont_know_what_to_type_anymore", 50.0, 200.0),
                    UploadItem("forced_null_safety_sucks", 80.0, 1000.0),
                    UploadItem("kek", 1.0, 10.0)
            ), activity)
        }

        val builder = AlertDialog.Builder(activity)
        builder.setView(rootView)
                .setPositiveButton("understandable", { dialog, id -> })
                .setNegativeButton("no", { dialog, id -> this@UploadActivity.dialog.cancel() })
        return builder.create()
    }
}
