package com.rentby.rentbymobile.ui.seller

import android.app.Dialog
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.rentby.rentbymobile.databinding.FragmentSellerDescriprionBinding
class SellerDescriptionFragment : BottomSheetDialogFragment() {
    private lateinit var binding: FragmentSellerDescriprionBinding

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Inflate the layout for this fragment
        binding = FragmentSellerDescriprionBinding.inflate(inflater, container, false)
        val sellerDescription = arguments?.getString(SELLER_DESCRIPTION) ?: ""

        binding.tvSellerDescription.text = sellerDescription

        return binding.root
    }

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
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

    companion object {
        const val TAG = "ModalBottomSheetDialog"
        private const val SELLER_DESCRIPTION = "seller_description"

        fun newInstance(sellerDescription: String): SellerDescriptionFragment {
            val fragment = SellerDescriptionFragment()
            val args = Bundle().apply {
                putString(SELLER_DESCRIPTION, sellerDescription)
            }
            fragment.arguments = args
            return fragment
        }
    }
}
