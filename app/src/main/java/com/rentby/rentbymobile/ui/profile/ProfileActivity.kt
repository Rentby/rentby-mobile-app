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
import androidx.core.view.isVisible
import androidx.lifecycle.Observer
import com.bumptech.glide.Glide
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
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
                signOut()
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
        profileViewModel.message.observe(this) { message ->
            message?.let {
                Toast.makeText(this, it, Toast.LENGTH_SHORT).show()

                if (it != "") {
                    profileViewModel.clearMessage()
                }
            }
        }

        profileViewModel.getSession().observe(this) { user ->
            user?.let {
                originalUser = it
                updateUI(it)
            } ?: navigateToLogin()
        }

        profileViewModel.isLoading.observe(this) {
            if (it) {
                binding.progressBar2.isVisible = true
            } else {
                binding.progressBar2.isVisible = false
            }
        }
    }

    private fun isValidIndonesianPhoneNumber(phoneNumber: String): Boolean {
        val regex = Regex("^08[0-9]{8,10}\$")
        return phoneNumber.matches(regex)
    }

    private fun updateUI(user: UserModel) {
        binding.email.text = user.email
        binding.username.setText(user.name)
        binding.phoneNumber.setText(user.phoneNumber)
        binding.address.setText(user.address)

        photoUrl = Firebase.auth.currentUser?.photoUrl.toString()

        Glide.with(this)
            .load(photoUrl)
            .circleCrop()
            .placeholder(R.color.gray_200) // Placeholder image
            .error(R.color.gray_200) // Error image if loading fails
            .into(binding.imageprofile)
    }

    private fun navigateToLogin() {
        val intent = Intent(this, LoginActivity::class.java)
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
        if (name.isNotEmpty() && address.isNotEmpty() && phoneNumber.isNotEmpty()) {
            if (isValidIndonesianPhoneNumber(phoneNumber)) {
                profileViewModel.updateProfile(name, address, phoneNumber)
            } else {
                Toast.makeText(this, "Nomor telepon tidak valid!", Toast.LENGTH_SHORT).show()
            }
        } else {
            Toast.makeText(this, "Harap Lengkapi Informasi Anda!", Toast.LENGTH_SHORT).show()
        }

    }

    private fun loadUserProfilePhoto() {
        Firebase.auth.currentUser?.photoUrl?.let { uri ->
            Glide.with(this)
                .load(uri)
                .circleCrop()
                .placeholder(R.color.gray_200) // Placeholder image
                .error(R.color.gray_200) // Error image if loading fails
                .into(binding.imageprofile)
        }
    }

    private fun signOut() {
        Firebase.auth.signOut()
        profileViewModel.logout()
        navigateToLogin()
    }
}
