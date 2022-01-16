package com.fangs.apar_app.adapter

import android.graphics.Color
import android.view.Gravity
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import androidx.cardview.widget.CardView
import androidx.core.view.marginStart
import androidx.recyclerview.widget.RecyclerView
import com.fangs.apar_app.R
import com.fangs.apar_app.utils.HelveticaBoldTextView
import com.fangs.apar_app.utils.HelveticaNormalTextView
import com.google.firebase.firestore.DocumentSnapshot
import com.google.firebase.firestore.QueryDocumentSnapshot

class ProductAdapter(private val dataSet : MutableList<DocumentSnapshot>) : RecyclerView.Adapter<ProductAdapter.ViewHolder>(){

    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val productName : HelveticaNormalTextView
        val productPrice : HelveticaNormalTextView
        val cardBg : CardView
        init {
            productName = itemView.findViewById(R.id.tv_product_name_search)
            productPrice = itemView.findViewById(R.id.tv_item_product_price)
            cardBg = itemView.findViewById(R.id.card_item)

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

        if(position %2 == 0){
            holder.cardBg.setCardBackgroundColor(Color.GRAY)
        }

        holder.productName.text = dataSet[position] ["name"].toString()
        holder.productPrice.text = dataSet[position] ["price"].toString()

    }

    override fun getItemCount(): Int {
        return dataSet.size
    }

}