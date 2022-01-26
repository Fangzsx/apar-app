package com.fangs.apar_app.activities

import android.content.ActivityNotFoundException
import android.os.Bundle
import android.widget.Toast
import android.content.Intent
import androidx.recyclerview.widget.LinearLayoutManager
import com.fangs.apar_app.adapter.CartAdapter
import com.fangs.apar_app.databinding.ActivityNewMemberCartBinding
import com.fangs.apar_app.model.Cart
import com.fangs.apar_app.model.NewMember
import java.lang.StringBuilder


class NewMemberCartActivity : BaseActivity() {
    private lateinit var binding : ActivityNewMemberCartBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityNewMemberCartBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val list = Cart.getList()
        //get total from cart
        binding.tvTotalCart.text = "Total: P ${Cart.getTotal()}"

        //sort list
        list.sortBy { it.productCategory }


        val recyclerView = binding.rvCartedItems
        recyclerView.layoutManager = LinearLayoutManager(this)
        val adapter = CartAdapter(this@NewMemberCartActivity, list, object : CartAdapter.OnItemClickListener{
            override fun onItemClick(total: Double) {
                binding.tvTotalCart.text = "Total: P $total"
            }

        })
        recyclerView.adapter = adapter

        binding.btnSend.setOnClickListener {

            if(list.isNotEmpty()){
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
            }else{
                showErrorSnackBar(binding.root, "Cart cannot be empty.", true)
            }


        }


    }
}