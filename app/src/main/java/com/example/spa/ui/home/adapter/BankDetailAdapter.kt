package com.example.spa.ui.home.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.spa.data.response.BankAccountDetail
import com.example.spa.databinding.LayoutBankDetailBinding
import kotlinx.android.synthetic.main.layout_bank_detail.view.*

class BankDetailAdapter(val onClick:OnClick) :
    RecyclerView.Adapter<BankDetailAdapter.BankDetailViewHolder>() {

    private val arrayList = mutableListOf<BankAccountDetail>()

    inner class BankDetailViewHolder(private val binding: LayoutBankDetailBinding) :
        RecyclerView.ViewHolder(binding.root) {
        init {
            binding.imageViewDelete.setOnClickListener {
                onClick.onDeleteClick(absoluteAdapterPosition,arrayList[absoluteAdapterPosition].id)
            }
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
        with(holder.itemView){
            textViewAED.text =item.account_holder_name
            textViewBankName.text =item.bank_name
            if (!item.is_active){
                textViewAED.alpha = 0.7f
                imageViewBg.alpha = 0.7f
                textViewBankName.alpha = 0.7f
                bg.alpha = 0.5f
                cardView.elevation = 5f
            }else{
                textViewAED.alpha = 1f
                imageViewBg.alpha = 1f
                textViewBankName.alpha = 1f
                bg.alpha = 1f
                cardView.elevation = 6f
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

