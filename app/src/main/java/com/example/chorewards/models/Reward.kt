package com.example.chorewards.models

import kotlinx.serialization.Serializable

@Serializable
data class Reward(var name: String, val pointVal: Int, var canUse: Boolean = false) {

}