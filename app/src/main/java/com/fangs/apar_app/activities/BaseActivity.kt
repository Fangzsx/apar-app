package com.fangs.apar_app.activities

import android.app.Activity
import android.content.Context
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.InputMethodManager
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import com.fangs.apar_app.R
import com.google.android.material.snackbar.Snackbar

open class BaseActivity : AppCompatActivity() {

    fun showErrorSnackBar(view : View, message : String, hasError : Boolean){


        val snackBar = Snackbar.make(view, message, Snackbar.LENGTH_LONG)
        val snackBarView =snackBar.view

        //snackBar margin
        val params = snackBarView.layoutParams as ViewGroup.MarginLayoutParams
        params.setMargins(0,0,0,0)
        snackBarView.layoutParams = params


        if(hasError){
            snackBarView.setBackgroundColor(ContextCompat.getColor(this@BaseActivity, R.color.snackBarError))
        }

        snackBar.show()

    }

    //hide keyboard when instance of an event
    fun Context.hideKeyboard(view: View) {
        val inputMethodManager = getSystemService(Activity.INPUT_METHOD_SERVICE) as InputMethodManager
        inputMethodManager.hideSoftInputFromWindow(view.windowToken, 0)
    }

}