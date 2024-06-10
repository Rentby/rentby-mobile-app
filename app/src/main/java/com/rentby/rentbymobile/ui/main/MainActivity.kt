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
        setupRecyclerView()
    }

    private fun setupRecyclerView() {
        val productAdapter = ProductAdapter(this, ProductList.getProducts())
        binding.recyclerView.adapter = productAdapter
        binding.recyclerView.layoutManager = GridLayoutManager(this, 2)
        binding.recyclerView.addItemDecoration(GridSpacingItemDecoration(2, 16, true))
    }

    private fun setupNavigation() {
        val bottomNavigationItemView = binding.bottomNavigation
        bottomNavigationItemView.setOnItemSelectedListener  { item ->
            when(item.itemId) {
                R.id.home -> {
                    // Respond to navigation item 1 click
                    true
                }
                R.id.bookmark -> {
                    // Respond to navigation item 2 click
                    true
                }
                R.id.booked -> {
                    Log.d("TestNavi", "Clicked")
                    signOut()
                    true
                }
                else -> false
            }
        }
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
                startActivity(Intent(this, RegisterActivity::class.java))
                finish()
            }
        }

        firebaseUser.photoUrl?.let { photoUrl ->
            Glide.with(this)
                .load(photoUrl)
                .circleCrop()
                .placeholder(R.drawable.ic_profile) // Optional: placeholder image
                .error(R.drawable.ic_profile) // Optional: error image
                .into(binding.userPhoto) // Ensure the ID is correct
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