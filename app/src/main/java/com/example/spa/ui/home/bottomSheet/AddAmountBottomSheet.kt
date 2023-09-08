package com.example.spa.ui.home.bottomSheet

import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.widget.Toast
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import com.example.spa.R
import com.example.spa.base.BaseBottomSheetDialogFragment
import com.example.spa.databinding.LayoutWithdrawalBottomsheetBinding
import com.example.spa.utilities.hasInternet
import com.example.spa.utilities.showToast
import com.example.spa.utilities.validation.ApplicationException
import com.example.spa.viewmodel.SettingsViewModel
import com.google.android.material.snackbar.Snackbar

class AddAmountBottomSheet(private val onclick:(Int,String)->Unit):BaseBottomSheetDialogFragment() {

    private var binding: LayoutWithdrawalBottomsheetBinding? = null
    var value="0.0"

    companion object {
        fun showDialog(
            fragmentManager: FragmentManager,
        //    onClick: Unit
        ) {
            return AddAmountBottomSheet({
                i,s->
            }).apply {
          //      this.onClick = onClick
            }.show(fragmentManager, AddAmountBottomSheet::class.java.simpleName)
        }
    }
    override fun isTitleAvailable(): Boolean = false

    override fun onReady() {
        binding.apply {
            this!!.imageViewBack.setOnClickListener {
                onclick.invoke(it!!.id, value)
            }

            this!!.buttonWithdrawal.setOnClickListener {
                Log.e("TAG", "onReady: $value", )
                  onclick.invoke(it!!.id, value)
              }

            editTextEnterAmount.addTextChangedListener (object : TextWatcher {
                override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {

                }

                override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
                    buttonWithdrawal.text = "Withdraw AED $p0"
                    value = p0.toString()
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
