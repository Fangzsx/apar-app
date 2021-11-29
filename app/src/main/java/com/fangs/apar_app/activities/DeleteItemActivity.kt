package com.fangs.apar_app.activities

import android.content.Intent
import android.os.Bundle
import android.widget.ArrayAdapter
import android.widget.AutoCompleteTextView
import android.widget.Toast
import androidx.core.view.isVisible
import com.fangs.apar_app.R
import com.fangs.apar_app.databinding.ActivityDeleteBinding
import com.fangs.apar_app.utils.HelveticaNormalTextView
import com.google.firebase.firestore.DocumentSnapshot
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.ListenerRegistration
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase

class DeleteItemActivity : BaseActivity() {
    private lateinit var binding : ActivityDeleteBinding
    private lateinit var listener : ListenerRegistration
    private var suggestions = mutableListOf<String>()
    private var documents = mutableListOf<DocumentSnapshot>()
    private val productsCollectionRef = Firebase.firestore.collection("products")

    override fun onCreate(savedInstanceState: Bundle?) {
        supportActionBar?.hide()
        super.onCreate(savedInstanceState)
        binding = ActivityDeleteBinding.inflate(layoutInflater)
        setContentView(binding.root)
        getRealTimeUpdates()

        val searchView = binding.svSideNavSearch
        val autoCompleteTextView = searchView.findViewById<AutoCompleteTextView>(R.id.search_src_text)
        val adapter = ArrayAdapter(this, android.R.layout.simple_dropdown_item_1line, suggestions)
        autoCompleteTextView.setAdapter(adapter)

        autoCompleteTextView.setOnItemClickListener { _, _, position, _ ->
            searchView.onActionViewCollapsed()
            searchView.onActionViewCollapsed()
            searchView.isIconified = false
            searchView.clearFocus()

            val currentProduct = adapter.getItem(position)
            //find doc with product name = current product
            for(document in documents){
                if(document["name"].toString() == currentProduct){
                    binding.tvProductName.text = document["name"].toString().uppercase()
                    binding.tvProductCategory.text = document["category"].toString().uppercase()
                    binding.tvProductPrice.text = document["price"].toString()
                }
            }
            val frame = binding.flProductInfo
            frame.isVisible = true
        }


        //cancel
        binding.navBackDelete.setOnClickListener {
            Intent(this, MainActivity::class.java).also{
                startActivity(it)
                finish()
            }
        }
    }

    private fun getRealTimeUpdates(){
        listener = productsCollectionRef.addSnapshotListener { snapshot, error ->
            error?.let {
                Toast.makeText(this@DeleteItemActivity, error.message, Toast.LENGTH_SHORT).show()
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