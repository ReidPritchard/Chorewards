package com.example.chorewards.models

// TODO: Make this class @Serializable
data class Reward (var name: String, val pointVal: Int, var canUse: Boolean = false) {

}