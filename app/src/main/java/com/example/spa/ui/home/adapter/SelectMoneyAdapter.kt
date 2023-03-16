package com.example.spa.ui.home.adapter

import android.annotation.SuppressLint
import android.graphics.Color
import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.spa.R
import com.example.spa.data.remote.AddMoney
import com.example.spa.data.response.TransactionList
import com.example.spa.databinding.LayoutSelectMoneyBinding


class SelectMoneyAdapter(val onClick:Onclick) :
    RecyclerView.Adapter<SelectMoneyAdapter.SelectMoneyViewHolder>() {

    private val arrayList = mutableListOf<AddMoney>()

    var selectedItem = -1
    var isSelected = false

    inner class SelectMoneyViewHolder( val binding: LayoutSelectMoneyBinding) :
        RecyclerView.ViewHolder(binding.root) {
        init {

        }
     }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): SelectMoneyViewHolder {
        val binding = LayoutSelectMoneyBinding.inflate(
            LayoutInflater.from(parent.context),
            parent,
            false
        )
        return SelectMoneyViewHolder(binding)

    }
    override fun onBindViewHolder(holder: SelectMoneyViewHolder, position: Int) {
       val item= arrayList[position]
       with(holder.itemView) {
             holder.apply {
                 binding.apply {
                     textViewMoney.text = item.amount
                     if (selectedItem == position) {
                         textViewMoney.setBackgroundResource(R.drawable.money_selected)
                         textViewMoney.setTextColor(Color.parseColor("#FFFFFFFF"))
                     } else {
                         textViewMoney.setBackgroundResource(R.drawable.money_unselected)
                         textViewMoney.setTextColor(Color.parseColor("#1E1E1E"))
                     }
                         root.setOnClickListener {
                             Log.e("TAG", "onBindViewHolder:${isSelected} ", )
                             if (isSelected){
                                 isSelected = false
                                 onClick.onClick("0")
                                 selectedItem = -1
                             }
                             else {
                                 isSelected = true
                                 selectedItem = position
                                 onClick.onClick(item.amount)
                             }
                             notifyDataSetChanged()
                         }


                 }
             }}
    }

    override fun getItemCount(): Int = arrayList.size

    fun setListItem(list: List<AddMoney>) {
        arrayList.clear()
        arrayList.addAll(list)
        notifyDataSetChanged()
    }

interface Onclick{
    fun onClick(value:String)
}

}

