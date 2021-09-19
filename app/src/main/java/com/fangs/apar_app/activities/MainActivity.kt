package com.fangs.apar_app.activities

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.GravityCompat
import androidx.drawerlayout.widget.DrawerLayout
import androidx.fragment.app.Fragment
import com.fangs.apar_app.R
import com.fangs.apar_app.fragments.PurchaseFragment
import com.google.android.material.appbar.MaterialToolbar

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)


        val toolbar = findViewById<MaterialToolbar>(R.id.nav_drawer)
        val drawer = findViewById<DrawerLayout>(R.id.drawer)
        toolbar.setNavigationOnClickListener {
            drawer.openDrawer(GravityCompat.START)
        }


        //bottom navigation

        val purchaseFragment = PurchaseFragment()
        replaceFragment(purchaseFragment)

    }

    private fun replaceFragment(fragment : Fragment) {

        val transaction = supportFragmentManager.beginTransaction()
        transaction.replace(R.id.fragment_container, fragment)
        transaction.commit()

    }
}