package com.fangs.apar_app.activities

import android.app.Dialog
import android.content.DialogInterface
import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.*
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.isVisible
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.fangs.apar_app.R
import com.fangs.apar_app.adapter.ProductAdapter
import com.fangs.apar_app.databinding.ActivityPurchaseNewMemberBinding
import com.fangs.apar_app.model.Cart
import com.fangs.apar_app.model.NewMember
import com.fangs.apar_app.utils.HelveticaBoldTextView
import com.fangs.apar_app.utils.HelveticaCustomButton
import com.fangs.apar_app.utils.HelveticaNormalTextView
import com.google.firebase.firestore.DocumentSnapshot
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await
import kotlinx.coroutines.withContext

class NewMemberPurchaseActivity : AppCompatActivity(), View.OnClickListener {
    private lateinit var binding : ActivityPurchaseNewMemberBinding
    private val productCollectionRef = Firebase.firestore.collection("products")


    override fun onBackPressed() {
        showAlertDialog()
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        supportActionBar?.hide()
        super.onCreate(savedInstanceState)
        binding = ActivityPurchaseNewMemberBinding.inflate(layoutInflater)

        setContentView(binding.root)
        showCustomerData()
        setButtonClick()
        
        binding.customerCart.setOnClickListener{
            Intent(this, NewMemberCartActivity::class.java).also {
                startActivity(it)
            }
        }

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

        binding.tvCustomerName.text = "${NewMember.lastName}, ${NewMember.firstName} ${NewMember.middleName}"
        binding.tvCustomerAddress.text = NewMember.address
        binding.tvCustomerBirthday.text = NewMember.birthday
        binding.tvCustomerContact.text = NewMember.contactNumber


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
                Cart.getList().clear()
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

        val tvEmptyList = dialog.findViewById<HelveticaNormalTextView>(R.id.tv_list_empty)

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
                    if(list.isEmpty()){
                        recyclerView.isVisible = false
                        tvEmptyList.isVisible = true
                    }else{
                        recyclerView.layoutManager = LinearLayoutManager(this@NewMemberPurchaseActivity)
                        val adapter = ProductAdapter(this@NewMemberPurchaseActivity, list, category)
                        recyclerView.adapter = adapter


                    }

                }

            }catch (e : Exception){
                withContext(Dispatchers.Main){
                    Toast.makeText(this@NewMemberPurchaseActivity, e.message, Toast.LENGTH_LONG).show()
                }
            }
        }
        dialog.show()
    }

    
    override fun onClick(p0: View?) {
        showProductDialog(p0 as Button)

    }
}