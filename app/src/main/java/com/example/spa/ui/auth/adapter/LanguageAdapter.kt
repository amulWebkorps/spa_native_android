package com.example.spa.ui.auth.adapter

import android.content.Context
import android.graphics.Color
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.widget.AppCompatTextView
import androidx.cardview.widget.CardView
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.example.spa.R
import com.example.spa.ui.auth.dataClass.Language

class LanguageAdapter(val onClick:OnClick,val context: Context ,val language:String): RecyclerView.Adapter<LanguageAdapter.laungageViewHolder>() {
    private val arrayList = mutableListOf<Language>()

    inner class laungageViewHolder(view: View): RecyclerView.ViewHolder(view){
        var title = view.findViewById<AppCompatTextView>(R.id.textViewEnglish)
        var cardView = view.findViewById<CardView>(R.id.cardViewLanguage)

        fun bind(model:Language){
            title.text = model.language
            if (language == model.language) {
                title.setTextColor(ContextCompat.getColor(context, R.color.colorBlue72));
            }
            cardView.setOnClickListener {
                onClick.onClick(title.text.toString())
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): laungageViewHolder {
         return laungageViewHolder(
             LayoutInflater.from(parent.context).inflate(R.layout.layout_language,parent,false)
         )
    }

    override fun onBindViewHolder(holder: laungageViewHolder, position: Int) {
        holder.bind(arrayList[position])


      }

    override fun getItemCount(): Int = 2


    fun setListItem(list: List<Language>) {
        arrayList.clear()
        arrayList.addAll(list)
        notifyDataSetChanged()
    }
    interface OnClick{
        fun onClick(selectLanguage:String)
    }
}