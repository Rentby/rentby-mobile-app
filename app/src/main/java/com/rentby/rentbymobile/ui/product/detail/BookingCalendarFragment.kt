package com.rentby.rentbymobile.ui.product.detail

import android.app.Dialog
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.util.Pair
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.fragment.app.FragmentManager
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.google.android.material.datepicker.CalendarConstraints
import com.google.android.material.datepicker.DateValidatorPointForward
import com.google.android.material.datepicker.MaterialDatePicker
import com.rentby.rentbymobile.databinding.BookingCalendarFragmentBinding
import com.rentby.rentbymobile.ui.order.OrderActivity
import java.text.NumberFormat
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Locale

class BookingCalendarFragment : BottomSheetDialogFragment() {
    private lateinit var binding: BookingCalendarFragmentBinding
    private val viewModel: BookingCalendarViewModel by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = BookingCalendarFragmentBinding.inflate(inflater, container, false)

        val productId = arguments?.getString(PRODUCT_ID) ?: ""
        viewModel.getProduct(productId)

        setupObservers()

        binding.pickDateLayout.setOnClickListener {
            showDateRangePicker(parentFragmentManager)
        }

        binding.orderButton.setOnClickListener {
            viewModel.makeBooking(requireContext(), productId)
        }

        return binding.root
    }

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        // Used to show the bottom sheet dialog
        val dialog = super.onCreateDialog(savedInstanceState) as BottomSheetDialog
        dialog.setOnShowListener {
            val bottomSheet = dialog.findViewById<View>(com.google.android.material.R.id.design_bottom_sheet)
            bottomSheet?.let {
                val behavior = BottomSheetBehavior.from(it)
                behavior.state = BottomSheetBehavior.STATE_EXPANDED
            }
        }
        return dialog
    }

    private fun showDateRangePicker(fragmentManager: FragmentManager) {
        val tomorrow = viewModel.tomorrow()

        val constraintsBuilder = CalendarConstraints.Builder()
            .setStart(tomorrow)
            .setValidator(DateValidatorPointForward.from(tomorrow))

        val dateRangePicker = MaterialDatePicker.Builder.dateRangePicker()
            .setTitleText("Select dates")
            .setSelection(Pair(viewModel.rentStart.value, viewModel.rentEnd.value))
            .setCalendarConstraints(constraintsBuilder.build())
            .build()

        dateRangePicker.show(fragmentManager, "date_range_picker")

        dateRangePicker.addOnPositiveButtonClickListener { selection ->
            viewModel.setRentDates(selection.first, selection.second)
        }
    }

    private fun setupObservers() {
        viewModel.rentStart.observe(viewLifecycleOwner) {
            updateUI()
        }

        viewModel.rentEnd.observe(viewLifecycleOwner) {
            updateUI()
        }

        viewModel.duration.observe(viewLifecycleOwner) {
            updateUI()
        }

        viewModel.rentTotal.observe(viewLifecycleOwner) {
            updateUI()
        }

        viewModel.product.observe(viewLifecycleOwner) { product ->
            product?.let {
                binding.tvRentPrice.text = NumberFormat.getNumberInstance(Locale("id", "ID")).format(it.price.toInt())
            }
        }
    }

    private fun updateUI() {
        val formattedStart = viewModel.rentStart.value?.let { SimpleDateFormat("dd MMM", Locale.getDefault()).format(it) } ?: ""
        val formattedEnd = viewModel.rentEnd.value?.let { SimpleDateFormat("dd MMM", Locale.getDefault()).format(it) } ?: ""
        val selectedDates = "$formattedStart - $formattedEnd"

        binding.selectedDates.text = selectedDates
        binding.tvRentDuration.text = "${viewModel.duration.value ?: 0} Hari"
        binding.tvRentTotalDay.text = "${viewModel.duration.value} x "
        binding.tvRentTotal.text = " = Rp" + NumberFormat.getNumberInstance(Locale("id", "ID")).format(viewModel.rentTotal.value ?: 0)
    }

    companion object {
        const val TAG = "ModalBottomSheetDialog"
        private const val PRODUCT_ID = "product_id"

        fun newInstance(productId: String): BookingCalendarFragment {
            val fragment = BookingCalendarFragment()
            val args = Bundle().apply {
                putString(PRODUCT_ID, productId)
            }
            fragment.arguments = args
            return fragment
        }
    }
}
