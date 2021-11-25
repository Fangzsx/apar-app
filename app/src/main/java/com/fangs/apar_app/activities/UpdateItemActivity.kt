package com.fangs.apar_app.activities

import android.content.Intent
import android.graphics.Color
import android.os.Bundle
import androidx.core.view.isVisible
import com.fangs.apar_app.databinding.ActivityUpdateItemBinding

class UpdateItemActivity : BaseActivity() {

    private lateinit var binding : ActivityUpdateItemBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        supportActionBar?.hide()
        super.onCreate(savedInstanceState)

        binding = ActivityUpdateItemBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.navBackUpdate.setOnClickListener {
            Intent(this, MainActivity::class.java).also{
                startActivity(it)
                finish()
            }
        }

        binding.llDisappearingText.isVisible = false




    }

}