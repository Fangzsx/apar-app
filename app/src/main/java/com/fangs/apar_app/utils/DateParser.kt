package com.fangs.apar_app.utils

import android.annotation.SuppressLint
import android.util.Log
import java.text.ParseException
import java.text.SimpleDateFormat

object DateParser{
    @SuppressLint("SimpleDateFormat")
    fun parse (inputFormat : String, outputFormat : String, inputDate: String) : String?{

        var output : String? = null
        val inFormat = SimpleDateFormat(inputFormat)
        val outFormat = SimpleDateFormat(outputFormat)
        try{
            val date = inFormat.parse(inputDate)
            output = outFormat.format(date!!)


        }catch (e : ParseException){
            Log.e("Date ParseException", e.message.toString())
        }
        return output

    }

}