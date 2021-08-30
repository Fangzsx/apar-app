package com.fangs.apar_app.utils

import android.content.Context
import android.graphics.Typeface
import android.util.AttributeSet
import androidx.appcompat.widget.AppCompatTextView

class HelveticaNormalTextView(context : Context, attrs : AttributeSet) : AppCompatTextView(context, attrs) {

    init{
        applyFont()
    }

    private fun applyFont() {
        val typeface = Typeface.createFromAsset(context.assets, "helvetica_normal")
    }
}