package com.example.spa.ui.home.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.spa.R
import com.example.spa.data.response.BankAccountDetail
import com.example.spa.databinding.FragmentTutorialBinding
import com.example.spa.databinding.LayoutBankDetailBinding
import com.example.spa.utilities.hideView
import com.example.spa.utilities.showView
//import kotlinx.android.synthetic.main.layout_bank_detail.view.*

class BankDetailAdapter(val onClick:OnClick) :
    RecyclerView.Adapter<BankDetailAdapter.BankDetailViewHolder>() {

    private lateinit var binding: FragmentTutorialBinding

    private val arrayList = mutableListOf<BankAccountDetail>()
    var isShow = false


    inner class BankDetailViewHolder( val binding: LayoutBankDetailBinding) :
        RecyclerView.ViewHolder(binding.root) {
        init {
            binding.bg.setOnClickListener {
                isShow = if (!isShow){
                    binding.layoutDetails.showView()
                    binding.imageViewArrow.setImageResource(R.drawable.ic_up_arrow)
                    true
                }else{
                    binding.layoutDetails.hideView()
                    binding.imageViewArrow.setImageResource(R.drawable.ic_bottom_arrow)
                    false
                }
            }
//            binding.imageViewDelete.setOnClickListener {
//                onClick.onDeleteClick(absoluteAdapterPosition,arrayList[absoluteAdapterPosition].id)
//            }
            }


        }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BankDetailViewHolder {
        val binding = LayoutBankDetailBinding.inflate(
            LayoutInflater.from(parent.context),
            parent,
            false
        )
        return BankDetailViewHolder(binding)

    }

    override fun onBindViewHolder(holder: BankDetailViewHolder, position: Int) {
        val item = arrayList[position]
        with(holder.itemView) {

            holder.binding.apply {
                textViewAED.text = item.account_holder_name!!
                textViewBankName.text = item.bank_name!!
                textViewAccountTypeValue.text = item.account_type!!
                item.ifsc_code?.let {
                    textViewIFSCValue.text = item.ifsc_code!!.toString()
                    when {
                        item.frequency!!.toString() == context.getString(R.string.weekly) -> {
                            textViewFrequencyValue.text = context.getString(R.string.weekly)
                        }
                        item.frequency!!.toString() == context.getString(R.string.monthly) -> {
                            textViewFrequencyValue.text = context.getString(R.string.monthly)
                        }
                        item.frequency!!.toString() == context.getString(R.string.quarterly) -> {
                            textViewFrequencyValue.text = context.getString(R.string.quarterly)
                        }
                        else -> {
                            textViewFrequencyValue.text = context.getString(R.string.yearly)
                        }
                    }
                }

                textViewAccountNumberValue.text = item.account_number!!

                if (item.is_active) {
                    textViewActive.setBackgroundResource(R.drawable.bg_active)
                    textViewActive.text = context.getString(R.string.active)
                } else {
                    textViewActive.setBackgroundResource(R.drawable.bg_inactive)
                    textViewActive.text = context.getString(R.string.inactive)
                }
            }
        }
    }

    override fun getItemCount(): Int = arrayList.size

    interface OnClick{
        fun onDeleteClick(pos:Int,id:Int)
    }


    fun setListItem(list: List<BankAccountDetail>) {
        arrayList.clear()
        arrayList.addAll(list)
        notifyDataSetChanged()
    }

    fun deleteItem(pos:Int){
        if (pos<arrayList.size) {
                arrayList.removeAt(pos)
                notifyDataSetChanged()
            }

    }
}

