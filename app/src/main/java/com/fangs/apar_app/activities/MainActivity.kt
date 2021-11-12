package com.fangs.apar_app.activities

import android.app.Dialog
import android.content.Intent
import android.os.Bundle
import android.text.TextUtils
import android.util.Log
import android.view.View
import android.view.WindowManager
import android.widget.*
import androidx.core.content.ContextCompat
import androidx.core.view.GravityCompat
import androidx.fragment.app.Fragment
import com.fangs.apar_app.R
import com.fangs.apar_app.databinding.ActivityMainBinding
import com.fangs.apar_app.fragments.PurchaseFragment
import com.fangs.apar_app.fragments.ViewOrderFragment
import com.fangs.apar_app.utils.HelveticaBoldTextView
import com.fangs.apar_app.utils.HelveticaNormalTextView
import com.google.android.material.navigation.NavigationView
import com.google.android.material.textfield.TextInputEditText
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.DocumentSnapshot
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await

class MainActivity : BaseActivity() {

    private lateinit var binding : ActivityMainBinding
    private var suggestions = mutableListOf<String>()
    private val productsCollectionRef = Firebase.firestore.collection("products")

    override fun onCreate(savedInstanceState: Bundle?) {
        supportActionBar?.hide()
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
            var productID : String? = null

            productsCollectionRef.whereEqualTo("name", currentProduct!!.lowercase()).get()
                .addOnSuccessListener { documents ->
                    for (document in documents) {
                        dialogProdName.text = document["name"].toString()
                        dialogProductCategory.text = document["category"].toString()
                        dialogProductPrice.text = document["price"].toString()
                        productID = document.id
                    }
                }
                .addOnFailureListener { exception ->
                    Log.e("manage toolbar", exception.message.toString())
                }

            //update item
            val tvUpdate = showItemDialog.findViewById<HelveticaNormalTextView>(R.id.tv_update)
            tvUpdate.setOnClickListener {
                //close search dialog
                showItemDialog.dismiss()

                val updateDialog = Dialog(this, R.style.CustomDialog)
                updateDialog.setContentView(R.layout.dialog_update)
                updateDialog.setCancelable(false)

                //spinner settings
                val spinner = updateDialog.findViewById<Spinner>(R.id.sp_update)
                populateSpinner(spinner, dialogProductCategory.text.toString())

                //set default values of update dialog fields == the current product's properties(name, category, price)
                val etProductName = updateDialog.findViewById<TextInputEditText>(R.id.et_update_product_name)
                val etProductPrice = updateDialog.findViewById<TextInputEditText>(R.id.et_update_product_price)

                etProductName.setText(dialogProdName.text.toString())
                etProductPrice.setText(dialogProductPrice.text.toString())

                //update item
                val tvSubmitToFirestore = updateDialog.findViewById<HelveticaBoldTextView>(R.id.tv_update_item1)

                //TODO allow user to update an item with the same name provided that it should only exist once
                // and must be unique

                tvSubmitToFirestore.setOnClickListener {



                    //TODO : allow user to reuse the product name when updating

                }



                //dismiss/cancel update dialog
                val tvCancelUpdate = updateDialog.findViewById<HelveticaNormalTextView>(R.id.tv_cancel_update)
                tvCancelUpdate.setOnClickListener {
                    updateDialog.dismiss()
                }
                updateDialog.show()
            }
            showItemDialog.show()
        }
    }


    private suspend fun validateProduct(name : String, category : String, price : Double) : Boolean{

        return when{
            TextUtils.isEmpty(name) -> {
                showErrorSnackBar(binding.root, "Product name cannot be empty.", true)
                false
            }
            category.uppercase() == "PLEASE SELECT FROM THE FF--" -> {
                showErrorSnackBar(binding.root, "Select a valid category.", true)
                false
            }
            price <= 0 -> {
                showErrorSnackBar(binding.root, "Price cannot be less than or equal to 0.", true)
                false
            }

            isExisting(name) -> {
                showErrorSnackBar(binding.root, "A product with the same name already exist.", true)
                false
            }


            else -> {
                true
            }


        }



    }

    private suspend fun isExisting(productName : String) : Boolean{

        val result = productsCollectionRef.whereEqualTo("name", productName).limit(1).get().await()
        val documents = result.documents
        return documents.count() == 1


    }


    private fun populateSpinner(spinner : Spinner, defaultValue : String) {
        ArrayAdapter.createFromResource(
            this, R.array.products_category,
            R.layout.support_simple_spinner_dropdown_item
        ).also { adapter ->
            adapter.setDropDownViewResource(R.layout.support_simple_spinner_dropdown_item)
            spinner.adapter = adapter
            val defaultIndex = adapter.getPosition(defaultValue.uppercase())
            spinner.setSelection(defaultIndex)

            //set text color of selected text
            spinner.onItemSelectedListener = object :
                AdapterView.OnItemSelectedListener{
                override fun onItemSelected(
                    parent: AdapterView<*>?,
                    view: View?,
                    position: Int,
                    id: Long
                ) {

                    val tv = spinner.selectedView as TextView
                    tv.setTextColor(ContextCompat.getColor(applicationContext, R.color.black))
                }
                override fun onNothingSelected(parent: AdapterView<*>?) {

                }
            }
        }
    }

    private fun replaceFragment(fragment : Fragment) {

        val transaction = supportFragmentManager.beginTransaction()
        transaction.replace(R.id.fragment_container, fragment)
        transaction.commit()
    }
}