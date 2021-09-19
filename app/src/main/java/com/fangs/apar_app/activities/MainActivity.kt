package com.fangs.apar_app.activities

import android.os.Bundle
import android.view.LayoutInflater
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.GravityCompat
import androidx.drawerlayout.widget.DrawerLayout
import androidx.fragment.app.Fragment
import com.fangs.apar_app.R
import com.fangs.apar_app.databinding.ActivityMainBinding
import com.fangs.apar_app.fragments.PurchaseFragment
import com.fangs.apar_app.fragments.ViewOrderFragment
import com.google.android.material.appbar.MaterialToolbar

class MainActivity : AppCompatActivity() {

    private lateinit var binding : ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)

        setContentView(binding.root)


        val toolbar = findViewById<MaterialToolbar>(R.id.nav_drawer)
        val drawer = findViewById<DrawerLayout>(R.id.drawer)
        toolbar.setNavigationOnClickListener {
            drawer.openDrawer(GravityCompat.START)
        }


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




    }

    private fun replaceFragment(fragment : Fragment) {

        if(fragment!= null){
            val transaction = supportFragmentManager.beginTransaction()
            transaction.replace(R.id.fragment_container, fragment)
            transaction.commit()
        }


    }
}