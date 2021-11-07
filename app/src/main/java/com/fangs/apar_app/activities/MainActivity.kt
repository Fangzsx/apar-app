package com.fangs.apar_app.activities

import android.app.Dialog
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.*
import androidx.appcompat.widget.SearchView
import androidx.core.content.ContextCompat
import androidx.core.view.GravityCompat
import androidx.fragment.app.Fragment
import com.fangs.apar_app.R
import com.fangs.apar_app.R.id.sp_update
import com.fangs.apar_app.databinding.ActivityMainBinding
import com.fangs.apar_app.fragments.PurchaseFragment
import com.fangs.apar_app.fragments.ViewOrderFragment
import com.fangs.apar_app.utils.HelveticaBoldTextView
import com.fangs.apar_app.utils.HelveticaNormalTextView
import com.google.android.material.navigation.NavigationView
import com.google.android.material.textfield.TextInputEditText
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase

class MainActivity : BaseActivity() {

    private lateinit var binding : ActivityMainBinding
    private var suggestions = mutableListOf<String>()
    private val productsCollectionRef = Firebase.firestore.collection("products")

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        getRealTimeUpdates()



        //open drawer
        binding.toolbar.navDrawerToolbar.setNavigationOnClickListener {
            binding.drawer.openDrawer(GravityCompat.START)
        }


        manageToolbar()
        manageSideNavigation()
        manageBottomNavigation()


        //sign out
        binding.tvSignOut.setOnClickListener {
            FirebaseAuth.getInstance().signOut()
            Intent(this, LoginActivity::class.java).also {
                startActivity(it)
                finish()
            }
        }
    }

    private fun getRealTimeUpdates(){
        productsCollectionRef.addSnapshotListener { snapshot, error ->
            error?.let {
                Toast.makeText(this, error.message, Toast.LENGTH_SHORT).show()
                return@addSnapshotListener
            }

            snapshot?.let {
                for(document in it){
                    suggestions.add(document["name"].toString())
                }

            }
        }


    }


    private fun manageBottomNavigation() {
        //bottom navigation

        val purchaseFragment = PurchaseFragment()
        val viewOrderFragment = ViewOrderFragment()

        replaceFragment(purchaseFragment)
        binding.bottomNavigation.setOnItemSelectedListener {
            when (it.itemId) {
                R.id.btm_nav_purchase -> replaceFragment(purchaseFragment)
                R.id.btm_nav_view_orders -> replaceFragment(viewOrderFragment)


            }
            true
        }
    }

    private fun manageSideNavigation() {
        //side navigation
        val sideNav = NavigationView.OnNavigationItemSelectedListener { item ->
            when (item.itemId) {
                R.id.side_bar_add_item -> {
                    Intent(this, AddNewItemActivity::class.java).also {
                        startActivity(it)
                        finish()
                    }
                    return@OnNavigationItemSelectedListener true
                }
            }
            false
        }

        //set sideNav = navigation for sidebar
        binding.sideNavBar.setNavigationItemSelectedListener(sideNav)
    }

    private fun manageToolbar() {
        val searchView = binding.toolbar.searchView
        val autoCompleteTextView = searchView.findViewById<AutoCompleteTextView>(R.id.search_src_text)
        val searchViewAdapter = ArrayAdapter(this, android.R.layout.simple_dropdown_item_1line, suggestions)
        autoCompleteTextView.setAdapter(searchViewAdapter)


        autoCompleteTextView.setOnItemClickListener { _, _, position, _ ->

            val currentProduct = searchViewAdapter.getItem(position)

            //display data of current product using dialog
            val showItemDialog = Dialog(this, R.style.CustomDialog)
            showItemDialog.setContentView(R.layout.dialog_search)
            val dialogProdName = showItemDialog.findViewById<HelveticaNormalTextView>(R.id.tv_search_product_name)
            val dialogProductCategory = showItemDialog.findViewById<HelveticaNormalTextView>(R.id.tv_search_product_category)
            val dialogProductPrice = showItemDialog.findViewById<HelveticaNormalTextView>(R.id.tv_search_product_price)

            productsCollectionRef.whereEqualTo("name", currentProduct!!.lowercase()).get()
                .addOnSuccessListener { documents ->
                    for (document in documents) {
                        dialogProdName.text = document["name"].toString()
                        dialogProductCategory.text = document["category"].toString()
                        dialogProductPrice.text = document["price"].toString()
                    }
                }
                .addOnFailureListener { exception ->
                    Log.e("manage toolbar", exception.message.toString())
                }

            showItemDialog.show()


        }



    }

    private fun replaceFragment(fragment : Fragment) {

        val transaction = supportFragmentManager.beginTransaction()
        transaction.replace(R.id.fragment_container, fragment)
        transaction.commit()
    }




}