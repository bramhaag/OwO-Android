package me.bramhaag.owoandroid.components

import android.content.Context
import android.graphics.PorterDuff
import android.util.AttributeSet
import android.view.LayoutInflater
import android.widget.ImageButton
import android.widget.LinearLayout
import android.widget.TextView
import kotlinx.android.synthetic.main.circular_button.view.*
import me.bramhaag.owouploader.R

class CircularButton : LinearLayout {

    lateinit var button: ImageButton
    lateinit var text: TextView

    private var buttonImage: Int? = null
    private var buttonColor: Int? = null
    private var buttonText: String? = null


    constructor(context: Context, attrs: AttributeSet) : super(context, attrs) { processAttributes(attrs) }
    constructor(context: Context, attrs: AttributeSet, defStyleAttr: Int) : super(context, attrs, defStyleAttr) { processAttributes(attrs) }

    init {
        val inflater = context.getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater
        inflater.inflate(R.layout.circular_button, this)
    }

    override fun onFinishInflate() {
        super.onFinishInflate()

        this.button = circular_button_button
        this.text = circular_button_text

        this.button.setImageResource(buttonImage!!)
        this.button.background.setTint(buttonColor!!)
        this.text.text = buttonText
    }

    override fun setOnClickListener(listener: OnClickListener) {
        super.setOnClickListener(listener)

        button.setOnClickListener({this.callOnClick()})
        text.setOnClickListener({this.callOnClick()})
    }

    private fun processAttributes(attrs: AttributeSet) {
        val attributeArray = context.obtainStyledAttributes(attrs, R.styleable.CircularButton)

        this.buttonImage = attributeArray.getResourceId(R.styleable.CircularButton_image, 0)
        this.buttonColor = attributeArray.getColor(R.styleable.CircularButton_backgroundColor, 0)
        this.buttonText = attributeArray.getString(R.styleable.CircularButton_text)

        attributeArray.recycle()
    }
}