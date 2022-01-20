package com.fangs.apar_app.model

object Cart {
    private val list = mutableListOf<Product>()

    fun add(product: Product) {
        list.add(product)
    }

    fun get() : MutableList<Product> = list

}