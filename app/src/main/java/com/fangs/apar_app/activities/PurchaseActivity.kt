package com.fangs.apar_app.activities

import android.app.Dialog
import android.content.DialogInterface
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.ImageView
import android.widget.LinearLayout
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import com.fangs.apar_app.R
import com.fangs.apar_app.databinding.ActivityPurchaseBinding
import com.fangs.apar_app.utils.HelveticaBoldTextView

class PurchaseActivity : AppCompatActivity() {
    private lateinit var binding : ActivityPurchaseBinding

    override fun onBackPressed() {
        showAlertDialog()
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        supportActionBar?.hide()
        super.onCreate(savedInstanceState)
        binding = ActivityPurchaseBinding.inflate(layoutInflater)

        setContentView(binding.root)

        showCustomerData()
        //back navigation
        binding.sideBarPurchaseOrderBack.setOnClickListener {

            showAlertDialog()
        }

        binding.btnAmaxLoad.setOnClickListener {
            showProductDialog(it as Button)
        }

        binding.btnBisquits.setOnClickListener {
            showProductDialog(it as Button)
        }



    }

    private fun showCustomerData() {
        //display customer data
        val intent = intent
        val lastname = intent.getStringExtra("LAST_NAME")
        val firstname = intent.getStringExtra("FIRST_NAME")
        val middlename = intent.getStringExtra("MIDDLE_NAME")
        val fullname = "$lastname, $firstname $middlename"

        val houseST = intent.getStringExtra("HOUSE_ST")
        val phaseZone = intent.getStringExtra("PHASE_ZONE")
        val city = intent.getStringExtra("CITY")
        val fullAddress = "$houseST, $phaseZone $city"

        val birthday = intent.getStringExtra("BIRTHDAY")

        binding.tvCustomerName.text = fullname
        binding.tvCustomerAddress.text = fullAddress
        binding.tvCustomerBirthday.text = birthday
    }

    private fun showAlertDialog() {
        val alertDialog = AlertDialog.Builder(this)
            .setMessage("Return to Dashboard?")
            .setTitle("Leave Purchase Order")
            .setPositiveButton("YES", DialogInterface.OnClickListener { _, _
                ->
                Intent(this, MainActivity::class.java).also {
                    startActivity(it)
                }
                finish()
            })
            .setNegativeButton("NO", DialogInterface.OnClickListener { dialog, _
                ->
                dialog.dismiss()
            })
        alertDialog.show()
    }

    private fun showProductDialog(button : Button){
        //extract product info selected from button

        //show dialog
        val dialog = Dialog(this)
        dialog.setContentView(R.layout.dialog_product)
        //set text and icon
        val titleLayout = dialog.findViewById<LinearLayout>(R.id.ll_product_label)
        val titleText = titleLayout.findViewById<HelveticaBoldTextView>(R.id.tv_category_click)
        val titleIcon = titleLayout.findViewById<ImageView>(R.id.iv_product_icon)

        titleText.text = button.text.toString()
        val image = button.compoundDrawables[0]
        titleIcon.setImageDrawable(image)
        Log.e("images", image.toString())
        dialog.show()



    }
}