package com.example.mytips.ui.home.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.mytips.databinding.LayoutResentTransactionBinding

class ResentTransactionAdapter() :
    RecyclerView.Adapter<ResentTransactionAdapter.AwardsShopViewHolder>() {


    inner class AwardsShopViewHolder(private val binding: LayoutResentTransactionBinding) :
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
    }

    override fun getItemCount(): Int = 4

}

