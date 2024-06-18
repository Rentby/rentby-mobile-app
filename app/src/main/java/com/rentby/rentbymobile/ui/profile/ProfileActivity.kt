package com.rentby.rentbymobile.ui.profile

import android.content.Intent
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.Menu
import android.view.MenuItem
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import com.bumptech.glide.Glide
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.firebase.Firebase
import com.google.firebase.auth.auth
import com.rentby.rentbymobile.R
import com.rentby.rentbymobile.data.pref.UserModel
import com.rentby.rentbymobile.databinding.ActivityProfileBinding
import com.rentby.rentbymobile.ui.ViewModelFactory
import com.rentby.rentbymobile.ui.login.LoginActivity
import com.rentby.rentbymobile.ui.register.RegisterActivity

class ProfileActivity : AppCompatActivity() {

    private lateinit var binding: ActivityProfileBinding
    private val profileViewModel: ProfileViewModel by viewModels {
        ViewModelFactory.getInstance(this)
    }

    private var originalUser: UserModel? = null
    private var isProfileChanged = false
    private var photoUrl: String? = null

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        menuInflater.inflate(R.menu.menu_profile, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            R.id.action_logout -> {
                profileViewModel.logout()
                true
            }
            android.R.id.home -> {
                onBackPressed()
                true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityProfileBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setupToolbar()
        observeViewModel()
        setupTextChangeListeners()
        setupSaveButton()

        // Load user profile photo if available
        loadUserProfilePhoto()
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
        profileViewModel.message.observe(this, Observer { message ->
            if (message.isNotBlank()) {
                Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
                profileViewModel.clearMessage()

                // If message indicates logout, navigate to login
                if (message == "Logged out successfully") {
                    navigateToLogin()
                }
            }
        })

        profileViewModel.getSession().observe(this, Observer { user ->
            user?.let {
                originalUser = it
                updateUI(it)
            } ?: navigateToLogin()
        })
    }

    private fun updateUI(user: UserModel) {
        binding.email.text = user.email
        binding.username.setText(user.name)
        binding.phoneNumber.setText(user.phoneNumber)
        binding.address.setText(user.address)

        // Load user profile photo if available
        photoUrl?.let { url ->
            Glide.with(this)
                .load(url)
                .circleCrop()
                .placeholder(R.drawable.ic_profile)
                .error(R.drawable.ic_profile)
                .into(binding.imageprofile)
        }
    }

    private fun navigateToLogin() {
        val intent = Intent(this, RegisterActivity::class.java)
        intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
        startActivity(intent)
        finish()
    }

    private fun setupTextChangeListeners() {
        binding.username.addTextChangedListener(profileTextWatcher)
        binding.phoneNumber.addTextChangedListener(profileTextWatcher)
        binding.address.addTextChangedListener(profileTextWatcher)

        binding.buttonSave.isEnabled = false
    }

    private val profileTextWatcher = object : TextWatcher {
        override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}

        override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
            if (!isProfileChanged) {
                isProfileChanged = true
                binding.buttonSave.isEnabled = true
            }
        }

        override fun afterTextChanged(s: Editable?) {}
    }

    private fun setupSaveButton() {
        binding.buttonSave.isEnabled = false
        binding.buttonSave.setOnClickListener {
            saveProfile()
        }
    }

    private fun saveProfile() {
        val name = binding.username.text.toString()
        val address = binding.address.text.toString()
        val phoneNumber = binding.phoneNumber.text.toString()

        profileViewModel.updateProfile(name, address, phoneNumber)
    }

    private fun loadUserProfilePhoto() {
        photoUrl = Firebase.auth.currentUser?.photoUrl.toString()

        Glide.with(this)
            .load(photoUrl)
            .circleCrop()
            .placeholder(R.color.gray_200) // Placeholder image
            .error(R.color.gray_200) // Error image if loading fails
            .into(binding.imageprofile)
    }
}