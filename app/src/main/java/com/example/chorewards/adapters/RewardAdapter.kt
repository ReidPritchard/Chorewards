package com.example.chorewards.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.chorewards.R
import com.example.chorewards.models.Reward
import com.example.chorewards.models.RewardManager

class RewardAdapter(
    private val rewardsManager: RewardManager, private val onItemClick:
    (Reward) -> Unit
) : RecyclerView.Adapter<RewardViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RewardViewHolder {
        val rewardView = LayoutInflater.from(parent.context)
            .inflate(R.layout.reward_item, parent, false)
        return RewardViewHolder(rewardView) {
            onItemClick(it)
        }
    }

    override fun onBindViewHolder(holder: RewardViewHolder, position: Int) {
        holder.bind(rewardsManager.getReward(position))
    }

    override fun getItemCount(): Int {
        return rewardsManager.size
    }
}