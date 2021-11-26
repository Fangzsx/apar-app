package com.fangs.apar_app.activities

import android.content.Intent
import android.graphics.Color
import android.os.Bundle
import android.widget.ArrayAdapter
import android.widget.AutoCompleteTextView
import android.widget.Toast
import androidx.core.view.isVisible
import com.fangs.apar_app.R
import com.fangs.apar_app.databinding.ActivityUpdateItemBinding
import com.fangs.apar_app.utils.HelveticaNormalTextView
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import kotlinx.coroutines.tasks.await

class UpdateItemActivity : BaseActivity() {

    private lateinit var binding : ActivityUpdateItemBinding
    private var suggestions = mutableListOf<String>()
    private val productsCollectionRef = Firebase.firestore.collection("products")

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


        autoCompleteText.setOnItemClickListener { _, _, position, _ ->
            val currentProduct = adapter.getItem(position)
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
            }


        }





        //TODO: UPDATE PROPER
        binding.btnSideNavUpdate.setOnClickListener {

        }
    }

    private fun getRealTimeUpdates() {
        
        productsCollectionRef.addSnapshotListener { snapshot, error ->
            error?.let {
                Toast.makeText(this@UpdateItemActivity, error.message, Toast.LENGTH_SHORT).show()
            }
            snapshot?.let { documents ->
                for (document in documents){
                    suggestions.add(document["name"].toString())
                }
            }


        }
        
    }

}