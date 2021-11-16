package com.example.chorewards.adapters

import android.view.View
import android.widget.ImageView
import android.widget.ProgressBar
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.chorewards.R
import com.example.chorewards.models.Chore


class ChoreViewHolder(view: View, onItemClicked: (Chore) -> Unit) : RecyclerView.ViewHolder(view) {
    private lateinit var chore: Chore
    private val nameTextView: TextView = view.findViewById(R.id.taskName)
    private val descriptionTextView: TextView = view.findViewById(R.id.taskDescription)
    private val progressBar: ProgressBar = view.findViewById(R.id.todoProgress)
    private val choreImage: ImageView = view.findViewById(R.id.imageView)

    init {
        itemView.setOnClickListener {
            onItemClicked(chore)
        }
    }

    fun bind(chore: Chore) {
        this.chore = chore

        this.nameTextView.text = chore.name
        this.descriptionTextView.text = chore.description
        this.progressBar.progress = chore.getProgress()
        this.choreImage.setImageResource(R.drawable.undraw_environment)
    }
}
