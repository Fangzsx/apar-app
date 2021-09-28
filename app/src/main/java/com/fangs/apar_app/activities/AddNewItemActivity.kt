package com.fangs.apar_app.activities

import android.os.Bundle
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

        //populate spinner with string array from resource
        ArrayAdapter.createFromResource(
            this, R.array.products_category,
            R.layout.support_simple_spinner_dropdown_item
        ).also { adapter ->
            adapter.setDropDownViewResource(R.layout.support_simple_spinner_dropdown_item)
            binding.spProductCategory.adapter = adapter

        }
    }
}