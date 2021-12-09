package com.example.chorewards.models

import android.content.Context
import android.util.Log
import kotlinx.serialization.Serializable
import kotlinx.serialization.json.Json
import org.json.JSONArray
import org.json.JSONObject
import java.io.File

@Serializable
class RewardManager {
    var size: Int = 0
    var allData: MutableList<Reward> = arrayListOf()
    private var filename = "rewards_data"

    fun addItem(newReward: Reward) {
        allData += newReward
        size = allData.size
    }

    fun removeItem(r: Reward): Int {
        val pos = getIndex(r)

        allData.removeAt(pos)
        // reset size
        size = allData.size

        return pos
    }

    fun getReward(i: Int): Reward {
        return allData[i]
    }

    fun getIndex(reward: Reward): Int {
        return allData.indexOf(reward)
    }

    fun filterPossible(currPoints: Int): List<Reward> {
        allData.forEach {
            it.canUse = it.pointVal <= currPoints
        }

        return allData.filter { it.canUse }
    }

    fun saveData(context: Context) {
        Log.d("saveData", "Attempting to save reward data")
        val saveData = Json.encodeToString(serializer(), this)
        context.openFileOutput(filename, Context.MODE_PRIVATE).use {
            it.write(saveData.toByteArray())
        }
    }

    fun loadData(context: Context) {
        val file = File(context.filesDir, filename)

        if (!file.canRead()) return

        val lineData = file.readLines().toString() + '\n'
        Log.d("loadData", lineData.toString())

        if (lineData.isNotEmpty()) {
            val jsonResArray = JSONArray(lineData.toString())

            val rewardJson = jsonResArray[0] as JSONObject

            Log.d("loadData", rewardJson.toString())

            try {
                val tempSize = rewardJson.optInt("size", 0)
                val tempRewardList: MutableList<Reward> = arrayListOf()
                val rewardsList = rewardJson.optJSONArray("allData")

                for (i in 0 until rewardsList.length()) {
                    val savedRewardData = rewardsList.getJSONObject(i)

                    tempRewardList.add(
                        Reward(
                            savedRewardData.optString("name", "Not Named"),
                            savedRewardData.optInt("pointVal"),
                            savedRewardData.optBoolean("canUse", false)
                        )
                    )
                }

                this.size = tempSize
                this.allData = tempRewardList

            } catch (e: Exception) {

            } finally {
                Log.d("loadData", "No data found")
            }
        }
    }
}