package com.fangs.apar_app.activities

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.fangs.apar_app.databinding.ActivityPurchaseBinding

class PurchaseActivity : AppCompatActivity() {
    private lateinit var binding : ActivityPurchaseBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        supportActionBar?.hide()
        super.onCreate(savedInstanceState)
        binding = ActivityPurchaseBinding.inflate(layoutInflater)

        setContentView(binding.root)

        val intent = intent
        val lastname = intent.getStringExtra("LAST_NAME")
        val firstname = intent.getStringExtra("FIRST_NAME")
        val middlename = intent.getStringExtra("MIDDLE_NAME")
        val fullname = "$lastname, $firstname $middlename"

        val houseST = intent.getStringExtra("HOUSE_ST")
        val phaseZone = intent.getStringExtra("PHASE_ZONE")
        val city = intent.getStringExtra("CITY")
        val fullAddress = "$houseST, $phaseZone $city"

        val birthday = intent.getStringExtra("BIRTHDAY")

        binding.tvCustomerName.text = fullname
        binding.tvCustomerAddress.text = fullAddress
        binding.tvCustomerBirthday.text = birthday




    }
}