package com.fangs.apar_app.activities

import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import com.fangs.apar_app.R
import com.google.android.material.snackbar.Snackbar

open class BaseActivity : AppCompatActivity() {

    fun showErrorSnackBar(message : String, hasError : Boolean){


        val snackBar = Snackbar.make(findViewById(R.id.content), message, Snackbar.LENGTH_LONG)
        val snackBarView =snackBar.view

        //snackBar margin
        val params = snackBarView.layoutParams as ViewGroup.MarginLayoutParams
        params.setMargins(0,0,0,0)
        snackBarView.layoutParams = params


        if(hasError){
            snackBarView.setBackgroundColor(ContextCompat.getColor(this@BaseActivity, R.color.snackBarError))
        } else{
            snackBarView.setBackgroundColor(ContextCompat.getColor(this@BaseActivity, R.color.snackBarSuccess))
        }

        snackBar.show()

    }

}