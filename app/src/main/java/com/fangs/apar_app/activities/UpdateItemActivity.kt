package com.fangs.apar_app.activities

import android.content.Intent
import android.graphics.Color
import android.os.Bundle
import android.widget.AutoCompleteTextView
import androidx.core.view.isVisible
import com.fangs.apar_app.R
import com.fangs.apar_app.databinding.ActivityUpdateItemBinding

class UpdateItemActivity : BaseActivity() {

    private lateinit var binding : ActivityUpdateItemBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        supportActionBar?.hide()
        super.onCreate(savedInstanceState)

        binding = ActivityUpdateItemBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.navBackUpdate.setOnClickListener {
            Intent(this, MainActivity::class.java).also{
                startActivity(it)
                finish()
            }
        }

        //TODO :setup search view
        val searchView = binding.svSideNavSearch
        val autoCompleteText = searchView.findViewById<AutoCompleteTextView>(R.id.search_src_text)



        //hide prod name, appears when search was success
        binding.llDisappearingText.isVisible = false

        //TODO: UPDATE PROPER
        binding.btnSideNavUpdate.setOnClickListener {

        }




    }

}