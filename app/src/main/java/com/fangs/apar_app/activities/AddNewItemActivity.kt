package com.fangs.apar_app.activities

import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.view.View
import android.widget.ArrayAdapter
import androidx.appcompat.app.AppCompatActivity
import com.fangs.apar_app.R
import com.fangs.apar_app.databinding.ActivityAddNewItemBinding

class AddNewItemActivity : AppCompatActivity() {

    private lateinit var binding : ActivityAddNewItemBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        binding = ActivityAddNewItemBinding.inflate(layoutInflater)
        super.onCreate(savedInstanceState)
        setContentView(binding.root)


        populateSpinner()


        //back key
        binding.navBack.setNavigationOnClickListener {
            Intent(this, MainActivity::class.java).also {
                startActivity(it)
            }
            finish()
        }
        
    }


    private fun validateProduct(){

        //TODO PRIORITY LEVEL : TOP
        val newProductName = binding.etNewProductName.text.toString().trim()
        val newProductPrice = binding.etNewProductPrice.text.toString().trim()



    }

    private fun populateSpinner() {
        ArrayAdapter.createFromResource(
            this, R.array.products_category,
            R.layout.support_simple_spinner_dropdown_item
        ).also { adapter ->
            adapter.setDropDownViewResource(R.layout.support_simple_spinner_dropdown_item)
            binding.spNewProductCategory.adapter = adapter

        }
    }
}