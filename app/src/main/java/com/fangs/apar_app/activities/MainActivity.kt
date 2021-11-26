package com.fangs.apar_app.activities

import android.annotation.SuppressLint
import android.app.Dialog
import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.text.TextUtils
import android.util.Log
import android.view.View
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
import com.fangs.apar_app.utils.LoadingDialog
import com.google.android.material.navigation.NavigationView
import com.google.android.material.textfield.TextInputEditText
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.DocumentSnapshot
import com.google.firebase.firestore.ListenerRegistration
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await

class MainActivity : BaseActivity() {

    private lateinit var binding : ActivityMainBinding
    private var suggestions = mutableListOf<String>()
    private val productsCollectionRef = Firebase.firestore.collection("products")
    private val usersRef = Firebase.firestore.collection("users")
    private lateinit var userID : String
    private lateinit var listenerRegistration: ListenerRegistration

    override fun onCreate(savedInstanceState: Bundle?) {
        supportActionBar?.hide()
        super.onCreate(savedInstanceState)

        val loadingDialog = LoadingDialog(this@MainActivity)
        loadingDialog.startLoading()
        Handler().postDelayed({
            loadingDialog.dismiss()
        }, 1500) // 1000 is the delayed time in milliseconds.


        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        getRealTimeUpdates()


        //get user ID
        userID = FirebaseAuth.getInstance().currentUser!!.uid

        manageToolbar()
        manageSideNavigation()
        manageBottomNavigation()

        //open drawer
        binding.toolbar.navDrawerToolbar.setNavigationOnClickListener {
            binding.drawer.openDrawer(GravityCompat.START)
        }


        //sign out
        binding.tvSignOut.setOnClickListener {

            listenerRegistration.remove()
            FirebaseAuth.getInstance().signOut()

            Intent(this, LoginActivity::class.java).also {
                startActivity(it)
                finish()
            }
        }
    }

    private suspend fun getUserData(): DocumentSnapshot {
        return usersRef.document(userID).get().await()


    }

    private fun getRealTimeUpdates() {
        listenerRegistration = productsCollectionRef.addSnapshotListener { snapshot, error ->
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

    @SuppressLint("SetTextI18n")
    private fun manageSideNavigation() {

        val headerView = binding.sideNavBar.getHeaderView(0)
        val tvAPARname = headerView.findViewById<HelveticaBoldTextView>(R.id.tv_apar_name)
        val tvAPARno = headerView.findViewById<HelveticaNormalTextView>(R.id.tv_apar_no)
        val tvStoreCode = headerView.findViewById<HelveticaNormalTextView>(R.id.tv_store_code)
        val tvRegion = headerView.findViewById<HelveticaNormalTextView>(R.id.tv_region)
        val tvClusterNo = headerView.findViewById<HelveticaNormalTextView>(R.id.tv_cluster_no)

        GlobalScope.launch {
            val userDoc = getUserData()

            Log.i("doc", userDoc.data.toString())
            runOnUiThread(Runnable{

                tvAPARname.text = "${userDoc["lastname"]}, ${userDoc["firstname"]} ${userDoc["middlename"]}".uppercase()
                tvAPARno.text = userDoc["apar no"].toString().uppercase()
                tvRegion.text = userDoc["region"].toString().uppercase()
                tvStoreCode.text = userDoc["store code"].toString().uppercase()
                tvClusterNo.text = userDoc["cluster"].toString().uppercase()
            })
        }


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
                R.id.side_bar_update_item -> {
                    Intent(this, UpdateItemActivity::class.java).also {
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

    private fun manageToolbar(){
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

            //delete item
            val tvDelete = showItemDialog.findViewById<HelveticaBoldTextView>(R.id.tv_delete)
            tvDelete.setOnClickListener {
                showItemDialog.dismiss()
                val deleteDialog = Dialog(this, R.style.CustomDialog)
                deleteDialog.setCancelable(false)
                deleteDialog.setContentView(R.layout.dialog_delete)

                //product name to be deleted
                val tvTobeDeletedProdName = deleteDialog.findViewById<HelveticaBoldTextView>(R.id.tv_delete_product_name)
                tvTobeDeletedProdName.text = dialogProdName.text.toString()

                //cancel delete
                val tvCancel = deleteDialog.findViewById<HelveticaNormalTextView>(R.id.tv_delete_cancel)
                tvCancel.setOnClickListener {
                    deleteDialog.dismiss()
                    searchView.onActionViewCollapsed()
                }

                //delete proper
                val tvConfirmDelete = deleteDialog.findViewById<HelveticaBoldTextView>(R.id.tv_delete_confirm)
                tvConfirmDelete.setOnClickListener {
                    Toast.makeText(this, "Product with name: ${dialogProdName.text.toString().uppercase()} was successfully deleted.", Toast.LENGTH_LONG).show()
                    productsCollectionRef.document(productID!!).delete()
                    deleteDialog.dismiss()
                    finish()
                    startActivity(intent)
                    overridePendingTransition(0, 0)
                }

                deleteDialog.show()
            }



            //update item
            val tvUpdate = showItemDialog.findViewById<HelveticaNormalTextView>(R.id.tv_update)
            tvUpdate.setOnClickListener {
                //close search dialog
                showItemDialog.dismiss()
                suggestions.remove(dialogProdName.text.toString())

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
                val tvUpdateDoc = updateDialog.findViewById<HelveticaBoldTextView>(R.id.tv_update_item)

                //allow user to update an item with the same name provided that it should only exist once
                // and must be unique

                tvUpdateDoc.setOnClickListener {
                    //allow user to reuse the product name when updating
                    val newProductName = etProductName.text.toString()
                    val newProductCategory = spinner.selectedItem.toString().uppercase()
                    val newProductPrice = etProductPrice.text.toString().toDouble()
                    
                    if(validateProduct(newProductName, newProductCategory, newProductPrice)){
                        //update the doc in firebase
                        productsCollectionRef.document(productID!!).update(
                            mapOf(
                                "name" to newProductName,
                                "category" to newProductCategory.lowercase(),
                                "price" to newProductPrice.toString()
                            )
                        )
                        Toast.makeText(this, "Product updated!", Toast.LENGTH_SHORT).show()

                        updateDialog.dismiss()
                        finish()
                        startActivity(intent)
                        overridePendingTransition(0, 0)

                    }
                }
                //dismiss/cancel update dialog
                val tvCancelUpdate = updateDialog.findViewById<HelveticaNormalTextView>(R.id.tv_cancel_update)
                tvCancelUpdate.setOnClickListener {
                    searchView.onActionViewCollapsed()
                    updateDialog.dismiss()
                }
                updateDialog.show()
            }
            showItemDialog.show()
        }
    }

    private fun isExisting(name : String) : Boolean{
        val productNameList = suggestions.toMutableList()
        return productNameList.contains(name)
    }

    private fun validateProduct(name : String, category : String, price : Double) : Boolean{
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
                showErrorSnackBar(binding.root, "Product with the same name already exist.", true)
                false
            }
            else -> {
                true
            }
        }
    }

    private fun replaceFragment(fragment : Fragment) {
        val transaction = supportFragmentManager.beginTransaction()
        transaction.replace(R.id.fragment_container, fragment)
        transaction.commit()
    }
}