package com.rentby.rentbymobile.ui.product.detail

import android.app.Dialog
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.FragmentManager
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.google.android.material.datepicker.MaterialDatePicker
import com.rentby.rentbymobile.databinding.BookingCalendarFragmentBinding
import androidx.core.util.Pair
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Locale

class BookingCalendarFragment : BottomSheetDialogFragment() {
    private lateinit var binding: BookingCalendarFragmentBinding

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = BookingCalendarFragmentBinding.inflate(inflater, container, false)

        binding.pickDateLayout.setOnClickListener {
            showDateRangePicker(parentFragmentManager)
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
        val today = MaterialDatePicker.todayInUtcMilliseconds()

        // Calculate tomorrow's date in milliseconds
        val calendar = Calendar.getInstance().apply {
            timeInMillis = today
            add(Calendar.DAY_OF_YEAR, 1)
        }
        val tomorrow = calendar.timeInMillis

        val dateRangePicker = MaterialDatePicker.Builder.dateRangePicker()
            .setTitleText("Select dates")
            .setSelection(
                Pair(today, tomorrow)
            )
            .build()

        dateRangePicker.show(fragmentManager, "date_range_picker")

        dateRangePicker.addOnPositiveButtonClickListener { selection ->
            val startDate = selection.first
            val endDate = selection.second
            // Format the selected dates
            val dateFormat = SimpleDateFormat("dd MMM", Locale.getDefault())
            val startDateFormatted = dateFormat.format(startDate)
            val endDateFormatted = dateFormat.format(endDate)
            // Display the formatted date range
            binding.selectedDates.text = "$startDateFormatted - $endDateFormatted"
        }
    }

    companion object {
        const val TAG = "ModalBottomSheetDialog"
    }
}