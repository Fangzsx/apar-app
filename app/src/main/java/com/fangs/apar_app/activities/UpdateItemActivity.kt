package com.fangs.apar_app.activities

import android.content.Intent
import android.graphics.Color
import android.os.Bundle
import android.view.View
import android.widget.*
import androidx.core.content.ContextCompat
import androidx.core.view.isVisible
import com.fangs.apar_app.R
import com.fangs.apar_app.databinding.ActivityUpdateItemBinding
import com.fangs.apar_app.utils.HelveticaNormalTextView
import com.google.firebase.firestore.DocumentSnapshot
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import kotlinx.coroutines.tasks.await

class UpdateItemActivity : BaseActivity() {

    private lateinit var binding : ActivityUpdateItemBinding
    private var suggestions = mutableListOf<String>()
    private val productsCollectionRef = Firebase.firestore.collection("products")
    private val documents = mutableListOf<DocumentSnapshot>()

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

                for(document in documents){
                    if(document["name"] == hiddenTv.text.toString().lowercase()){
                        etNewProductName.setText(document["name"].toString())
                        etNewProductPrice.setText(document["price"].toString())
                        populateSpinner(spinner, document["category"].toString())
                        break
                    }
                }
            }
        }





        //TODO: UPDATE PROPER
        binding.btnSideNavUpdate.setOnClickListener {

        }
    }

    private fun populateSpinner(spinner : Spinner, defaultValue : String) {
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

    private fun getRealTimeUpdates() {
        
        productsCollectionRef.addSnapshotListener { snapshot, error ->
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

}