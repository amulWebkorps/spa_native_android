package com.example.spa.ui.auth.adapter

import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.widget.AppCompatImageView
import androidx.appcompat.widget.AppCompatTextView
import androidx.recyclerview.widget.RecyclerView
import com.example.spa.R
import com.example.spa.data.response.TransactionList
import com.example.spa.ui.auth.dataClass.Tutorial

class TutorialAdapter(): RecyclerView.Adapter<TutorialAdapter.tutorialViewHolder>() {
    private val arrayList = mutableListOf<Tutorial>()

    inner class tutorialViewHolder(view: View): RecyclerView.ViewHolder(view){
        var image = view.findViewById<AppCompatImageView>(R.id.imageViewMain)
        var title = view.findViewById<AppCompatTextView>(R.id.textViewMain)

        fun bind(model:Tutorial){
            image.setImageResource(model.image)
            title.text = model.title
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): tutorialViewHolder {
         return tutorialViewHolder(
             LayoutInflater.from(parent.context).inflate(R.layout.layout_tutorial,parent,false)
         )

    }

    override fun onBindViewHolder(holder: tutorialViewHolder, position: Int) {
        holder.bind(arrayList[position])
    }

    override fun getItemCount(): Int =
        arrayList.size


    fun setListItem(list: List<Tutorial>) {
        arrayList.clear()
        arrayList.addAll(list)
        notifyDataSetChanged()
    }
}