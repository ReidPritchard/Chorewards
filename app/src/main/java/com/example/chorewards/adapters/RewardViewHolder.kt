package com.example.chorewards.adapters

import android.view.View
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.chorewards.R
import com.example.chorewards.models.Reward

class RewardViewHolder(view: View, onItemClick: (Reward) -> Unit) : RecyclerView.ViewHolder(view) {
    private lateinit var reward: Reward
    private val rewardName: TextView = view.findViewById(R.id.rewardName)

    init {
        itemView.setOnClickListener {
            onItemClick(reward)
        }
    }

    fun bind(reward: Reward) {
        this.reward = reward

        this.rewardName.text = reward.name
    }

}