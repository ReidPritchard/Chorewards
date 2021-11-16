package com.example.chorewards.activities

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.ImageButton
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.chorewards.R
import com.example.chorewards.adapters.ChoreAdapter
import com.example.chorewards.models.Chore
import com.example.chorewards.models.ChoreManager
import com.example.chorewards.models.Reward
import com.google.android.material.floatingactionbutton.FloatingActionButton
import java.time.LocalDate
import java.time.Period

class MainActivity : AppCompatActivity() {
    private var choreManager = ChoreManager()
    private lateinit var choreAdapter: ChoreAdapter
    private lateinit var recycler: RecyclerView

    private lateinit var fab: FloatingActionButton
    private lateinit var homeButton: ImageButton
    private lateinit var rewardsButton: ImageButton

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        rewardsButton = findViewById(R.id.RewardsImageButton)
        rewardsButton.setOnClickListener {
            handleRewardsNav(it)
        }

        fab = findViewById(R.id.new_chore_fab)
        fab.setOnClickListener {
            handleFabClick(it)
        }


        recycler = findViewById(R.id.choreRecycler)

        choreManager.addItem(
            Chore(
                "Clean room",
                20,
                "Clean one room in my house",
                Period.ofWeeks(1),
                null,
                canRepeat = false
            )
        )
        choreManager.addItem(
            Chore(
                "Take out trash",
                10,
                "Take out the trash, if it's full",
                Period.ofDays(4),
                null,
                canRepeat = false
            )
        )
        choreManager.addItem(
            Chore(
                "Water Plants",
                5,
                "Water all your plants!",
                Period.ofDays(4),
                null
            )
        )

        choreManager.addItem(
            Chore(
                "Do Homework",
                15,
                "Finish all hw for the day.",
                Period.ofDays(1),
                LocalDate.now(),
                canRepeat = true
            )
        )

        choreManager.allData.sort()

        // On click based off this:
        // https://oozou.com/blog/a-better-way-to-handle-click-action-in-a-recyclerview-item-60
        choreAdapter = ChoreAdapter(choreManager) {
            Log.i("Chore Clicked", it.name)

            val prev = choreManager.getIndex(it)
            choreManager.completeChore(it)
//            choreAdapter.notifyItemChanged(prev)

            choreManager.allData.sort()
            val pos = choreManager.getIndex(it)

            choreAdapter.notifyItemMoved(prev, pos)
            choreAdapter.notifyItemChanged(pos)
            choreAdapter.notifyItemChanged(prev)
            choreAdapter.notifyItemChanged(0)
        }
        recycler.adapter = choreAdapter

        recycler.layoutManager = GridLayoutManager(applicationContext, 2)
    }

    private fun handleRewardsNav(v: View) {
        val intent = Intent(this, RewardsActivity::class.java)
        intent.putExtra("totalPoints", choreManager.totalPoints)

        startActivity(intent)
    }

    private fun handleFabClick(v: View) {
        val chore = Chore(
                "Do something",
                15,
                "Yea we should do something else",
                Period.ofDays(4),
                LocalDate.now(),
                canRepeat = true
            )
        choreManager.addItem(chore);
        val pos = choreManager.getIndex(chore)

        choreAdapter.notifyItemInserted(pos)
        choreAdapter.notifyItemRangeChanged(pos, choreManager.size)
    }
}