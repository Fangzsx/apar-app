package com.fangs.apar_app.activities

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.fangs.apar_app.databinding.ActivityCartBinding

class CartActivity : AppCompatActivity() {
    private lateinit var binding : ActivityCartBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityCartBinding.inflate(layoutInflater)
        setContentView(binding.root)


    }
}