package com.fangs.apar_app.activities

import android.content.Intent
import android.graphics.Color
import android.os.Bundle
import android.text.TextUtils
import android.widget.*
import androidx.core.view.isVisible
import com.fangs.apar_app.R
import com.fangs.apar_app.databinding.ActivityUpdateItemBinding
import com.fangs.apar_app.utils.HelveticaNormalTextView
import com.google.firebase.firestore.DocumentSnapshot
import com.google.firebase.firestore.ListenerRegistration
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase

class UpdateItemActivity : BaseActivity() {

    private lateinit var binding : ActivityUpdateItemBinding
    private var suggestions = mutableListOf<String>()
    private val productsCollectionRef = Firebase.firestore.collection("products")
    private val documents = mutableListOf<DocumentSnapshot>()
    private lateinit var listener : ListenerRegistration

    override fun onCreate(savedInstanceState: Bundle?) {
        supportActionBar?.hide()
        super.onCreate(savedInstanceState)

        //get all product names in document
        getRealTimeUpdates()
        

        binding = ActivityUpdateItemBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.navBackUpdate.setOnClickListener {
            Intent(this, MainActivity::class.java).also{
                startActivity(it)
                finish()
            }
        }

        //hide prod name, appears when search was success
        val disappearingText = binding.llDisappearingText
        val etNewProductName = binding.etSideNavProductName
        val etNewProductPrice = binding.etSideNavProductPrice

        disappearingText.isVisible = false

        //TODO :setup search view
        val searchView = binding.svSideNavSearch
        val autoCompleteText = searchView.findViewById<AutoCompleteTextView>(R.id.search_src_text)
        val adapter = ArrayAdapter(this, android.R.layout.simple_dropdown_item_1line, suggestions)
        autoCompleteText.setAdapter(adapter)

        //textInputs, initially grayed out.
        val tiProductName = binding.tiSideNavUpdateProductName
        val tiProductPrice = binding.tiSideNavUpdateProductPrice
        //spinner
        val spinner = binding.spSideNavUpdateNewCategory
        var productID : String? = null


        autoCompleteText.setOnItemClickListener { _, _, position, _ ->
            val currentProduct = adapter.getItem(position)
            //remove this item in list
            suggestions.remove(currentProduct)

            val hiddenTv = disappearingText.findViewById<HelveticaNormalTextView>(R.id.tv_side_nav_update_product_found)
            hiddenTv.text = currentProduct.toString().uppercase()
            disappearingText.isVisible = true
            searchView.onActionViewCollapsed()
            searchView.isIconified = false
            searchView.clearFocus()


            //if a product is found,
            //enable edit texts by changing bg of text inputs
            if(hiddenTv.text.toString().isNotBlank()){

                tiProductName.boxBackgroundColor = Color.WHITE
                etNewProductName.isEnabled = true

                tiProductPrice.boxBackgroundColor = Color.WHITE
                etNewProductPrice.isEnabled = true

                for(document in documents){
                    if(document["name"] == hiddenTv.text.toString().lowercase()){
                        etNewProductName.setText(document["name"].toString())
                        etNewProductPrice.setText(document["price"].toString())
                        populateSpinner(spinner, document["category"].toString())
                        productID = document.id
                        break
                    }
                }
            }
        }



        //TODO: UPDATE PROPER
        binding.btnSideNavUpdate.setOnClickListener {
            //validate
            val productName = etNewProductName.text.toString().trim().lowercase()
            val productPrice = etNewProductPrice.text.toString().toDouble()
            val productCategory = spinner.selectedItem.toString().uppercase()



            //update doc corresponding to the product selected
            if(validateProduct(productName, productCategory, productPrice)){
                productsCollectionRef.document(productID!!).update(
                    mapOf(
                        "name" to productName,
                        "category" to productCategory.lowercase(),
                        "price" to productPrice
                    )
                )
                Toast.makeText(this, "Product updated!", Toast.LENGTH_SHORT).show()
                //return to MainActivity
                Intent(this, MainActivity::class.java).also {
                    startActivity(it)
                }
                listener.remove()
                finish()
            }
        }
    }


    private fun getRealTimeUpdates() {
        
        listener = productsCollectionRef.addSnapshotListener { snapshot, error ->
            error?.let {
                Toast.makeText(this@UpdateItemActivity, error.message, Toast.LENGTH_SHORT).show()
            }
            snapshot?.let { docs ->
                for (doc in docs){
                    suggestions.add(doc["name"].toString())
                    documents.add(doc)
                }
            }
        }
    }

    private fun isExisting(name : String) : Boolean{
        val productNameList = suggestions.toMutableList()
        return productNameList.contains(name)
    }

    private fun validateProduct(name : String, category : String, price : Double) : Boolean{
        return when{
            TextUtils.isEmpty(name) -> {
                showErrorSnackBar(binding.root, "Product name cannot be empty.", true)
                false
            }
            category.uppercase() == "PLEASE SELECT FROM THE FF--" -> {
                showErrorSnackBar(binding.root, "Select a valid category.", true)
                false
            }
            price <= 0 -> {
                showErrorSnackBar(binding.root, "Price cannot be less than or equal to 0.", true)
                false
            }

            isExisting(name) -> {
                showErrorSnackBar(binding.root, "Product with the same name already exist.", true)
                false
            }
            else -> {
                true
            }
        }
    }

}