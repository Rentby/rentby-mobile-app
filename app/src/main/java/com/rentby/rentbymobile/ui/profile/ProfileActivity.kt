package com.rentby.rentbymobile.ui.profile

import android.content.Intent
import android.os.Bundle
import android.view.MenuItem
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import com.bumptech.glide.Glide
import com.rentby.rentbymobile.R
import com.rentby.rentbymobile.data.pref.UserModel
import com.rentby.rentbymobile.databinding.ActivityProfileBinding
import com.rentby.rentbymobile.ui.ViewModelFactory
import com.rentby.rentbymobile.ui.login.LoginActivity

class ProfileActivity : AppCompatActivity() {

    private lateinit var binding: ActivityProfileBinding
    private val profileViewModel: ProfileViewModel by viewModels {
        ViewModelFactory.getInstance(this)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityProfileBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setupToolbar()
        observeViewModel()
    }

    private fun setupToolbar() {
        setSupportActionBar(binding.toolbar)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar?.title = "Profile"
        supportActionBar?.setHomeAsUpIndicator(R.drawable.ic_arrow_left)

        binding.toolbar.setNavigationOnClickListener {
            onBackPressed()
        }
    }

    private fun observeViewModel() {
        profileViewModel.getSession().observe(this, Observer { user: UserModel? ->
            user?.let {
                updateUI(it)
            } ?: navigateToLogin()
        })
    }

    private fun updateUI(user: UserModel) {
        binding.username.text = user.name
        binding.email.setText(user.email)
        binding.phoneNumber.setText(user.phoneNumber)
        binding.address.setText(user.address)
    }

    private fun navigateToLogin() {
        val intent = Intent(this, LoginActivity::class.java)
        intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
        startActivity(intent)
        finish()
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            android.R.id.home -> {
                onBackPressed()
                true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }
}
