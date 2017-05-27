package me.bramhaag.owoandroid.listeners

import android.app.AlertDialog
import android.text.InputType
import android.view.View
import android.widget.EditText
import android.widget.TextView
import me.bramhaag.owoandroid.activities.MainActivity
import okhttp3.ResponseBody
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.net.URL
import java.util.*


class ShortenButtonListener(val activity: MainActivity): View.OnClickListener {

    override fun onClick(v: View?) {
        //TODO Strings
        AlertDialog.Builder(activity).apply {
            val input = EditText(activity).apply {
                inputType = InputType.TYPE_TEXT_VARIATION_URI
                hint = "URL to shorten"
                setSingleLine()
            }

            setTitle("Shorten URL")
            setView(input)

            setPositiveButton("OK", { dialog, _ ->
                dialog.cancel()

                AlertDialog.Builder(activity).apply {
                    setTitle("Shortened URL")
                    setView(TextView(activity).apply {
                        text = "Loading..."

                        //TODO
                        //200 = expected
                        //400 = bad url
                        //401 = bad token
                        val originalUrl = input.text.toString()
                        activity.owo.service.shorten(originalUrl).enqueue(object : Callback<ResponseBody> {
                            //TODO checks
                            override fun onResponse(call: Call<ResponseBody>?, response: Response<ResponseBody>?) {
                                println("code: ${response?.code()}")
                                println("error: ${response?.errorBody()?.string()}")
                                //println("response: ${response?.body()?.string()}")
                                val shortenedUrl = response?.body()?.string()
                                text = shortenedUrl
                                activity.mRecycleViewManager.addUrl(URL(originalUrl), URL(shortenedUrl), Date())
                            }

                            override fun onFailure(call: Call<ResponseBody>?, t: Throwable?) {
                                TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
                            }

                        })
                    })
                }.show()
            })
            setNegativeButton("Cancel", { dialog, _ -> dialog.cancel() })
        }.show()
    }
}