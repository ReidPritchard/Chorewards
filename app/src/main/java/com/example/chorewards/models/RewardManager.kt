package com.example.chorewards.models

class RewardManager(var currPoints: Int) {
    var size: Int = 0
    var allData: MutableList<Reward> = arrayListOf()
    private var filename = "rewards_data"

    fun addItem(newReward: Reward) {
        allData += newReward
        size = allData.size
    }

    fun getReward(i: Int): Reward {
        return allData[i]
    }

    fun getIndex(reward: Reward): Int {
        return allData.indexOf(reward)
    }

    fun filterPossible(): List<Reward> {
        allData.forEach {
            it.canUse = it.pointVal < currPoints
        }

        return allData.filter { it.canUse }
    }

    fun saveData() {
        TODO("Implement persistent data")
    }

}