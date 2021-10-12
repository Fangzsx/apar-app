package com.fangs.apar_app.activities

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.AutoCompleteTextView
import android.widget.Toast
import androidx.appcompat.widget.SearchView
import androidx.core.view.GravityCompat
import androidx.fragment.app.Fragment
import com.fangs.apar_app.R
import com.fangs.apar_app.databinding.ActivityMainBinding
import com.fangs.apar_app.fragments.PurchaseFragment
import com.fangs.apar_app.fragments.ViewOrderFragment
import com.google.android.material.navigation.NavigationView
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

        val arrayAdapter = ArrayAdapter(this, android.R.layout.simple_dropdown_item_1line, productList)
        val autoComplete = searchView.findViewById<AutoCompleteTextView>(R.id.search_src_text)
        autoComplete.setAdapter(arrayAdapter)

        autoComplete.onItemClickListener = object : AdapterView.OnItemClickListener{
            override fun onItemClick(
                parent: AdapterView<*>?,
                view: View?,
                position: Int,
                id: Long
            ) {
                Toast.makeText(this@MainActivity, arrayAdapter.getItem(position), Toast.LENGTH_SHORT).show()
            }

        }


        searchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener {

            override fun onQueryTextSubmit(query: String?): Boolean {
                searchView.clearFocus()
                if (query != null) {

                    productsRef.whereEqualTo("name", query.lowercase()).get()


                        .addOnSuccessListener { documents ->
                            for(document in documents){

                                Toast.makeText(this@MainActivity, "Item found! : ${document.data}", Toast.LENGTH_SHORT).show()
                            }


                        }
                        .addOnFailureListener { exception ->
                            Toast.makeText(this@MainActivity, exception.message, Toast.LENGTH_SHORT).show()
                        }
                    //toggle keyboard off
//                    hideKeyboard(currentFocus ?: View(this@MainActivity))

                }
                return false
            }

            override fun onQueryTextChange(newText: String?): Boolean {

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