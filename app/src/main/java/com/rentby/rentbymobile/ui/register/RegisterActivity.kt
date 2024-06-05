package com.rentby.rentbymobile.ui.register

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.credentials.ClearCredentialStateRequest
import androidx.credentials.CredentialManager
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import com.google.firebase.Firebase
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.auth
import com.rentby.rentbymobile.R
import com.rentby.rentbymobile.databinding.ActivityRegisterBinding
import com.rentby.rentbymobile.ui.ViewModelFactory
import com.rentby.rentbymobile.ui.login.LoginActivity
import com.rentby.rentbymobile.ui.main.MainActivity
import kotlinx.coroutines.launch

class RegisterActivity : AppCompatActivity() {
    private lateinit var binding: ActivityRegisterBinding
    private lateinit var viewModel: RegisterViewModel
    private lateinit var auth: FirebaseAuth
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding =ActivityRegisterBinding.inflate(layoutInflater)
        enableEdgeToEdge()
        setContentView(binding.root)
        ViewCompat.setOnApplyWindowInsetsListener(binding.root) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        auth = Firebase.auth

        val factory = ViewModelFactory.getInstance(this)
        viewModel =ViewModelProvider(this, factory).get(RegisterViewModel::class.java)

        setupAction()
    }

    private fun setupAction() {
        binding.saveButton.setOnClickListener {
            val name = binding.tfNama.editText?.text.toString()
            val address = binding.tfAddress.editText?.text.toString()
            val phoneNumber = binding.tfTelephone.editText?.text.toString()
            if (name.isNotEmpty() && address.isNotEmpty() && phoneNumber.isNotEmpty()) {
                if (isValidIndonesianPhoneNumber(phoneNumber)) {
                    viewModel.register(this, name, address, phoneNumber) {
                        // This block will be executed on successful registration
                        navigateToMainActivity()
                    }
                } else {
                    Toast.makeText(this, "Nomor telepon tidak valid!", Toast.LENGTH_SHORT).show()
                }
            } else {
                Toast.makeText(this, "Harap Lengkapi Informasi Anda!", Toast.LENGTH_SHORT).show()
            }
        }

        binding.backButton.setOnClickListener {
            signOut()
        }

        viewModel.loading.observe(this) { loading ->
            if (loading) {
                binding.progressBar.visibility = View.VISIBLE
            } else {
                binding.progressBar.visibility = View.GONE
            }
        }
    }

    private fun navigateToMainActivity() {
        val intent = Intent(this, MainActivity::class.java)
        startActivity(intent)
        finish() // Close the current activity
    }

    private fun isValidIndonesianPhoneNumber(phoneNumber: String): Boolean {
        val regex = Regex("^08[0-9]{8,10}\$")
        return phoneNumber.matches(regex)
    }

    private fun signOut() {
        lifecycleScope.launch {
            val credentialManager = CredentialManager.create(this@RegisterActivity)
            auth.signOut()
            viewModel.logout()
            credentialManager.clearCredentialState(ClearCredentialStateRequest())
            startActivity(Intent(this@RegisterActivity, LoginActivity::class.java))
            finish()
        }
    }
}