package com.fangs.apar_app.activities

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.View
import android.widget.Toast
import com.fangs.apar_app.databinding.ActivityLoginBinding
import com.google.firebase.auth.FirebaseAuth

class LoginActivity : BaseActivity() {

    private lateinit var binding: ActivityLoginBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityLoginBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val auth = FirebaseAuth.getInstance()

        //check if user is already logged in
        val user = auth.currentUser
        if (user != null) {
            goToMainActivity()
        }

        binding.tvSendRequest.setOnClickListener {
            composeEmail("someemail@gmail.com", "REQUEST ACCOUNT")
        }

        binding.btnLogin.setOnClickListener {

            val email = binding.etEmail.text.toString().trim()
            val password = binding.etPassword.text.toString().trim()
            hideKeyboard(currentFocus ?: View(this))
            login(email, password, auth)
        }
    }

    private fun login(
        email: String,
        password: String,
        auth: FirebaseAuth
    ) {
        if (email.isNotEmpty() && password.isNotEmpty()) {
            auth.signInWithEmailAndPassword(email, password)
                .addOnCompleteListener { task ->

                    if (task.isSuccessful) {
                        goToMainActivity()
                    } else {
                        showErrorSnackBar(
                            binding.root,
                            "Account does not exist. Try again.",
                            true
                        )
                    }
                }
        } else {
            showErrorSnackBar(binding.root, "Email and Password cannot be empty!", true)
        }
    }

    private fun goToMainActivity() {
        Intent(this, MainActivity::class.java).also {
            startActivity(it)
            finish()
        }
    }

    private fun composeEmail(addresses : String, subject: String) {
        val myEmail = "jygrzn@gmail.com"
        val intent = Intent(Intent.ACTION_SENDTO);
        intent.data = Uri.parse("mailto:" + myEmail); // only email apps should handle this
        intent.putExtra(Intent.EXTRA_EMAIL, addresses);
        intent.putExtra(Intent.EXTRA_SUBJECT, subject)

        if (intent.resolveActivity(packageManager) != null) {
            startActivity(intent);
        }
    }
}