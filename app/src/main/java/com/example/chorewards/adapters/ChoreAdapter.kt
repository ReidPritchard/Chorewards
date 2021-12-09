package com.example.chorewards.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.chorewards.R
import com.example.chorewards.models.Chore
import com.example.chorewards.models.ChoreManager

// Lots of help from this tutorial https://guides.codepath.com/android/Using-the-RecyclerView
class ChoreAdapter(private val chores: ChoreManager, private val onItemClicked: (Chore) -> Unit, private val onItemLongClick: (Chore, View) -> Boolean) :
    RecyclerView.Adapter<ChoreViewHolder>() {

    override fun onCreateViewHolder(viewGroup: ViewGroup, viewType: Int): ChoreViewHolder {
        val choreView = LayoutInflater.from(viewGroup.context)
            .inflate(R.layout.todo_item, viewGroup, false)
        return ChoreViewHolder(choreView, {
            onItemClicked(it)
        }, { chore: Chore, view: View ->
            onItemLongClick(chore, view)
        })
    }

    override fun onBindViewHolder(holder: ChoreViewHolder, position: Int) {
        holder.bind(chores.getItem(position))
    }

    override fun getItemCount(): Int {
        return chores.size
    }
}
