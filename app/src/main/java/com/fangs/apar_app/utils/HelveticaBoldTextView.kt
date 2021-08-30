package com.fangs.apar_app.utils

import android.content.Context
import android.graphics.Typeface
import android.util.AttributeSet
import androidx.appcompat.widget.AppCompatTextView

class HelveticaBoldTextView(context : Context, attrs : AttributeSet) : AppCompatTextView(context, attrs) {

    init{
        applyFont()
    }

    private fun applyFont() {
        val typeface = Typeface.createFromAsset(context.assets, "helvetica_bold.ttf")
        setTypeface(typeface)
    }
}