package com.fangs.apar_app.activities

import android.annotation.SuppressLint
import android.app.Dialog
import android.content.Intent
import android.os.Bundle
import android.text.TextUtils
import android.view.View
import com.fangs.apar_app.R
import com.fangs.apar_app.databinding.ActivityNewMemberBinding
import com.google.firebase.auth.FirebaseAuth
import java.text.ParseException
import java.text.SimpleDateFormat
import java.util.*
import android.view.Gravity
import android.view.Window

import android.view.WindowManager
import com.fangs.apar_app.utils.HelveticaCustomButton
import com.fangs.apar_app.utils.HelveticaNormalTextView


class NewMemberActivity : BaseActivity() {

    private lateinit var binding: ActivityNewMemberBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        supportActionBar?.hide()
        super.onCreate(savedInstanceState)
        binding = ActivityNewMemberBinding.inflate(layoutInflater)
        setContentView(binding.root)


        //back navigation
        binding.sideBarNewMemberBack.setNavigationOnClickListener {
            Intent(this, MainActivity::class.java).also {
                startActivity(it)
                finish()
            }
        }

        //validate
        binding.btnSubmitNewMember.setOnClickListener {
            hideKeyboard(currentFocus ?: View(this))
            if (isValidCustomerInfo()) {
                val lastname = binding.etNewMemberLastname.text.toString()
                val firstname = binding.etNewMemberFirstname.text.toString()
                val middlename = binding.etNewMemberMiddlename.text.toString()
                val houseSt = binding.etNewMemberHouseNoSt.text.toString()
                val phaseSubd = binding.etNewMemberPhaseSubd.text.toString()
                val city = binding.etNewMemberCity.text.toString()
                val contactNumber = binding.etNewMemberContactNumber.text.toString()
                val birthday = binding.etNewMemberBirthday.text.toString()

                val customerInfoDialog = Dialog(this)
                customerInfoDialog.setContentView(R.layout.dialog_new_member_info)
                val window: Window? = customerInfoDialog.window
                window?.setLayout(
                    WindowManager.LayoutParams.MATCH_PARENT,
                    WindowManager.LayoutParams.WRAP_CONTENT
                )
                window?.setGravity(Gravity.CENTER)

                //TODO: show member info inside dialog
                val tvLastname = customerInfoDialog.findViewById<HelveticaNormalTextView>(R.id.tv_lastname_new)
                val tvFirstname = customerInfoDialog.findViewById<HelveticaNormalTextView>(R.id.tv_firstname_new)
                val tvMiddlename = customerInfoDialog.findViewById<HelveticaNormalTextView>(R.id.tv_middlename_new)
                val tvHouseSt = customerInfoDialog.findViewById<HelveticaNormalTextView>(R.id.tv_housest_new)
                val tvPhaseZone = customerInfoDialog.findViewById<HelveticaNormalTextView>(R.id.tv_phasezone_new)
                val tvCity = customerInfoDialog.findViewById<HelveticaNormalTextView>(R.id.tv_city_new)
                val tvContact = customerInfoDialog.findViewById<HelveticaNormalTextView>(R.id.tv_contact_new)
                val tvBirthday = customerInfoDialog.findViewById<HelveticaNormalTextView>(R.id.tv_birthday_new)

                tvLastname.text = lastname.uppercase()
                tvFirstname.text = firstname.uppercase()
                if(middlename.isEmpty()){
                    tvMiddlename.text = "N.A"
                }else{
                    tvMiddlename.text = middlename.uppercase()
                }

                tvHouseSt.text = houseSt.uppercase()
                tvPhaseZone.text = phaseSubd.uppercase()
                tvCity.text = city.uppercase()
                tvContact.text = contactNumber
                tvBirthday.text = birthday



                //button sends to purchase activity
                val btnOrder = customerInfoDialog.findViewById<HelveticaCustomButton>(R.id.btn_order)
                btnOrder.setOnClickListener {
                    Intent(this, PurchaseActivity::class.java).also {
                        //send data to purchase activity
                        it.putExtra("LAST_NAME", tvLastname.text.toString())
                        it.putExtra("FIRST_NAME", tvFirstname.text.toString())
                        it.putExtra("MIDDLE_NAME", tvMiddlename.text.toString())
                        customerInfoDialog.dismiss()
                        startActivity(it)
                    }
                }

                customerInfoDialog.show()
            }
        }


    }

    private fun isValidCustomerInfo(): Boolean {
        val etLastName = binding.etNewMemberLastname.text.toString().trim()
        val etFirstName = binding.etNewMemberFirstname.text.toString().trim()
        val etHouseSt = binding.etNewMemberHouseNoSt.text.toString().trim()
        val etPhaseSubd = binding.etNewMemberPhaseSubd.text.toString().trim()
        val etCity = binding.etNewMemberCity.text.toString().trim()
        val etContactNumber = binding.etNewMemberContactNumber.toString()
        val etBirthday = binding.etNewMemberBirthday.text.toString()

        return when {
            TextUtils.isEmpty(etLastName) -> {
                showErrorSnackBar(binding.root, "Last name cannot be empty", true)
                false
            }
            TextUtils.isEmpty(etFirstName) -> {
                showErrorSnackBar(binding.root, "First name cannot be empty", true)
                false
            }

            TextUtils.isEmpty(etLastName) -> {
                showErrorSnackBar(binding.root, "Last name cannot be empty", true)
                false
            }
            TextUtils.isEmpty(etHouseSt) -> {
                showErrorSnackBar(binding.root, "House# and Street cannot be empty", true)
                false
            }
            TextUtils.isEmpty(etPhaseSubd) -> {
                showErrorSnackBar(binding.root, "Phase/Zone/Subdivision cannot be empty", true)
                false
            }
            TextUtils.isEmpty(etCity) -> {
                showErrorSnackBar(binding.root, "City cannot be empty", true)
                false
            }
            TextUtils.isEmpty(etContactNumber) -> {
                showErrorSnackBar(binding.root, "Contact cannot be empty", true)
                false
            }
            TextUtils.isEmpty(etBirthday) || isDateValid(etBirthday) -> {
                showErrorSnackBar(binding.root, "Please input a valid date.", true)
                false
            }

            else -> {

                true
            }
        }

    }

    @SuppressLint("SimpleDateFormat")
    private fun isDateValid(date: String): Boolean {
        val inputFormat = SimpleDateFormat("yyyy-MM-dd")
        val outputFormat = SimpleDateFormat("yyyy-MM-dd")
        return try{
            val rawDate = inputFormat.parse(date)
            outputFormat.format(rawDate!!)
            false
        }catch (e : ParseException){
            true
        }




    }
}