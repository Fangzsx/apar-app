package com.fangs.apar_app.activities

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.View
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
            composeEmail()
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

    private fun composeEmail() {
        val to = "jygrzn@gmail.com"
        val subject = "[APAR APP]Request Access"
        val body =
                "I am sending you an email to request for an access for the APAR which I have installed.\n\n" +
                "Kindly fill-out the following: \n" +
                "Last Name: \n First Name: \n Middle Name: \n\n" +
                "APAR no: \n"+
                "Store Code: \n" +
                "Region: SOUTH GMA\n" +
                "Cluster number: \n\n" +
                "For DEMO, just leave all spaces blank. This is to limit the access to avoid Firebase charges. "+
                "Thank you!"

        val mailTo = "mailto:" + to +
                "?&subject=" + Uri.encode(subject) +
                "&body=" + Uri.encode(body)
        val emailIntent = Intent(Intent.ACTION_VIEW)
        emailIntent.data = Uri.parse(mailTo)
        startActivity(emailIntent)
    }
}
