package com.example.spa.ui.home.adapter

import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.spa.data.response.TransactionList
import com.example.spa.databinding.LayoutTransactionBinding
import com.example.spa.utilities.formatDate

class ResentAdapter() :
    RecyclerView.Adapter<ResentAdapter.AwardsShopViewHolder>() {
     val arrayList = mutableListOf<TransactionList>()


    inner class AwardsShopViewHolder( val binding: LayoutTransactionBinding) :
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
            holder.binding.apply {
            textViewDate.text = formatDate(item.created_at)
            textViewAED.text = "+AED "+item.amount
            textViewPlumbings.text = item.description
        }
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

