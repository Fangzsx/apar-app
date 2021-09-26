package com.fangs.apar_app.activities

import android.content.Intent
import android.os.Bundle
import com.fangs.apar_app.databinding.ActivityLoginBinding
import com.google.android.material.snackbar.Snackbar

class LoginActivity : BaseActivity() {

    private lateinit var binding : ActivityLoginBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityLoginBinding.inflate(layoutInflater)
        setContentView(binding.root)


        binding.btnLogin.setOnClickListener {



            Intent(this, MainActivity::class.java).also {
                startActivity(it)
            }
            Snackbar.make(binding.root, "login", Snackbar.LENGTH_LONG).show()


        }


    }

}