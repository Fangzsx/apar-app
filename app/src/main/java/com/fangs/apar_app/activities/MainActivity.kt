package com.fangs.apar_app.activities

import android.app.SearchManager
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
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

class MainActivity : BaseActivity() {

    private lateinit var binding : ActivityMainBinding



    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        showErrorSnackBar(binding.root, "Welcome Jay!", false)


        binding.navDrawer.setNavigationOnClickListener {
            binding.drawer.openDrawer(GravityCompat.START)
        }



        //side navigation
        val sideNav = NavigationView.OnNavigationItemSelectedListener { item ->
            when(item.itemId){
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



        //bottom navigation

        val purchaseFragment = PurchaseFragment()
        val viewOrderFragment = ViewOrderFragment()

        replaceFragment(purchaseFragment)
        binding.bottomNavigation.setOnItemSelectedListener {
            when(it.itemId){
                R.id.btm_nav_purchase -> replaceFragment(purchaseFragment)
                R.id.btm_nav_view_orders ->replaceFragment(viewOrderFragment)


            }
            true
        }


        //sign out
        binding.tvSignOut.setOnClickListener {
            FirebaseAuth.getInstance().signOut()
            Intent(this, LoginActivity::class.java).also {
                startActivity(it)
                finish()
            }
        }





    }

    private fun replaceFragment(fragment : Fragment) {

        val transaction = supportFragmentManager.beginTransaction()
        transaction.replace(R.id.fragment_container, fragment)
        transaction.commit()
    }
    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.top_app_bar, menu)

        val searchItem: MenuItem? = menu?.findItem(R.id.top_nav_search)
        val searchManager = getSystemService(Context.SEARCH_SERVICE) as SearchManager
        val searchView: SearchView = searchItem?.actionView as SearchView

        searchView.setSearchableInfo(searchManager.getSearchableInfo(componentName))

        searchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener{
            override fun onQueryTextSubmit(query: String?): Boolean {
                Toast.makeText(this@MainActivity, "Looking for $query", Toast.LENGTH_SHORT).show()
                return true
            }

            override fun onQueryTextChange(newText: String?): Boolean {
                return true
            }

        })
        return super.onCreateOptionsMenu(menu)
    }


}