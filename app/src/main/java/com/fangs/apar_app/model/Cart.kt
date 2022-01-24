package com.fangs.apar_app.model

import java.math.RoundingMode
import java.text.DecimalFormat

object Cart {
    private val list = mutableListOf<Product>()

    fun add(product: Product) {
        list.add(product)
    }

    fun getList() : MutableList<Product> = list
    
    fun getTotal() : Double {
        var total = 0.0
        for (product in list){
            total += roundOffDecimal(product.productPrice * product.productQuantity)
        }
        return roundOffDecimal(total)
    }
    private fun roundOffDecimal(number: Double): Double {
        val df = DecimalFormat("#.##")
        df.roundingMode = RoundingMode.FLOOR
        return df.format(number).toDouble()
    }

}