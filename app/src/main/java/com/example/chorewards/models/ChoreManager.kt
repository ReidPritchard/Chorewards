package com.example.chorewards.models

import android.content.Context
import android.util.Log
import kotlinx.serialization.json.Json
import org.json.JSONArray
import org.json.JSONObject
import java.io.File
import java.lang.Exception
import java.time.LocalDate
import java.time.Period

@kotlinx.serialization.Serializable
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

    fun removeChore(c: Chore): Int {
        val i = getIndex(c)

        allData.removeAt(i)
        size -= 1

        return i
    }

    fun saveData(context: Context) {
        Log.d("saveData", "Attempting to save chore data")
        val saveChoreData = Json.encodeToString(serializer(), this)
        context.openFileOutput(filename, Context.MODE_PRIVATE).use {
            it.write(saveChoreData.toByteArray())
        }
    }

    fun loadData(context: Context) {
        val file = File(context.filesDir, filename)

        if (!file.canRead()) return

        val lineData = file.readLines().toString() + '\n'
        Log.d("loadData", lineData.toString())

        if (lineData.isNotEmpty()) {

            val jsonResArray = JSONArray(lineData.toString())

            Log.d("loadData", jsonResArray.toString())

            val choreJson = jsonResArray[0] as JSONObject

            Log.d("loadData", choreJson.toString())

            try {
                val savedSize = choreJson.getInt("size")
                val savedPoints = choreJson.optInt("totalPoints", 0)
                val choreList = choreJson.getJSONArray("allData")

                val tempAllData: MutableList<Chore> = arrayListOf()

                for (i in 0 until choreList.length()) {
                    val savedChoreData = choreList.getJSONObject(i)

                    tempAllData.add(
                        Chore(
                            savedChoreData.optString("name", "No Name"),
                            savedChoreData.getInt("pointValue"),
                            savedChoreData.optString("description", "No Description"),
                            Period.parse(savedChoreData.optString("frequency") ?: null),
                            savedChoreData.optLong("lastDone")?.let { LocalDate.ofEpochDay(it) },
                            savedChoreData.optBoolean("canRepeat", false)
                        )
                    )
                }
                this.size = savedSize
                this.totalPoints = savedPoints
                this.allData = tempAllData

            } catch (e: Exception) {

            } finally {
                Log.d("loadData", "No data found")
            }
        }
    }
}
