package com.example.mytips.ui.home.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.mytips.databinding.LayoutResentTransactionBinding
import com.example.mytips.databinding.LayoutTransactionBinding

class ResentAdapter() :
    RecyclerView.Adapter<ResentAdapter.AwardsShopViewHolder>() {


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
    }

    override fun getItemCount(): Int = 14

}

