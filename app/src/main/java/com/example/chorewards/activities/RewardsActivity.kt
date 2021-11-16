package com.example.chorewards.activities

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.chorewards.R
import com.example.chorewards.adapters.RewardAdapter
import com.example.chorewards.models.Reward
import com.example.chorewards.models.RewardManager
import com.google.android.material.floatingactionbutton.FloatingActionButton

class RewardsActivity : AppCompatActivity() {
    private val rewardManager: RewardManager = RewardManager(10)
    private lateinit var rewardAdapter: RewardAdapter
    private lateinit var rewardsRecyclerView: RecyclerView

    private lateinit var fab: FloatingActionButton
    private lateinit var pointsTextView: TextView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.rewards)

        pointsTextView = findViewById(R.id.totalPointsTextView)

        val totalPoints = intent.getIntExtra("totalPoints", 0)
        rewardManager.currPoints = totalPoints

        pointsTextView.text = "You have $totalPoints points"

        fab = findViewById(R.id.new_reward_fab)
        fab.setOnClickListener {
            handleFabClick(it)
        }

        rewardsRecyclerView = findViewById(R.id.rewardsRecycler)

        rewardManager.addItem(
            Reward("Candy", 20)
        )

        rewardManager.addItem(
            Reward("Extra long poop", 30)
        )

        rewardManager.addItem(
            Reward("Sleep In", 50)
        )

        rewardManager.allData.sortBy { it.canUse }

        rewardAdapter = RewardAdapter(rewardManager) {
            Log.i("Reward Clicked", it.name)

            rewardAdapter.notifyDataSetChanged()

            TODO("Implement pop up menu")
        }

        rewardsRecyclerView.adapter = rewardAdapter

        rewardsRecyclerView.layoutManager = LinearLayoutManager(applicationContext)
    }

    fun handleFabClick(v: View) {
        Log.d("Click", "New Reward")
    }
}