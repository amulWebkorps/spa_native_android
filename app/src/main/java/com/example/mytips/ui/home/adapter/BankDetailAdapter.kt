package com.example.mytips.ui.home.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.mytips.databinding.LayoutBankDetailBinding
import com.example.mytips.databinding.LayoutResentTransactionBinding
import com.example.mytips.databinding.LayoutTransactionBinding

class BankDetailAdapter(val onClick:OnClick) :
    RecyclerView.Adapter<BankDetailAdapter.BankDetailViewHolder>() {


    inner class BankDetailViewHolder(private val binding: LayoutBankDetailBinding) :
        RecyclerView.ViewHolder(binding.root) {
        init {
            binding.imageViewDelete.setOnClickListener {
                onClick.onDeleteClick(absoluteAdapterPosition)
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
    }

    override fun getItemCount(): Int = 4

    interface OnClick{
        fun onDeleteClick(pos:Int)
    }
}

