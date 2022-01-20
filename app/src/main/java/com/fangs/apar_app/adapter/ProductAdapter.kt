package com.fangs.apar_app.adapter

import android.content.Context
import android.graphics.Color
import android.view.*
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.Toast
import androidx.cardview.widget.CardView
import androidx.core.view.marginStart
import androidx.recyclerview.widget.RecyclerView
import com.fangs.apar_app.R
import com.fangs.apar_app.model.Cart
import com.fangs.apar_app.model.Product
import com.fangs.apar_app.utils.HelveticaBoldTextView
import com.fangs.apar_app.utils.HelveticaNormalTextView
import com.google.firebase.firestore.DocumentSnapshot

class ProductAdapter(private val context : Context, private val dataSet : MutableList<DocumentSnapshot>, private val category : String) : RecyclerView.Adapter<ProductAdapter.ViewHolder>(){

    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val productName : HelveticaNormalTextView
        val productPrice : HelveticaNormalTextView
        val cardBg : CardView
        val tvAdd : HelveticaBoldTextView
        val tvQuantity : HelveticaBoldTextView
        val tvSubtract : HelveticaBoldTextView
        val ivCart : ImageView

        init {
            productName = itemView.findViewById(R.id.tv_product_name_search)
            productPrice = itemView.findViewById(R.id.tv_item_product_price)
            cardBg = itemView.findViewById(R.id.card_item)
            tvAdd = itemView.findViewById(R.id.tv_add_quantity)
            tvQuantity = itemView.findViewById(R.id.tv_quantity)
            tvSubtract = itemView.findViewById(R.id.tv_subtract_quantity)
            ivCart = itemView.findViewById(R.id.iv_add_to_cart)
        }

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.product_row,parent,false)
        val offset = view.marginStart
        val params = LinearLayout.LayoutParams(
            LinearLayout.LayoutParams.MATCH_PARENT,
            LinearLayout.LayoutParams.WRAP_CONTENT
        ).apply {
            gravity = Gravity.CENTER
            width = parent.width - offset
            setMargins(10,10,10,10)
        }
        view.layoutParams = params
        return ViewHolder(view)

    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {

        if(position % 2 == 0){
            holder.cardBg.setCardBackgroundColor(Color.WHITE)
        }else{
            holder.cardBg.setCardBackgroundColor(Color.GRAY)
        }
        
        //get product info
        val productName = dataSet[position] ["name"].toString()
        val productPrice = dataSet[position] ["price"].toString()
        var quantity = 0

        holder.productName.text = productName
        holder.productPrice.text = productPrice
        holder.tvQuantity.text = quantity.toString()

        holder.tvAdd.setOnClickListener {
            quantity++
            holder.tvQuantity.text = quantity.toString()
        }

        //change quantity
        holder.tvSubtract.setOnClickListener{
            if(quantity > 0 ){
                quantity--
                holder.tvQuantity.text = quantity.toString()
            }
        }

        holder.ivCart.setOnClickListener{
            val name = holder.productName.text.toString()
            val pcs = holder.tvQuantity.text.toString().toInt()
            val price = holder.productPrice.text.toString().toDouble()
            val finalPrice = pcs * price
            
            

            val product  =  Product(name, category,  pcs, finalPrice)
            Cart.add(product)

            //set quantity of item selected = 0
            holder.tvQuantity.text = 0.toString()
            Toast.makeText(context, "$productName added to list", Toast.LENGTH_SHORT).show()

            //TODO: extract category of the product to Order data class

        }
        

    }

    override fun getItemCount(): Int {
        return dataSet.size
    }



}