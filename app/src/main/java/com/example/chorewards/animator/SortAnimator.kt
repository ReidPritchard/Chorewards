package com.example.chorewards.animator

import android.util.Log
import androidx.recyclerview.widget.DefaultItemAnimator
import androidx.recyclerview.widget.RecyclerView

class SortAnimator : DefaultItemAnimator() {

    override fun animateChange(
        oldHolder: RecyclerView.ViewHolder,
        newHolder: RecyclerView.ViewHolder,
        preInfo: RecyclerView.ItemAnimator.ItemHolderInfo,
        postInfo: RecyclerView.ItemAnimator.ItemHolderInfo
    ): Boolean {
        Log.d("AnimatePersistence", "AnimateChange")
        Log.d("AnimatePersistence", oldHolder.toString())
        Log.d("AnimatePersistence", preInfo.top.toString())
        Log.d("AnimatePersistence", postInfo.top.toString())

        val fromLeft: Int = preInfo.left
        val fromTop: Int = preInfo.top
        val toLeft: Int
        val toTop: Int
//        if (newHolder.shouldIgnore()) {
//            toLeft = preInfo.left
//            toTop = preInfo.top
//        } else {
//            toLeft = postInfo.left
//            toTop = postInfo.top
//        }
        toLeft = postInfo.left
        toTop = postInfo.top
        return animateChange(oldHolder, newHolder, fromLeft, fromTop, toLeft, toTop)
    }

    override fun animatePersistence(
        viewHolder: RecyclerView.ViewHolder,
        preLayoutInfo: RecyclerView.ItemAnimator.ItemHolderInfo,
        postLayoutInfo: RecyclerView.ItemAnimator.ItemHolderInfo
    ): Boolean {
        Log.d("AnimatePersistence", "AnimatePersistence")
        Log.d("AnimatePersistence", preLayoutInfo.top.toString())
        Log.d("AnimatePersistence", postLayoutInfo.top.toString())

        return super.animatePersistence(viewHolder, preLayoutInfo, postLayoutInfo)
    }

}