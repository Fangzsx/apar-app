package com.fangs.apar_app.activities

import android.app.Activity
import android.content.Context
import android.view.View
import android.view.ViewGroup
import android.view.WindowManager
import android.view.inputmethod.InputMethodManager
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.Spinner
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.core.view.WindowInsetsCompat
import androidx.core.view.WindowInsetsControllerCompat
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

    fun populateSpinner(spinner : Spinner, defaultValue : String) {
        ArrayAdapter.createFromResource(
            this, R.array.products_category,
            R.layout.support_simple_spinner_dropdown_item
        ).also { adapter ->
            adapter.setDropDownViewResource(R.layout.support_simple_spinner_dropdown_item)
            spinner.adapter = adapter
            val defaultIndex = adapter.getPosition(defaultValue.uppercase())
            spinner.setSelection(defaultIndex)

            //set text color of selected text
            spinner.onItemSelectedListener = object :
                AdapterView.OnItemSelectedListener{
                override fun onItemSelected(
                    parent: AdapterView<*>?,
                    view: View?,
                    position: Int,
                    id: Long
                ) {

                    val tv = spinner.selectedView as TextView
                    tv.setTextColor(ContextCompat.getColor(applicationContext, R.color.black))
                }
                override fun onNothingSelected(parent: AdapterView<*>?) {

                }
            }
        }
    }

}