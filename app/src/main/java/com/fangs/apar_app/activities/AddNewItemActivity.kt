package com.fangs.apar_app.activities

import android.content.Intent
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
import com.google.firebase.firestore.DocumentSnapshot
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.Runnable
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await

class AddNewItemActivity : BaseActivity(){

    private lateinit var binding : ActivityAddNewItemBinding
    private val productsCollectionRef = Firebase.firestore.collection("products")

    override fun onCreate(savedInstanceState: Bundle?) {
        supportActionBar?.hide()

        binding = ActivityAddNewItemBinding.inflate(layoutInflater)
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        populateSpinner()

        //add to database
        binding.btnAddToDatabase.setOnClickListener {
            //hide keyboard on btn click
            hideKeyboard(currentFocus ?: View(this))

            GlobalScope.launch {
                addItemToFirebase()
            }
        }
        //back key
        binding.navBack.setNavigationOnClickListener {
            Intent(this, MainActivity::class.java).also {
                startActivity(it)
            }
            finish()
        }
    }
    private suspend fun addItemToFirebase(){

        if(validateProduct()){

            val productName = binding.etNewProductName.text.toString().trim().lowercase()
            val productPrice = binding.etNewProductPrice.text.toString().trim().toDouble()
            val productCategory = binding.spNewProductCategory.selectedItem.toString().lowercase()
            //open firestore
            productsCollectionRef.add(
                hashMapOf(
                    "category" to productCategory,
                    "name" to productName,
                    "price" to productPrice
                )
            )

            Thread(Runnable {
                this.runOnUiThread {
                    Toast.makeText(
                        this@AddNewItemActivity,
                        "A new item was added to database.",
                        Toast.LENGTH_SHORT
                    ).show()
                }
            }).start()


            Intent(this, MainActivity::class.java).also {
                startActivity(it)
            }
            finish()
        }

    }




    private suspend fun validateProduct() : Boolean{

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
            //check if this item already exist on firestore, if exist, return false
            isProductExisting(newProductName) ->{
                showErrorSnackBar(binding.root, "Product already exists.", true)
                false
            }

            newProductPrice.toDouble() <=  0 -> {
                showErrorSnackBar(binding.root, "Price cannot be equal or less than 0", true)
                false
            }


            else -> {
                true
            }
        }
    }

    private suspend fun getDocumentsOnFirestore() : List<DocumentSnapshot>{
        val snapshot = productsCollectionRef.get().await()
        return snapshot.documents
    }

    private suspend fun isProductExisting(productName : String) : Boolean{
        var isExisting = false
        val documents = getDocumentsOnFirestore()
        for(document in documents){
            if(document["name"].toString() == productName){
                isExisting = true
            }

        }
        return isExisting
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
                    tv.setTextColor(ContextCompat.getColor(applicationContext, R.color.black))
                }

                override fun onNothingSelected(parent: AdapterView<*>?) {

                }

            }
        }
    }


}