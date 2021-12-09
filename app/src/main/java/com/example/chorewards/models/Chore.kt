package com.example.chorewards.models

import android.util.Log
import com.example.chorewards.serializable.LocalDateSerializable
import com.example.chorewards.serializable.PeriodSerializable
//import com.example.chorewards.serializable.PeriodSerializable
import java.io.Serializable
import java.time.LocalDate
import java.time.Period


// Followed https://kotlinlang.org/docs/serialization.html#example-json-serialization

@kotlinx.serialization.Serializable
data class Chore (
    var name: String,
    var pointValue: Int? = null,
    var description: String? = null,

    @kotlinx.serialization.Serializable(with = PeriodSerializable::class)
    var frequency: Period? = null,
    @kotlinx.serialization.Serializable(with = LocalDateSerializable::class)
    var lastDone: LocalDate? = null,
    var canRepeat: Boolean = false
) : Comparable<Chore> {

    override fun compareTo(other: Chore): Int = run {
        // Compare next time to take
        val aLastDone = this.lastDone ?: return -1
        val aFreq = this.frequency ?: return -1
        val bLastDone = other.lastDone ?: return 1
        val bFreq = other.frequency ?: return 1

        val currTime = LocalDate.now()

        val currDiff = Period.between(aLastDone + aFreq, currTime).days
        val bDiff = Period.between(bLastDone + bFreq, currTime).days

        return currDiff.compareTo(bDiff)
    }

    fun getProgress(): Int {
        return if (lastDone != null && frequency != null) {
            val currTime = LocalDate.now()
            val currDiff = Period.between(lastDone, currTime)

            currDiff.days.div(frequency!!.days)
        } else {
            100
        }
    }

    fun completed(): Int {
//        if (lastDone != null) {
//            val currentDate = LocalDate.now()
//            val nextDate = Period.between(lastDone, currentDate).normalized()
//            val diff = Period.between(lastDone!!.plus(frequency), currentDate).normalized()
////            if (diff >= frequency) {
////                return pointValue
////            }
//        }
        lastDone = LocalDate.now()

        return pointValue ?: 0
    }
}
