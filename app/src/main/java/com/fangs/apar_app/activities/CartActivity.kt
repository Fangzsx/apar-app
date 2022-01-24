package com.fangs.apar_app.activities

import android.content.ActivityNotFoundException
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.fangs.apar_app.databinding.ActivityCartBinding
import android.widget.Toast

import android.content.Intent
import androidx.recyclerview.widget.LinearLayoutManager
import com.fangs.apar_app.adapter.CartAdapter
import com.fangs.apar_app.model.Cart
import com.fangs.apar_app.model.NewMember
import java.lang.StringBuilder


class CartActivity : AppCompatActivity() {
    private lateinit var binding : ActivityCartBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityCartBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val list = Cart.getList()
        //sort list
        list.sortBy { it.productCategory }
        val recyclerView = binding.rvCartedItems
        recyclerView.layoutManager = LinearLayoutManager(this)
        val adapter = CartAdapter(list)
        recyclerView.adapter = adapter

        binding.btnSend.setOnClickListener {

            val sb = StringBuilder()
            //input customer info
            sb.append(
                "Customer name: ${NewMember.lastName}, ${NewMember.firstName} ${NewMember.middleName}\n\n" +
                "Customer address: ${NewMember.address}\n\n"+
                "Contact number: ${NewMember.contactNumber}\n\n" +
                "Birthday: ${NewMember.birthday}\n\n" +
                "Purchase Order: \n"
            )

            for(product in list){
                sb.append(
                        "Item no: ${list.indexOf(product) + 1}\n" +
                        "Product name: ${product.productName}\n" +
                        "Category: ${product.productCategory}\n" +
                        "Quantity: ${product.productQuantity}\n\n")
            }

            sb.append("---NOTHING FOLLOWS---")


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