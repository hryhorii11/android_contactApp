package com.example.myapplication.presentation.utils.views

import android.content.Context
import android.content.res.TypedArray

import android.graphics.drawable.Drawable
import android.text.Spannable
import android.text.SpannableString
import android.text.style.ImageSpan
import android.util.AttributeSet
import androidx.core.content.ContextCompat
import com.example.myapplication.R

class GoogleButtonView : androidx.appcompat.widget.AppCompatButton {

    private val imageDrawable: Drawable?

    constructor(context: Context) : super(context) {
        imageDrawable = null
    }

    constructor(context: Context, attributeSet: AttributeSet?) : super(context, attributeSet) {
        val typedArray = context.obtainStyledAttributes(
            attributeSet,
            R.styleable.GoogleButtonView
        )
        setImage(typedArray)
        imageDrawable = typedArray.getDrawable(R.styleable.GoogleButtonView_googleButtonImage)

        typedArray.recycle()
    }

    private fun setImage(typedArray: TypedArray) {
        val customImageResId = typedArray.getResourceId(
            R.styleable.GoogleButtonView_googleButtonImage,
            R.drawable.google_button_image
        )
        val customImage = ContextCompat.getDrawable(context, customImageResId)
        customImage?.setBounds(0, 0, customImage.intrinsicWidth, customImage.intrinsicHeight)

        val spannable = SpannableString("  $text")
        val imageSpan = ImageSpan(customImage!!, ImageSpan.ALIGN_CENTER)
        spannable.setSpan(imageSpan, 0, 1, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE)
        text = spannable
    }

}

