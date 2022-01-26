package com.fangs.apar_app.fragments

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.fangs.apar_app.activities.NewMemberActivity
import com.fangs.apar_app.activities.OldMemberActivity
import com.fangs.apar_app.databinding.FragmentPurchaseBinding

class PurchaseFragment : Fragment() {

    private lateinit var binding : FragmentPurchaseBinding

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        binding = FragmentPurchaseBinding.inflate(inflater, container, false)

        //back to main

        binding.btnNewMember.setOnClickListener {
            Intent(context, NewMemberActivity::class.java).also {
                startActivity(it)
            }
        }

        binding.btnOldMember.setOnClickListener {
            Intent(context, OldMemberActivity::class.java).also {
                startActivity(it)
            }
        }

        return binding.root
    }

}