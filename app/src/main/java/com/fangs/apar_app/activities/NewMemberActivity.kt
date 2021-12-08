package com.fangs.apar_app.activities

import android.content.Intent
import android.os.Bundle
import android.text.TextUtils
import android.util.Log
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import com.fangs.apar_app.databinding.ActivityNewMemberBinding
import com.fangs.apar_app.dataclass.Customer
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import java.time.LocalDate
import java.time.format.DateTimeFormatter

class NewMemberActivity : BaseActivity() {

    private lateinit var binding : ActivityNewMemberBinding
    private val userID = FirebaseAuth.getInstance().currentUser!!.uid
    private val customersCollectionRef = Firebase.firestore.collection("users/$userID/customers")

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
            if(isValidCustomerInfo()){
                val lastname = binding.etNewMemberLastname.text.toString()
                val firstname = binding.etNewMemberFirstname.text.toString()
                val middlename = binding.etNewMemberMiddlename.text.toString()
                val houseSt = binding.etNewMemberHouseNoSt.text.toString()
                val phaseSubd = binding.etNewMemberPhaseSubd.text.toString()
                val city = binding.etNewMemberCity.text.toString()
                val contactNumber = binding.etNewMemberContactNumber.text.toString()
                val birthday = binding.etNewMemberBirthday.text.toString()

                //MAKE ORDER.

            }
        }




    }

    private fun isValidCustomerInfo() : Boolean{
        val etLastName = binding.etNewMemberLastname.text.toString().trim()
        val etFirstName = binding.etNewMemberFirstname.text.toString().trim()
        val etMiddleName = binding.etNewMemberMiddlename.text.toString().trim()
        val etHouseSt = binding.etNewMemberHouseNoSt.text.toString().trim()
        val etPhaseSubd = binding.etNewMemberPhaseSubd.text.toString().trim()
        val etCity = binding.etNewMemberCity.text.toString().trim()
        val etContactNumber = binding.etNewMemberContactNumber.toString()
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
                showErrorSnackBar(binding.root, "House# and Street cannot be empty", true)
                false
            }
            TextUtils.isEmpty(etPhaseSubd) ->{
                showErrorSnackBar(binding.root, "Phase/Zone/Subdivision cannot be empty", true)
                false
            }
            TextUtils.isEmpty(etCity) ->{
                showErrorSnackBar(binding.root, "City cannot be empty", true)
                false
            }
            TextUtils.isEmpty(etContactNumber) ->{
                showErrorSnackBar(binding.root, "Contact cannot be empty", true)
                false
            }
            TextUtils.isEmpty(etBirthday) ->{
                showErrorSnackBar(binding.root, "Birthday cannot be empty", true)
                false
            }

            else -> true
        }

    }

}