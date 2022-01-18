package com.fangs.apar_app.model

object Cart {
    private val list = mutableListOf<Order>()

    fun add(order: Order) {
        list.add(order)
    }

    fun get() : MutableList<Order> = list

}