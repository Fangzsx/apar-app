package com.fangs.apar_app.activities

import android.content.Intent
import android.graphics.Color
import android.os.Bundle
import android.text.TextUtils
import android.view.View
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.TextView
import android.widget.Toast
import androidx.core.content.ContextCompat
import com.fangs.apar_app.R
import com.fangs.apar_app.databinding.ActivityAddNewItemBinding
import com.google.firebase.firestore.FirebaseFirestore

class AddNewItemActivity : BaseActivity(){

    private lateinit var binding : ActivityAddNewItemBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        binding = ActivityAddNewItemBinding.inflate(layoutInflater)
        super.onCreate(savedInstanceState)
        setContentView(binding.root)


        populateSpinner()

        //add to database
        binding.btnAddToDatabase.setOnClickListener {
            //hide keyboard on btn click
            hideKeyboard(currentFocus ?: View(this))

            addItemToFirebase()

        }


        //back key
        binding.navBack.setNavigationOnClickListener {
            Intent(this, MainActivity::class.java).also {
                startActivity(it)
            }
            finish()
        }
        
    }

    private fun addItemToFirebase(){

        if(validateProduct()){

            val productName = binding.etNewProductName.text.toString().trim().lowercase()
            val productPrice = binding.etNewProductPrice.text.toString().trim().toDouble()
            val productCategory = binding.spNewProductCategory.selectedItem.toString().lowercase()




            //open firestore
            val root = FirebaseFirestore.getInstance()
            root.collection("products").add(
                hashMapOf(
                    "category" to productCategory,
                    "name" to productName,
                    "price" to productPrice
                )
            )

            Toast.makeText(applicationContext, "A new item was added to database.", Toast.LENGTH_SHORT).show()
            Intent(this, MainActivity::class.java).also {
                startActivity(it)
            }
            finish()


        }

    }




    private fun validateProduct() : Boolean{

        //TODO PRIORITY LEVEL : TOP
        val newProductName = binding.etNewProductName.text.toString().trim()
        val newProductPrice = binding.etNewProductPrice.text.toString().trim()
        val newProductCategory = binding.spNewProductCategory.selectedItem.toString()

        return when{
            TextUtils.isEmpty(newProductName) -> {
                showErrorSnackBar(binding.root, "Product name cannot be empty.", true)
                false

            }
            TextUtils.isEmpty(newProductPrice) -> {
                showErrorSnackBar(binding.root, "Price cannot be empty.", true)
                false
            }

            //make sure the first element is not a valid category
            newProductCategory == binding.spNewProductCategory.getItemAtPosition(0) -> {
                showErrorSnackBar(binding.root, "Please select a valid category.", true)
                false
            }

            else -> {
                true
            }

        }





    }

    private fun populateSpinner() {

        val spinner = binding.spNewProductCategory

        ArrayAdapter.createFromResource(
            this, R.array.products_category,
            R.layout.support_simple_spinner_dropdown_item
        ).also { adapter ->
            adapter.setDropDownViewResource(R.layout.support_simple_spinner_dropdown_item)


            spinner.adapter = adapter

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
                    tv.setTextColor(ContextCompat.getColor(applicationContext, R.color.red))
                }

                override fun onNothingSelected(parent: AdapterView<*>?) {

                }

            }
        }
    }


}