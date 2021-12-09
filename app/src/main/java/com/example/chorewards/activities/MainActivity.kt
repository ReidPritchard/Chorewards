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
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.chorewards.R
import com.example.chorewards.adapters.ChoreAdapter
import com.example.chorewards.models.Chore
import com.example.chorewards.models.ChoreManager
import com.google.android.material.floatingactionbutton.FloatingActionButton
import kotlinx.serialization.json.Json
import java.lang.Exception
import java.time.Period


class MainActivity : AppCompatActivity() {
    private var choreManager = ChoreManager()
    private lateinit var choreAdapter: ChoreAdapter
    private lateinit var recycler: RecyclerView

    private var popUpOpen: Boolean = false

    private lateinit var fab: FloatingActionButton
    private lateinit var rewardsButton: ImageButton

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        choreManager.loadData(applicationContext)
        val tempPoints = intent.getIntExtra("totalPoints", -1)
        if (tempPoints > -1) {
            choreManager.totalPoints = tempPoints
        }
        choreManager.allData.sort()

        rewardsButton = findViewById(R.id.RewardsImageButton)
        rewardsButton.setOnClickListener {
            handleRewardsNav(it)
        }

        fab = findViewById(R.id.new_chore_fab)
        fab.setOnClickListener {
            handleFabClick(it)
        }

        recycler = findViewById(R.id.choreRecycler)

        // Got some ideas from here:
        // https://stackoverflow.com/questions/36097953/how-to-provide-custom-animation-during-sorting-notifydatasetchanged-on-recycle
//        val customAnimator = SortAnimator()
//        recycler.itemAnimator = customAnimator

        // On click based off this:
        // https://oozou.com/blog/a-better-way-to-handle-click-action-in-a-recyclerview-item-60
        choreAdapter = ChoreAdapter(choreManager, {
            handleChoreOnClick(it)
        }) { chore: Chore, view: View ->
            handleChoreOnLongClick(chore, view)
        }

        recycler.adapter = choreAdapter

        recycler.layoutManager = GridLayoutManager(applicationContext, 2)
    }

    override fun onSaveInstanceState(outState: Bundle, outPersistentState: PersistableBundle) {
        super.onSaveInstanceState(outState, outPersistentState)
        choreManager.saveData(applicationContext) // This doesn't work :/

        outState.putSerializable("chore_data", choreManager.allData as ArrayList<Chore>)
    }

    // I would have loved to handle this better :(
    override fun onRestoreInstanceState(savedInstanceState: Bundle) {
        super.onRestoreInstanceState(savedInstanceState)
        Log.d("restore", choreManager.toString())
        choreManager.loadData(applicationContext) // This doesn't work, might be the context??

        val data = savedInstanceState.getSerializable("chore_data")

        data?.let {
            try {
                val tempD = it as ArrayList<Chore>
                choreManager.allData = tempD.toMutableList()
            } catch (e: Exception) {
                Log.d("Deserialzation", "Failed, moving on")
            }
        }

        choreAdapter.notifyDataSetChanged()
    }

    override fun onBackPressed() {
        super.onBackPressed()
        TODO("Not implemented")
    }

    private fun handleChoreOnLongClick(chore: Chore, v: View): Boolean {
        val popup = PopupMenu(applicationContext, v)

        popup.menuInflater.inflate(R.menu.popup_menu, popup.menu)

        //registering popup with OnMenuItemClickListener
        popup.setOnMenuItemClickListener { item ->
            Log.d("click", item.toString())
            if (item.title == "Edit") {
                TODO("Implement Edit View")
            } else if (item.title == "Delete") {
                val index = choreManager.removeChore(chore)
                choreAdapter.notifyItemRemoved(index)

                choreManager.saveData(applicationContext)
            }
            true
        }

        popup.show()
        return true
    }

    private fun handleChoreOnClick(chore: Chore) {
        Log.i("Chore Clicked", chore.name)

        val prev = choreManager.getIndex(chore)
        val previousOrder = choreManager.allData

        choreManager.completeChore(chore)
        choreAdapter.notifyItemChanged(prev)

        choreManager.allData.sort()

        Log.d("HandleChore", previousOrder.toString())
        Log.d("HandleChore", choreManager.allData.toString())
//        previousOrder.forEachIndexed { i, chore ->
//            val pos = choreManager.getIndex(chore)
//
//            if (i != pos)
//                choreAdapter.notifyItemMoved(i, pos)
//        }

//        choreAdapter.notifyItemMoved(prev, pos)
//        choreAdapter.notifyItemRangeChanged(prev, choreManager.size + 1)
        choreManager.saveData(applicationContext)
        choreAdapter.notifyDataSetChanged()

//        // Got some ideas from here:
//        // https://stackoverflow.com/questions/36097953/how-to-provide-custom-animation-during-sorting-notifydatasetchanged-on-recycle
//        val layoutManager: GridLayoutManager = recycler.layoutManager as GridLayoutManager
//        val firstVisible = layoutManager.findFirstVisibleItemPosition()
//        val lastVisible = layoutManager.findLastVisibleItemPosition()
//        choreAdapter.notifyItemRangeChanged(prev, lastVisible-firstVisible+1)
    }

    private fun handleRewardsNav(v: View) {
        choreManager.saveData(applicationContext)

        val intent = Intent(this, RewardsActivity::class.java)
        intent.putExtra("totalPoints", choreManager.totalPoints)

        startActivity(intent)
    }

    private fun handleFabClick(v: View) {
        // Check if there is a popup already open
        if (popUpOpen) return

        popUpOpen = true

        // Open add reward screen
        var addRewardView = LayoutInflater.from(applicationContext).inflate(
            R.layout
                .chore_add_screen,
            null
        )

        // Setup popup window
        val popWindow = PopupWindow(
            addRewardView, FrameLayout.LayoutParams.WRAP_CONTENT,
            FrameLayout.LayoutParams.WRAP_CONTENT
        )

        popWindow.isFocusable = true
        popWindow.isTouchModal = true


        // Connect data to view data
        val choreNameInput = addRewardView.findViewById<TextView>(R.id.chore_name_input)
        val choreDescriptionInput =
            addRewardView.findViewById<TextView>(R.id.chore_description_input)
        val choreFreqInput = addRewardView.findViewById<TextView>(R.id.chore_freq_input)
        val choreDifficultySeekBar = addRewardView.findViewById<SeekBar>(R.id.chore_difficulty_bar)
        val choreDoneButton = addRewardView.findViewById<Button>(R.id.chore_done_button)

        // Connect click listeners to done and cancel buttons
        choreDoneButton.setOnClickListener {
            val choreName = choreNameInput.text
            val choreDescription = choreDescriptionInput.text
            val choreFreq = choreFreqInput.text
            val choreDifficulty = choreDifficultySeekBar.progress

            choreName?.let {
                if (choreDescription != null && choreFreq != null) {
                    val newChore = Chore(
                        choreName.toString(), choreDifficulty, choreDescription
                            .toString(), Period.ofDays(Integer.parseInt(choreFreq.toString())), null
                    )

                    choreManager.addItem(newChore)

                    choreManager.allData.sort()

                    val pos = choreManager.getIndex(newChore)
                    choreAdapter.notifyItemInserted(pos)
                }
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