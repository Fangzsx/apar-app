package com.fangs.apar_app.activities

import android.app.ActionBar
import android.app.Dialog
import android.content.DialogInterface
import android.content.Intent
import android.graphics.drawable.Drawable
import android.os.Bundle
import android.util.Log
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.annotation.DimenRes
import androidx.annotation.DrawableRes
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import com.fangs.apar_app.R
import com.fangs.apar_app.databinding.ActivityPurchaseBinding
import com.fangs.apar_app.utils.HelveticaBoldTextView
import com.google.firebase.firestore.QueryDocumentSnapshot
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase

class PurchaseActivity : AppCompatActivity(), View.OnClickListener {
    private lateinit var binding : ActivityPurchaseBinding
    private val productCollectionRef = Firebase.firestore.collection("products")
    private var productList = mutableListOf<QueryDocumentSnapshot>()

    override fun onBackPressed() {
        showAlertDialog()
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        supportActionBar?.hide()
        super.onCreate(savedInstanceState)
        binding = ActivityPurchaseBinding.inflate(layoutInflater)

        setContentView(binding.root)

        showCustomerData()
        setButtonClick()

    }

    private fun setButtonClick() {
        //back navigation
        binding.sideBarPurchaseOrderBack.setOnClickListener {

            showAlertDialog()
        }

        binding.btnAmaxLoad.setOnClickListener(this)
        binding.btnBeverages.setOnClickListener(this)
        binding.btnCannedGoods.setOnClickListener(this)
        binding.btnCigarettesMed.setOnClickListener(this)
        binding.btnCondiments.setOnClickListener(this)
        binding.btnDetergent.setOnClickListener(this)
        binding.btnDiapers.setOnClickListener(this)
        binding.btnHousehold.setOnClickListener(this)
        binding.btnNoodles.setOnClickListener(this)
        binding.btnPowderedMilk.setOnClickListener(this)
        binding.btnShampooConditioner.setOnClickListener(this)
        binding.btnSoapHygiene.setOnClickListener(this)
        binding.btnSugar.setOnClickListener(this)
        binding.btnBisquits.setOnClickListener(this)


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
        val contact = intent.getStringExtra("CONTACT")

        binding.tvCustomerName.text = fullname
        binding.tvCustomerAddress.text = fullAddress
        binding.tvCustomerBirthday.text = birthday
        binding.tvCustomerContact.text = contact
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
        val titleText = dialog.findViewById<HelveticaBoldTextView>(R.id.tv_product_selected)
        val category = button.text.toString()

        //get text of button, set to titleText
        titleText.text = category.uppercase()
        //get all products within the category clicked
        retrieveProducts(category.lowercase())
        Log.e("list", productList.toString())





        dialog.show()
    }

    private fun retrieveProducts(category: String) {
        productCollectionRef.whereEqualTo("category", category.lowercase())
            .get()
            .addOnSuccessListener { documents ->
                Log.i("productSize", "No. Of Products: ${documents.size()}")
                for (document in documents) {
                    Log.i("product", "${document.id} => ${document.data}")
                    productList.add(document)
                }
            }
            .addOnFailureListener { error ->
                Log.e("query error", error.message.toString())
            }
    }


    override fun onClick(p0: View?) {
        showProductDialog(p0 as Button)
    }
}