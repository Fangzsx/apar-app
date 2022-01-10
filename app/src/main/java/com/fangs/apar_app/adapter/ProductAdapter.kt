package com.fangs.apar_app.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.fangs.apar_app.R
import com.fangs.apar_app.utils.HelveticaBoldTextView
import com.google.firebase.firestore.DocumentSnapshot
import com.google.firebase.firestore.QueryDocumentSnapshot

class ProductAdapter(private val dataSet : MutableList<DocumentSnapshot>) : RecyclerView.Adapter<ProductAdapter.ViewHolder>(){

    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val productName : HelveticaBoldTextView
        init {
            productName = itemView.findViewById(R.id.tv_product_name_search)
        }

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.product_row,parent,false)
        return ViewHolder(view)

    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {

        holder.productName.text = dataSet[position] ["name"].toString()

    }

    override fun getItemCount(): Int {
        return dataSet.size
    }

}