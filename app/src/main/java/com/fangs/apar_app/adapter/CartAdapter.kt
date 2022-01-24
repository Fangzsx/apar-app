package com.fangs.apar_app.adapter

import android.content.Context
import android.graphics.Color
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.Toast
import androidx.cardview.widget.CardView
import androidx.recyclerview.widget.RecyclerView
import com.fangs.apar_app.R
import com.fangs.apar_app.model.Cart
import com.fangs.apar_app.model.Product
import com.fangs.apar_app.utils.HelveticaBoldTextView
import com.fangs.apar_app.utils.HelveticaNormalTextView
import java.math.RoundingMode
import java.text.DecimalFormat

class CartAdapter(private val context : Context, private val orderList : MutableList<Product>) : RecyclerView.Adapter<CartAdapter.ViewHolder>(){

    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val productName : HelveticaNormalTextView
        val productPrice : HelveticaNormalTextView
        val quantity : HelveticaBoldTextView
        val amount : HelveticaBoldTextView
        val btnRemove : ImageView
        val btnAdd : HelveticaBoldTextView
        val btnSubtract : HelveticaBoldTextView
        val card : CardView

        init {
            productName = itemView.findViewById(R.id.tv_product_name_cart)
            productPrice = itemView.findViewById(R.id.tv_item_product_price_cart)
            quantity = itemView.findViewById(R.id.tv_quantity_cart)
            amount = itemView.findViewById(R.id.tv_amount_cart)
            btnRemove = itemView.findViewById(R.id.iv_remove)
            btnAdd = itemView.findViewById(R.id.tv_add_quantity_cart)
            btnSubtract = itemView.findViewById(R.id.tv_subtract_quantity_cart)
            card = itemView.findViewById(R.id.card_cart_item)
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.cart_row, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {

        //change bg of card
        if (position % 2 == 0){
            holder.card.setCardBackgroundColor(Color.WHITE)
        }else{
            holder.card.setCardBackgroundColor(Color.GRAY)
        }
        val currentProduct = orderList[position]
        val name = currentProduct.productName
        val price = currentProduct.productPrice
        val quantity = currentProduct.productQuantity

        holder.productName.text = name
        holder.productPrice.text = price.toString()
        holder.quantity.text = quantity.toString()
        holder.amount.text = "Total: ${roundOffDecimal(price * quantity)}"


        //controls
        holder.btnAdd.setOnClickListener {
            if(quantity <= 99){
                currentProduct.productQuantity++
                //update value of quantity
                holder.quantity.text = currentProduct.productQuantity.toString()
                Toast.makeText(context, "quantity: ${currentProduct.productQuantity}", Toast.LENGTH_SHORT).show()
            }

        }

        holder.btnSubtract.setOnClickListener {
            Toast.makeText(context, "subtract clicked", Toast.LENGTH_SHORT).show()
        }

        holder.btnRemove.setOnClickListener {
            val list = Cart.getList()
            list.remove(currentProduct)
            notifyItemRemoved(position)
            Toast.makeText(context, "delete clicked.", Toast.LENGTH_SHORT).show()
        }

    }

    override fun getItemCount(): Int = orderList.size

    private fun roundOffDecimal(number: Double): Double? {
        val df = DecimalFormat("#.##")
        df.roundingMode = RoundingMode.FLOOR
        return df.format(number).toDouble()
    }


}