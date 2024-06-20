package com.rentby.rentbymobile.ui.main

import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.credentials.ClearCredentialStateRequest
import androidx.credentials.CredentialManager
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.GridLayoutManager
import com.bumptech.glide.Glide
import com.google.firebase.Firebase
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.auth
import com.rentby.rentbymobile.R
import com.rentby.rentbymobile.data.mock.ProductList
import com.rentby.rentbymobile.databinding.ActivityMainBinding
import com.rentby.rentbymobile.ui.login.LoginActivity
import com.rentby.rentbymobile.ui.ViewModelFactory
import com.rentby.rentbymobile.ui.adapter.ProductAdapter
import com.rentby.rentbymobile.ui.register.RegisterActivity
import com.rentby.rentbymobile.utils.GridSpacingItemDecoration
import kotlinx.coroutines.launch

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    private lateinit var auth: FirebaseAuth

    private val viewModel by viewModels<MainViewModel> {
        ViewModelFactory.getInstance(this)
    }

    // Store references to the fragments
    private val homeFragment by lazy { HomeFragment.newInstance(auth.currentUser?.displayName, auth.currentUser?.photoUrl?.toString()) }
    val searchFragment by lazy { SearchFragment.newInstance() }
    private val bookedFragment by lazy { BookedFragment.newInstance(auth.currentUser?.displayName, auth.currentUser?.photoUrl?.toString()) }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        enableEdgeToEdge()
        setContentView(binding.root)
        ViewCompat.setOnApplyWindowInsetsListener(binding.root) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, 0)
            insets
        }

        setupAuth()
        setupNavigation()
        setupDefaultFragment(savedInstanceState)
    }

    private fun setupDefaultFragment(savedInstanceState: Bundle?){
        if (savedInstanceState == null) {
            auth.currentUser?.let { user ->
                loadFragment(homeFragment)
            }
        }
    }

    private fun loadFragment(fragment: Fragment) {
        supportFragmentManager.beginTransaction()
            .replace(R.id.fragmentContainerView, fragment)
            .commit()
    }

    fun showFragment(fragment: Fragment) {
        val fragmentTransaction = supportFragmentManager.beginTransaction()

        // Hide all other fragments
        supportFragmentManager.fragments.forEach { existingFragment ->
            fragmentTransaction.hide(existingFragment)
        }

        if (fragment.isAdded) {
            fragmentTransaction.show(fragment)
        } else {
            fragmentTransaction.add(R.id.fragmentContainerView, fragment)
        }

        fragmentTransaction.commit()
    }

    private fun setupNavigation() {
        val bottomNavigationItemView = binding.bottomNavigation
        bottomNavigationItemView.setOnItemSelectedListener  { item ->
            when(item.itemId) {
                R.id.home -> {
                    auth.currentUser?.let {
                        showFragment(homeFragment)
                    }
                    true
                }
                R.id.search -> {
                    lifecycleScope.launch {
                        showFragment(searchFragment)
                    }
                    true
                }
                R.id.booked -> {
                    auth.currentUser?.let {
                        showFragment(bookedFragment)
                    }
                    true
                }
                else -> false
            }
        }
    }

    fun updateSelectedNavigationItem(itemId: Int) {
        val bottomNavigation = binding.bottomNavigation
        bottomNavigation.selectedItemId = itemId
    }

    private fun setupAuth(){
        auth = Firebase.auth
        val firebaseUser = auth.currentUser

        if (firebaseUser == null) {
            // Not signed in, launch the Login activity
            startActivity(Intent(this, LoginActivity::class.java))
            finish()
            return
        }

        viewModel.getSession().observe(this) { user ->
            if (auth.currentUser != null && !user.isRegistered) {
                viewModel.isRegistered(Firebase.auth.currentUser?.email.toString()) { isRegistered ->
                    if (!isRegistered) {
                        startActivity(Intent(this, RegisterActivity::class.java))
                        finish()
                    }
                }
            }
        }
    }

    private fun signOut() {
        lifecycleScope.launch {
            val credentialManager = CredentialManager.create(this@MainActivity)
            auth.signOut()
            viewModel.logout()
            credentialManager.clearCredentialState(ClearCredentialStateRequest())
            startActivity(Intent(this@MainActivity, LoginActivity::class.java))
            finish()
        }
    }
}