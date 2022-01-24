package com.fangs.apar_app.adapter

import android.graphics.Color
import android.view.Gravity
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.LinearLayout
import androidx.cardview.widget.CardView
import androidx.core.view.marginStart
import androidx.recyclerview.widget.RecyclerView
import com.fangs.apar_app.R
import com.fangs.apar_app.model.Product
import com.fangs.apar_app.utils.HelveticaBoldTextView
import com.fangs.apar_app.utils.HelveticaNormalTextView
import java.math.RoundingMode
import java.text.DecimalFormat
import kotlin.math.roundToInt

class CartAdapter(private val orderList : MutableList<Product>) : RecyclerView.Adapter<CartAdapter.ViewHolder>(){

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


        val name = orderList[position].productName
        val price = orderList[position].productPrice
        val quantity = orderList[position].productQuantity

        holder.productName.text = name
        holder.productPrice.text = price.toString()
        holder.quantity.text = quantity.toString()
        holder.amount.text = "Total: ${roundOffDecimal(price * quantity)}"

    }

    override fun getItemCount(): Int = orderList.size

    private fun roundOffDecimal(number: Double): Double? {
        val df = DecimalFormat("#.##")
        df.roundingMode = RoundingMode.FLOOR
        return df.format(number).toDouble()
    }


}