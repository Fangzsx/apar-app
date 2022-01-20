package com.fangs.apar_app.activities

import android.content.ActivityNotFoundException
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.fangs.apar_app.databinding.ActivityCartBinding
import android.widget.Toast

import android.content.Intent
import com.fangs.apar_app.model.Cart
import java.lang.StringBuilder


class CartActivity : AppCompatActivity() {
    private lateinit var binding : ActivityCartBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityCartBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.btnSend.setOnClickListener {
            val list = Cart.get()
            //sort list
            list.sortBy { it.productCategory }

            val sb = StringBuilder()
            for(product in list){
                sb.append("${product.productName} ${product.productCategory} ${product.productQuantity} ${product.productPrice}\n\n")
            }


            val sendIntent = Intent()
            sendIntent.action = Intent.ACTION_SEND
            sendIntent
                .putExtra(
                    Intent.EXTRA_TEXT,
                    sb.toString()
                )
            sendIntent.type = "text/plain"
            sendIntent.setPackage("com.facebook.orca")
            try {
                startActivity(sendIntent)
            } catch (ex: ActivityNotFoundException) {
                Toast.makeText(this, "Please Install Facebook Messenger", Toast.LENGTH_LONG)
                    .show()
            }
        }


    }
}