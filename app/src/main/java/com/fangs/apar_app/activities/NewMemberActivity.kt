package com.fangs.apar_app.activities

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.fangs.apar_app.databinding.ActivityNewMemberBinding

class NewMemberActivity : AppCompatActivity() {

    private lateinit var binding : ActivityNewMemberBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityNewMemberBinding.inflate(layoutInflater)
        binding.sideBarNewMemberBack.setNavigationOnClickListener{
            Intent(this, MainActivity::class.java).also {
                startActivity(it)
                finish()
            }
        }


        setContentView(binding.root)


    }
}