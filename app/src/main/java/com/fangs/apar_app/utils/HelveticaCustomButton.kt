package com.fangs.apar_app.utils

import android.content.Context
import android.graphics.Typeface
import android.util.AttributeSet
import com.google.android.material.button.MaterialButton

class HelveticaCustomButton(context : Context, attrs : AttributeSet) : MaterialButton(context, attrs){

    init {
        applyFont()
    }

    private fun applyFont() {
        val typeface = Typeface.createFromAsset(context.assets, "helvetica_bold.ttf")
        setTypeface(typeface)
    }
}