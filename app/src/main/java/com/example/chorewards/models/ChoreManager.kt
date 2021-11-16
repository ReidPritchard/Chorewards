package com.example.chorewards.models

import android.graphics.ColorSpace
import android.util.Log
import kotlinx.serialization.internal.throwArrayMissingFieldException

class ChoreManager {
    var size: Int = 0
    var allData: MutableList<Chore> = arrayListOf()
    var totalPoints: Int = 0
    private var filename = "chore_data"

    fun addItem(nChore: Chore) {
        allData += nChore
        size = allData.size
    }

    fun getItem(i: Int): Chore {
        return allData[i]
    }

    fun getIndex(chore: Chore): Int {
        return allData.indexOf(chore)
    }

    fun completeChore(c: Chore) {
        totalPoints += c.completed()
    }

    fun saveData() {
        TODO("Implement persistent data")
//        val file = File(context.filesDir, filename)
    }
}
