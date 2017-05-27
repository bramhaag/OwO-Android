package me.bramhaag.owoandroid.listeners

import android.view.View
import me.bramhaag.owoandroid.activities.MainActivity
import android.app.AlertDialog
import android.text.InputType
import android.widget.EditText
import android.widget.TextView
import okhttp3.ResponseBody
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response


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
                //val m_Text = input.text.toString()
                dialog.cancel()

                AlertDialog.Builder(activity).apply {
                    setTitle("Shortened URL")
                    setView(TextView(activity).apply {
                        text = "Loading..."

                        //TODO
                        //200 = expected
                        //400 = bad url
                        //401 = bad token
                        activity.owo.service.shorten(input.text.toString()).enqueue(object : Callback<ResponseBody> {
                            //TODO checks
                            override fun onResponse(call: Call<ResponseBody>?, response: Response<ResponseBody>?) {
                                println("code: ${response?.code()}")
                                println("error: ${response?.errorBody()?.string()}")
                                //println("response: ${response?.body()?.string()}")
                                text = response?.body()?.string()
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