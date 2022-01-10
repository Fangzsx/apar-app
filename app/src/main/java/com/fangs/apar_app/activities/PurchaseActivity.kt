package com.fangs.apar_app.activities

import android.app.Dialog
import android.content.DialogInterface
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.*
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.fangs.apar_app.R
import com.fangs.apar_app.adapter.ProductAdapter
import com.fangs.apar_app.databinding.ActivityPurchaseBinding
import com.fangs.apar_app.utils.HelveticaBoldTextView
import com.fangs.apar_app.utils.HelveticaCustomButton
import com.google.firebase.firestore.DocumentSnapshot
import com.google.firebase.firestore.ListenerRegistration
import com.google.firebase.firestore.QueryDocumentSnapshot
import com.google.firebase.firestore.QuerySnapshot
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await
import kotlinx.coroutines.withContext
import java.lang.Exception

class PurchaseActivity : AppCompatActivity(), View.OnClickListener {
    private lateinit var binding : ActivityPurchaseBinding
    private val productCollectionRef = Firebase.firestore.collection("products")


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
        dialog.setCancelable(false)
        //to dismiss
        val btnReturn = dialog.findViewById<HelveticaCustomButton>(R.id.btn_return)
        btnReturn.setOnClickListener{
            dialog.dismiss()
        }


        //set text and icon
        val titleText = dialog.findViewById<HelveticaBoldTextView>(R.id.tv_product_selected)
        val category = button.text.toString()


        //get text of button, set to titleText
        titleText.text = category.uppercase()
        //get all products within the category clicked

        CoroutineScope(Dispatchers.IO).launch {
            try {
                val querySnapshot = productCollectionRef.get().await()
                withContext(Dispatchers.Main){
                    val list = mutableListOf<DocumentSnapshot>()
                    for(document in querySnapshot.documents){
                        if(document["category"] == category.lowercase()){
                            list.add(document)
                        }
                    }
                    val recyclerView = dialog.findViewById<RecyclerView>(R.id.rv_products)
                    recyclerView.layoutManager = LinearLayoutManager(applicationContext)
                    val adapter = ProductAdapter(list)
                    recyclerView.adapter = adapter

                }

            }catch (e : Exception){
                withContext(Dispatchers.Main){
                    Toast.makeText(this@PurchaseActivity, e.message, Toast.LENGTH_LONG).show()
                }
            }

        }
        dialog.show()
    }




    override fun onClick(p0: View?) {
        showProductDialog(p0 as Button)

    }
}