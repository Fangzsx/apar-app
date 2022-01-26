package com.fangs.apar_app.activities

import android.content.ActivityNotFoundException
import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import com.fangs.apar_app.adapter.CartAdapter
import com.fangs.apar_app.databinding.ActivityOldMemberCartBinding
import com.fangs.apar_app.model.Cart
import com.fangs.apar_app.model.OldMember


class OldMemberCartActivity : BaseActivity() {
    private lateinit var binding : ActivityOldMemberCartBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityOldMemberCartBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val list = Cart.getList()
        //get total from cart
        binding.tvTotalCart.text = "Total: P ${Cart.getTotal()}"

        //sort list
        list.sortBy { it.productCategory }


        val recyclerView = binding.rvCartedItems
        recyclerView.layoutManager = LinearLayoutManager(this)
        val adapter = CartAdapter(this@OldMemberCartActivity, list, object : CartAdapter.OnItemClickListener{
            override fun onItemClick(total: Double) {
                binding.tvTotalCart.text = "Total: P $total"
            }

        })
        recyclerView.adapter = adapter

        binding.sideBarCartBack.setOnClickListener {
            Intent(this, OldMemberPurchaseActivity::class.java).also {
                startActivity(it)
            }
        }

        binding.btnSend.setOnClickListener {

            if(list.isNotEmpty()){
                val sb = StringBuilder()
                //input customer info
                sb.append(
                    "Customer name: ${OldMember.lastName}, ${OldMember.firstName}")

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