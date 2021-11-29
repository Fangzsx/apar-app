package com.fangs.apar_app.activities

import android.content.Intent
import android.os.Bundle
import com.fangs.apar_app.databinding.ActivityDeleteBinding

class DeleteItemActivity : BaseActivity() {
    private lateinit var binding : ActivityDeleteBinding


    override fun onCreate(savedInstanceState: Bundle?) {
        supportActionBar?.hide()
        super.onCreate(savedInstanceState)
        binding = ActivityDeleteBinding.inflate(layoutInflater)
        setContentView(binding.root)




        //cancel
        binding.navBackDelete.setOnClickListener {
            Intent(this, MainActivity::class.java).also{
                startActivity(it)
                finish()
            }
        }



    }

}