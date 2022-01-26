package com.fangs.apar_app.activities

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.fangs.apar_app.databinding.ActivityOldMemberPurchaseBinding
import com.fangs.apar_app.model.OldMember

class OldMemberPurchaseActivity : AppCompatActivity() {
    private lateinit var binding : ActivityOldMemberPurchaseBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityOldMemberPurchaseBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.tvCustomerNameOld.text = "${OldMember.lastName}, ${OldMember.firstName}"

    }
}