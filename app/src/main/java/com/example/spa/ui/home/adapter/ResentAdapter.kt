package com.example.spa.ui.home.adapter

import android.content.Context
import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.spa.R
import com.example.spa.data.response.TransactionList
import com.example.spa.databinding.LayoutTransactionBinding
import com.example.spa.utilities.formatDate
import com.example.spa.utilities.getInFrench

class ResentAdapter(val context:Context) :
    RecyclerView.Adapter<ResentAdapter.AwardsShopViewHolder>() {
     val arrayList = mutableListOf<TransactionList>()
    var map2 = mutableMapOf<String,String>()

    inner class AwardsShopViewHolder( val binding: LayoutTransactionBinding) :
        RecyclerView.ViewHolder(binding.root) {
        init {
            map2.put("restoration", context.getString(R.string.restoration))
            map2.put("concierge_hotel_services", context.getString(R.string.concierge_hotel_services))
            map2.put("transportations", context.getString(R.string.transportations))
            map2.put("sale", context.getString(R.string.sale))
            map2.put("Other", context.getString(R.string.Other))
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
            textViewDate.text = item.created_at.getInFrench(context)
            textViewAED.text = "+AED "+item.amount

                if (map2.containsKey(item.description)) {
                    textViewPlumbings.text = map2.get(item.description)
                }else{
                    textViewPlumbings.text = item.description
                }
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

