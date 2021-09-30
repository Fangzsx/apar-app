package com.fangs.apar_app.activities

import android.content.Intent
import android.os.Bundle
import android.view.View
import com.fangs.apar_app.databinding.ActivityLoginBinding
import com.google.firebase.auth.FirebaseAuth

class LoginActivity : BaseActivity() {

    private lateinit var binding : ActivityLoginBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityLoginBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val auth = FirebaseAuth.getInstance()

        //check if user is already logged in
        val user = auth.currentUser
        if(user != null){

            Intent(this, MainActivity::class.java).also {
                startActivity(it)
                finish()
            }
        }


        binding.btnLogin.setOnClickListener {

            val email = binding.etEmail.text.toString().trim()
            val password = binding.etPassword.text.toString().trim()
            hideKeyboard(currentFocus ?: View(this))

            if(email.isNotEmpty() && password.isNotEmpty()){



                auth.signInWithEmailAndPassword(email, password)
                    .addOnCompleteListener { task ->

                        if(task.isSuccessful){
                            Intent(this, MainActivity::class.java).also {
                                startActivity(it)
                                finish()
                            }
                        }
                        else{
                            showErrorSnackBar(binding.root, "Account does not exist. Try again.", true)
                        }

                    }
            } else{
                showErrorSnackBar(binding.root, "Email and Password cannot be empty!", true)
            }



        }


    }

}