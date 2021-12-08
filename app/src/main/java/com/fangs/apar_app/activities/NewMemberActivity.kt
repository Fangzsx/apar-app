package com.fangs.apar_app.activities

import android.content.Intent
import android.os.Bundle
import android.text.TextUtils
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import com.fangs.apar_app.databinding.ActivityNewMemberBinding

class NewMemberActivity : BaseActivity() {

    private lateinit var binding : ActivityNewMemberBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityNewMemberBinding.inflate(layoutInflater)
        setContentView(binding.root)


        //back navigation
        binding.sideBarNewMemberBack.setNavigationOnClickListener{
            Intent(this, MainActivity::class.java).also {
                startActivity(it)
                finish()
            }
        }

        //validate
        binding.btnSubmitNewMember.setOnClickListener {
            hideKeyboard(currentFocus ?: View(this))
            isValidCustomerInfo()
        }




    }

    private fun isValidCustomerInfo() : Boolean{
        val etLastName = binding.etNewMemberLastname.text.toString().trim()
        val etFirstName = binding.etNewMemberFirstname.text.toString().trim()
        val etMiddleName = binding.etNewMemberMiddlename.text.toString().trim()
        val etHouseSt = binding.etNewMemberHouseNoSt.text.toString().trim()
        val etPhaseSubd = binding.etNewMemberPhaseSubd.text.toString().trim()
        val etCity = binding.etNewMemberCity.text.toString().trim()
        val etBirthday = binding.etNewMemberBirthday.text.toString()

        return when{
            TextUtils.isEmpty(etLastName) ->{
                showErrorSnackBar(binding.root, "Last name cannot be empty", true)
                false
            }
            TextUtils.isEmpty(etFirstName) ->{
                showErrorSnackBar(binding.root, "First name cannot be empty", true)
                false
            }

            TextUtils.isEmpty(etLastName) ->{
                showErrorSnackBar(binding.root, "Last name cannot be empty", true)
                false
            }
            TextUtils.isEmpty(etHouseSt) ->{
                showErrorSnackBar(binding.root, "Last name cannot be empty", true)
                false
            }
            TextUtils.isEmpty(etPhaseSubd) ->{
                showErrorSnackBar(binding.root, "Last name cannot be empty", true)
                false
            }
            TextUtils.isEmpty(etCity) ->{
                showErrorSnackBar(binding.root, "Last name cannot be empty", true)
                false
            }
            TextUtils.isEmpty(etBirthday) ->{
                showErrorSnackBar(binding.root, "Last name cannot be empty", true)
                false
            }

            else -> true
        }

    }

}