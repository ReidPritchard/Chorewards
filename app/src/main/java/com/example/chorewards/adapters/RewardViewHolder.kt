package com.example.chorewards.adapters

import android.view.View
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.chorewards.R
import com.example.chorewards.interfaces.onItemClick
import com.example.chorewards.models.Reward
import org.w3c.dom.Text

class RewardViewHolder(view: View, onItemClick: (Reward) -> Unit) : RecyclerView.ViewHolder(view) {
    private lateinit var reward: Reward
    private val rewardName: TextView = view.findViewById(R.id.rewardName)
    private val pointsValue: TextView = view.findViewById(R.id.PointsValue)

    init {
        itemView.setOnClickListener {
            onItemClick(reward)
        }
    }

    fun bind(reward: Reward) {
        this.reward = reward

        this.rewardName.text = reward.name
        this.pointsValue.text = "Points: ${reward.pointVal}"
    }

}