package com.fangs.apar_app.activities

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.fangs.apar_app.databinding.ActivityOldMemberBinding

class OldMemberActivity : AppCompatActivity() {
    private lateinit var binding : ActivityOldMemberBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityOldMemberBinding.inflate(layoutInflater)
        setContentView(binding.root)




    }
}