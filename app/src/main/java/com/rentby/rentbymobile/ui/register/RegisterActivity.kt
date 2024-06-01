package com.rentby.rentbymobile.ui.register

import android.os.Bundle
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.lifecycle.ViewModelProvider
import com.rentby.rentbymobile.R
import com.rentby.rentbymobile.databinding.ActivityRegisterBinding
import com.rentby.rentbymobile.ui.ViewModelFactory

class RegisterActivity : AppCompatActivity() {
    private lateinit var binding: ActivityRegisterBinding
    private lateinit var viewModel: RegisterViewModel
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
                viewModel.register(this, name, address, phoneNumber)
            } else {
                Toast.makeText(this, "Harap Lengkapi Informasi Anda!", Toast.LENGTH_SHORT).show()
            }
        }
    }
}