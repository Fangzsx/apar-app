package com.fangs.apar_app.activities

import android.app.AlertDialog
import android.content.Intent
import android.os.Bundle
import android.text.TextUtils
import com.fangs.apar_app.databinding.ActivityOldMemberBinding
import com.fangs.apar_app.model.OldMember

class OldMemberActivity : BaseActivity() {
    private lateinit var binding : ActivityOldMemberBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityOldMemberBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.btnPurchaseOld.setOnClickListener {
            if(validateName()){
                val lastname = binding.etOldMemberLastname.text.toString().uppercase()
                val firstname = binding.etOldMemberFirstname.text.toString().uppercase()

                //show dialog
                val alertDialog = AlertDialog.Builder(this)
                alertDialog.setTitle("Old Member")


                OldMember.lastName = lastname
                OldMember.firstName = firstname

                Intent(this, OldMemberPurchaseActivity::class.java).also {
                    startActivity(it)
                }
            }
        }

    }

    private fun validateName() : Boolean {
        val etLastName = binding.etOldMemberLastname.text?.trim()
        val etFirstName = binding.etOldMemberFirstname.text?.trim()

        return when{
            TextUtils.isEmpty(etLastName) ->{
                showErrorSnackBar(binding.root, "Last Name cannot be empty", true)
                false
            }

            TextUtils.isEmpty(etFirstName) ->{
                showErrorSnackBar(binding.root, "First Name cannot be empty", true)
                false
            }

            else -> true

        }
    }
}