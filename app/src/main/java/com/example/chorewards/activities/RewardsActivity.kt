package com.example.chorewards.activities

import android.content.Intent
import android.os.Bundle
import android.os.PersistableBundle
import android.util.Log
import android.view.Gravity
import android.view.LayoutInflater
import android.view.View
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.chorewards.R
import com.example.chorewards.adapters.RewardAdapter
import com.example.chorewards.models.Reward
import com.example.chorewards.models.RewardManager
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.google.android.material.slider.Slider
import kotlin.random.Random

class RewardsActivity : AppCompatActivity() {
    private val rewardManager: RewardManager = RewardManager()
    private lateinit var rewardAdapter: RewardAdapter
    private lateinit var rewardsRecyclerView: RecyclerView

    private var popUpOpen: Boolean = false

    private var totalPoints: Int = 0
    private lateinit var randomRewardButton: Button
    private lateinit var chosenRewardTextView: TextView
    private lateinit var fab: FloatingActionButton
    private lateinit var pointsTextView: TextView
    private lateinit var choresButton: ImageButton

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.rewards)

        pointsTextView = findViewById(R.id.totalPointsTextView)

        rewardManager.loadData(applicationContext)

        val totalPointsIntent = intent.getIntExtra("totalPoints", -1)
        if (totalPointsIntent > -1) {
            this.totalPoints = totalPointsIntent
        }

        updatePointsText()

        choresButton = findViewById(R.id.ChoresImageButton)
        choresButton.setOnClickListener {
            handleChoresNav(it)
        }

        fab = findViewById(R.id.new_reward_fab)
        fab.setOnClickListener {
            handleFabClick(it)
        }

        chosenRewardTextView = findViewById(R.id.chosenRewardTextView)
        randomRewardButton = findViewById(R.id.RandomRewardButton)
        randomRewardButton.setOnClickListener {
            handleRandomRewardButton(it)
        }

        rewardsRecyclerView = findViewById(R.id.rewardsRecycler)

        rewardManager.allData.sortBy { it.pointVal }

        rewardAdapter = RewardAdapter(rewardManager) {
            Log.i("Reward Clicked", it.name)

            val pos = rewardManager.removeItem(it)

            rewardAdapter.notifyItemRemoved(pos)

            // TODO("Implement pop up menu")
        }

        rewardsRecyclerView.adapter = rewardAdapter

        rewardsRecyclerView.layoutManager = LinearLayoutManager(applicationContext)
    }

    private fun updatePointsText() {
        val pointsString = resources.getString(R.string.number_of_points_label, totalPoints)
        pointsTextView.text = pointsString
    }

    override fun onSaveInstanceState(outState: Bundle, outPersistentState: PersistableBundle) {
        super.onSaveInstanceState(outState, outPersistentState)
        rewardManager.saveData(applicationContext)
        Log.d("save", rewardManager.toString())
    }

    // I would have loved to handle this better too :(
    override fun onRestoreInstanceState(savedInstanceState: Bundle) {
        super.onRestoreInstanceState(savedInstanceState)

        Log.d("restore", rewardManager.toString())
        rewardManager.loadData(applicationContext)
        rewardAdapter.notifyDataSetChanged()

        updatePointsText()
    }

    override fun onBackPressed() {
        super.onBackPressed()
        TODO("Not implemented")
    }

    private fun handleRandomRewardButton(v: View) {
        val possibleOptions = rewardManager.filterPossible(totalPoints)

        if (possibleOptions.isNotEmpty()) {
            val picked = possibleOptions[Random.nextInt(possibleOptions.size)]

            Log.d("Random Reward", picked.toString())

            chosenRewardTextView.text = "You got ${picked.name}! It cost ${picked.pointVal} points"

            totalPoints -= picked.pointVal

            updatePointsText()
        } else {
            chosenRewardTextView.text = "You don't have enough points for any rewards. Keep " +
                    "completing chores!"
        }
    }

    private fun handleChoresNav(v: View) {
        rewardManager.saveData(applicationContext)

        val intent = Intent(this, MainActivity::class.java)
        intent.putExtra("totalPoints", totalPoints)

        startActivity(intent)
    }

    private fun handleFabClick(v: View) {
        Log.d("Click", "New Reward")
        Log.d("Click", "popup ${popUpOpen}")

        // Check if there is a popup already open
        if (popUpOpen) return

        popUpOpen = true

        // Open add reward screen
        val addRewardView = LayoutInflater.from(applicationContext).inflate(R.layout.reward_add_screen,
            null)

        // Setup popup window
        val popWindow = PopupWindow(addRewardView, FrameLayout.LayoutParams.WRAP_CONTENT,
            FrameLayout.LayoutParams.WRAP_CONTENT)

        popWindow.isFocusable = true
        popWindow.isTouchModal = true


        // Connect data to view data
        val reward_name_view = addRewardView.findViewById<TextView>(R.id.rewardTitle)
        // I name this difficulty but it shouldn't be the opposite of difficulty
        val reward_difficulty_view = addRewardView.findViewById<SeekBar>(R.id.rewardDifficulty)
        val cancelButton = addRewardView.findViewById<Button>(R.id.rewardCancel)
        val doneButton = addRewardView.findViewById<Button>(R.id.rewardDone)

        // Connect click listeners to done and cancel buttons
        cancelButton.setOnClickListener {
            popWindow.dismiss()

            popUpOpen = false
        }

        doneButton.setOnClickListener {
            val reward_name = reward_name_view.text
            val reward_difficulty = reward_difficulty_view.progress

            reward_name?.let {
                val newReward = Reward(reward_name.toString(), (1 + (reward_difficulty * 2) / 5).toInt(),
                    true)

                rewardManager.addItem(newReward)

                rewardManager.allData.sortBy { it.pointVal }

                val pos = rewardManager.getIndex(newReward)
                rewardAdapter.notifyItemInserted(pos)
            }

            popWindow.dismiss()
            popUpOpen = false
        }

        popWindow.setOnDismissListener {
            popUpOpen = false
        }

        // View is set, show view at center
        popWindow.showAtLocation(v, Gravity.CENTER, 0, 0)

    }
}