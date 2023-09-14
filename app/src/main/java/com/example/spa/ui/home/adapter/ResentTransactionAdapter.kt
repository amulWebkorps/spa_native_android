package com.example.spa.ui.home.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.spa.R
import com.example.spa.data.response.TransactionList
import com.example.spa.databinding.LayoutResentTransactionBinding
import com.example.spa.utilities.formatDate
//import kotlinx.android.synthetic.main.layout_bank_detail.view.*
//import kotlinx.android.synthetic.main.layout_bank_detail.view.textViewAED
//import kotlinx.android.synthetic.main.layout_resent_transaction.view.*
//import kotlinx.android.synthetic.main.layout_transaction.view.*
//import kotlinx.android.synthetic.main.layout_transaction.view.textViewDate

class ResentTransactionAdapter() :
    RecyclerView.Adapter<ResentTransactionAdapter.AwardsShopViewHolder>() {

    private val arrayList = mutableListOf<TransactionList>()



    inner class AwardsShopViewHolder( val binding: LayoutResentTransactionBinding) :
        RecyclerView.ViewHolder(binding.root) {
        init {

            }

        }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): AwardsShopViewHolder {
        val binding = LayoutResentTransactionBinding.inflate(
            LayoutInflater.from(parent.context),
            parent,
            false
        )

        return AwardsShopViewHolder(binding)

    }

    override fun onBindViewHolder(holder: AwardsShopViewHolder, position: Int) {
        val item = arrayList[position]
        with(holder.itemView) {
            holder.binding.apply {
                textViewDate.text = formatDate(item.created_at)
                textViewAED.text = "+AED " + item.amount
                textViewResentPlumbings.text = item.description
            }
        }
    }

    fun setListItem(list: List<TransactionList>) {
        arrayList.clear()
        arrayList.addAll(list)
        notifyDataSetChanged()
    }


    override fun getItemCount(): Int = arrayList.size

}

