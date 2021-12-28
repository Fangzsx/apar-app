package com.fangs.apar_app.activities

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.fangs.apar_app.R
import com.fangs.apar_app.databinding.ActivityPurchaseBinding

class PurchaseActivity : AppCompatActivity() {
    private lateinit var binding : ActivityPurchaseBinding


    override fun onCreate(savedInstanceState: Bundle?) {
        supportActionBar?.hide()
        super.onCreate(savedInstanceState)
        binding = ActivityPurchaseBinding.inflate(layoutInflater)
        setContentView(binding.root)


    }
}