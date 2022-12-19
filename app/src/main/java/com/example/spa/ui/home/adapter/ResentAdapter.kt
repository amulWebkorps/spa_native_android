package com.example.spa.ui.home.adapter

import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.spa.data.response.TransactionList
import com.example.spa.databinding.LayoutTransactionBinding
import com.example.spa.utilities.formatDate
import kotlinx.android.synthetic.main.layout_bank_detail.view.*
import kotlinx.android.synthetic.main.layout_bank_detail.view.textViewAED
import kotlinx.android.synthetic.main.layout_resent_transaction.view.*
import kotlinx.android.synthetic.main.layout_transaction.view.*
import kotlinx.android.synthetic.main.layout_transaction.view.textViewDate

class ResentAdapter() :
    RecyclerView.Adapter<ResentAdapter.AwardsShopViewHolder>() {
     val arrayList = mutableListOf<TransactionList>()


    inner class AwardsShopViewHolder(private val binding: LayoutTransactionBinding) :
        RecyclerView.ViewHolder(binding.root) {
        init {
            }

        }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): AwardsShopViewHolder {
        val binding = LayoutTransactionBinding.inflate(
            LayoutInflater.from(parent.context),
            parent,
            false
        )
        return AwardsShopViewHolder(binding)

    }

    override fun onBindViewHolder(holder: AwardsShopViewHolder, position: Int) {
        val item = arrayList[position]
        with(holder.itemView){
            textViewDate.text = formatDate(item.created_at)
            textViewAED.text = "+AED "+item.amount
            textViewPlumbings.text = item.description
        }
    }

    override fun getItemCount(): Int = arrayList.size


    fun setListItem(list: List<TransactionList>) {
        arrayList.clear()
        arrayList.addAll(list)
        notifyDataSetChanged()
    }

    fun setListItem2(list: List<TransactionList>) {
     //   arrayList.clear()
        arrayList.addAll(list)
        notifyDataSetChanged()
    }

}

