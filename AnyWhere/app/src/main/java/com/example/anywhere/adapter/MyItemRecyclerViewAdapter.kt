package com.example.anywhere.adapter

import android.content.Context
import androidx.recyclerview.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import com.example.anywhere.R
import com.example.anywhere.api.model.RelatedTopicModel
import kotlinx.android.synthetic.main.fragment_list_item.view.*

class MyItemRecyclerViewAdapter(
    private val context: Context,
    private val clickListener: OnItemClickListener
) : RecyclerView.Adapter<MyItemRecyclerViewAdapter.ViewHolder>(), Filterable {

    var charactersList: ArrayList<String> = ArrayList()
    var filteredCharactersList: ArrayList<String> = ArrayList()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(
            LayoutInflater.from(context).inflate(
                R.layout.fragment_list_item,
                parent,
                false
            )
        )
    }

    fun setUpData(values: List<String>) {
        charactersList = values as ArrayList<String>
        filteredCharactersList = values
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val item = filteredCharactersList[position]
        holder.contentView.apply {
            text = item
            isClickable = true
            isSingleLine = true
            setOnClickListener {
                clickListener.onItemClicked(item)
            }
        }
    }

    override fun getItemCount(): Int = filteredCharactersList.size

    inner class ViewHolder(var view: View) : RecyclerView.ViewHolder(view) {
        val contentView: TextView = view.content

        override fun toString(): String {
            return super.toString() + " '" + contentView.text + "'"
        }
    }

    interface OnItemClickListener {
        fun onItemClicked(name: String)
    }

    override fun getFilter(): Filter {
        return object : Filter() {
            override fun performFiltering(constraint: CharSequence?): FilterResults {
                val charString = constraint?.toString() ?: ""
                if (charString.isEmpty()) filteredCharactersList = charactersList else {
                    val filteredList = ArrayList<String>()
                    charactersList
                        .filter {
                            (it.contains(constraint!!))
                        }
                        .forEach { filteredList.add(it) }
                    filteredCharactersList = filteredList
                }
                return FilterResults().apply { values = filteredCharactersList }
            }

            override fun publishResults(constraint: CharSequence?, results: FilterResults?) {

                filteredCharactersList = if (results?.values == null) ArrayList()
                else results.values as ArrayList<String>
                notifyDataSetChanged()
            }
        }
    }

}