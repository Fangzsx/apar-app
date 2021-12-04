package com.fangs.apar_app.activities

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.fangs.apar_app.R
import com.fangs.apar_app.databinding.ActivityNewMemberBinding

class NewMemberActivity : AppCompatActivity() {

    private lateinit var binding : ActivityNewMemberBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityNewMemberBinding.inflate(layoutInflater)


        setContentView(binding.root)


    }
}