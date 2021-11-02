package com.fangs.apar_app.activities

import android.annotation.SuppressLint
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
import com.fangs.apar_app.databinding.ActivityMainBinding
import com.fangs.apar_app.fragments.PurchaseFragment
import com.fangs.apar_app.fragments.ViewOrderFragment
import com.fangs.apar_app.utils.HelveticaNormalTextView
import com.google.android.material.navigation.NavigationView
import com.google.android.material.textfield.TextInputEditText
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase

class MainActivity : BaseActivity() {

    private lateinit var binding : ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)




        showErrorSnackBar(binding.root, "Welcome Jay!", false)


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
                    }
                    return@OnNavigationItemSelectedListener true
                }
            }
            false
        }

        //set sideNav = navigation for sidebar
        binding.sideNavBar.setNavigationItemSelectedListener(sideNav)
    }

    @SuppressLint("SetTextI18n")
    private fun manageToolbar() {

        //search
        val searchView = binding.toolbar.searchView
        val productsRef = Firebase.firestore.collection("products")
        val productList = mutableListOf<String>()
        //get all products name in firebase
        productsRef.get()
            .addOnSuccessListener { documents ->
                for(document in documents){
                    productList.add(document["name"].toString())
                }
            }
            .addOnFailureListener { exception ->
                Toast.makeText(this, "auto complete text error. ${exception.message}", Toast.LENGTH_SHORT).show()
            }

        val arrayAdapter = ArrayAdapter(this, android.R.layout.simple_dropdown_item_1line, productList)
        val autoComplete = searchView.findViewById<AutoCompleteTextView>(R.id.search_src_text)
        autoComplete.setAdapter(arrayAdapter)

        autoComplete.onItemClickListener =
            AdapterView.OnItemClickListener { parent, view, position, id ->
                
                //get current item
                val item = arrayAdapter.getItem(position)
                val dialog = Dialog(this@MainActivity, R.style.CustomDialog)
                dialog.setContentView(R.layout.dialog_search)
                
                
                //layout fields
                val productName = dialog.findViewById<HelveticaNormalTextView>(R.id.tv_search_product_name)
                val productCategory = dialog.findViewById<HelveticaNormalTextView>(R.id.tv_search_product_category)
                val productPrice = dialog.findViewById<HelveticaNormalTextView>(R.id.tv_search_product_price)
                
                
                
                //get all products, get document where name = search result 
                productsRef.whereEqualTo("name", item!!.lowercase()).get()
                    .addOnSuccessListener { documents ->
                        
                        for(document in documents){
                            productName.text = document["name"].toString()
                            productCategory.text = document["category"].toString()
                            productPrice.text = document["price"].toString()

                            Toast.makeText(this, "Search Complete.", Toast.LENGTH_SHORT).show()
                            
                        }

                    }
                    .addOnFailureListener { 
                        
                    }

                //update item dialog
                dialog.findViewById<HelveticaNormalTextView>(R.id.tv_update).setOnClickListener {
                    //open dialog
                    val updateDialog = Dialog(this, R.style.CustomDialog)
                    updateDialog.setContentView(R.layout.dialog_update)
                    updateDialog.setCancelable(false)

                    //populate spinner
                    val spinnerUpdate = updateDialog.findViewById<Spinner>(R.id.sp_update)
                    ArrayAdapter.createFromResource(
                        this, R.array.products_category,
                        R.layout.support_simple_spinner_dropdown_item
                    ).also { adapter ->
                        adapter.setDropDownViewResource(R.layout.support_simple_spinner_dropdown_item)


                        spinnerUpdate.adapter = adapter

                        //set text color of selected text
                        spinnerUpdate.onItemSelectedListener = object :


                            AdapterView.OnItemSelectedListener{
                            override fun onItemSelected(
                                parent: AdapterView<*>?,
                                view: View?,
                                position: Int,
                                id: Long
                            ) {
                                val tv = spinnerUpdate.selectedView as TextView
                                tv.setTextColor(ContextCompat.getColor(applicationContext, R.color.black))
                            }

                            override fun onNothingSelected(parent: AdapterView<*>?) {

                            }

                        }
                    }


                    //cancel
                    val cancel = updateDialog.findViewById<HelveticaNormalTextView>(R.id.tv_cancel)
                    cancel.setOnClickListener {
                        updateDialog.dismiss()
                    }

                    //update item selected
                    //set default text of editTexts to current product data
                    val updateProductName = updateDialog.findViewById<TextInputEditText>(R.id.et_update_product_name)
                    val updateProductPrice = updateDialog.findViewById<TextInputEditText>(R.id.et_update_product_price)

                    updateProductName.setText(productName.text.toString())
                    updateProductPrice.setText(productPrice.text.toString())



                    updateDialog.show()


                }





                dialog.show()
            }


        searchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener {

            override fun onQueryTextSubmit(query: String?): Boolean {
//                searchView.clearFocus()
//                if (query != null) {
//
//                    productsRef.whereEqualTo("name", query.lowercase()).get()
//
//
//                        .addOnSuccessListener { documents ->
//                            for(document in documents){
//
//                                Toast.makeText(this@MainActivity, "Item found! : ${document.data}", Toast.LENGTH_SHORT).show()
//                            }
//
//
//                        }
//                        .addOnFailureListener { exception ->
//                            Toast.makeText(this@MainActivity, exception.message, Toast.LENGTH_SHORT).show()
//                        }
//                    //toggle keyboard off
////
//
//                }
                return false
            }

            override fun onQueryTextChange(newText: String?): Boolean {

                arrayAdapter.filter.filter(newText)

                return false
            }


        })
    }

    private fun replaceFragment(fragment : Fragment) {

        val transaction = supportFragmentManager.beginTransaction()
        transaction.replace(R.id.fragment_container, fragment)
        transaction.commit()
    }


}