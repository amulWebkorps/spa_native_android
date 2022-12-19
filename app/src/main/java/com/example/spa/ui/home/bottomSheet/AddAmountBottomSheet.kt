package com.example.spa.ui.home.bottomSheet

import android.text.Editable
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.View
import androidx.fragment.app.FragmentManager
import com.example.spa.base.BaseBottomSheetDialogFragment
import com.example.spa.databinding.LayoutWithdrawalBottomsheetBinding

class AddAmountBottomSheet:BaseBottomSheetDialogFragment() {

    private var onClick: (Int) -> (Unit) = {}
    private var binding: LayoutWithdrawalBottomsheetBinding? = null

    companion object {
        fun showDialog(
            fragmentManager: FragmentManager,
//            onClick: Unit
        ) {
            return AddAmountBottomSheet().apply {
                //this.onClick = onClick
            }.show(fragmentManager, AddAmountBottomSheet::class.java.simpleName)
        }
    }
    override fun isTitleAvailable(): Boolean = false

    override fun onReady() {
        binding.apply {
            this!!.imageViewBack.setOnClickListener {
                dismiss()
            }

            editTextEnterAmount.addTextChangedListener (object : TextWatcher {
                override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {

                }

                override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
                    buttonWithdrawal.text = "Withdraw AED $p0"
                }

                override fun afterTextChanged(p0: Editable?) {

                }

            })

        }
      }

    override fun getBinding(): View {
        binding = LayoutWithdrawalBottomsheetBinding.inflate(LayoutInflater.from(context), null, false)
        return binding!!.root
     }
}