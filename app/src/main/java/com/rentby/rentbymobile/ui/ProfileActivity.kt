package com.rentby.rentbymobile.ui

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import com.rentby.rentbymobile.data.pref.UserPreference
import com.rentby.rentbymobile.data.repository.UserRepository
import com.rentby.rentbymobile.databinding.ActivityProfileBinding
import kotlinx.coroutines.launch
import com.rentby.rentbymobile.data.pref.dataStore
import com.rentby.rentbymobile.data.retrofit.ApiConfig

class ProfileActivity : AppCompatActivity() {

    private lateinit var binding: ActivityProfileBinding
    private lateinit var userRepository: UserRepository
    private lateinit var userPreference: UserPreference

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityProfileBinding.inflate(layoutInflater)
        setContentView(binding.root)

        userPreference = UserPreference.getInstance(this.dataStore)
        userRepository = UserRepository.getInstance(
            userPreference,
            ApiConfig.getApiService()
        )

        setupViews()
        loadUserData()
    }

    private fun setupViews() {
        setSupportActionBar(binding.toolbar)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar?.setDisplayShowTitleEnabled(false)
        binding.toolbar.setNavigationOnClickListener {
            onBackPressed()
        }
    }

    private fun loadUserData() {
        lifecycleScope.launch {
            try {
                userRepository.getSession().collect { user ->
                    binding.username.text = user.name
                    binding.email.setText(user.email)
                    binding.phoneNumber.setText(user.phoneNumber)
                    binding.address.setText(user.address)
                }
            } catch (e: Exception) {
                // Handle error if necessary
            }
        }
    }
}