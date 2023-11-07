package com.example.alarm.model.screens.main.details

import android.app.Activity
import android.graphics.Paint
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.CheckBox
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.SortedList
import com.example.alarm.App
import com.example.alarm.R
import com.example.alarm.model.Note

class Adapter : RecyclerView.Adapter<Adapter.NoteViewHolder>() {

    protected val sortedList: SortedList<Note>

    init {
        sortedList = SortedList(Note::class.java, object : SortedList.Callback<Note>() {
            override fun compare(o1: Note, o2: Note): Int {
                return if (!o2.done && o1.done) {
                    1
                } else if (o2.done && !o1.done) {
                    -1
                } else {
                    (o2.timestamp - o1.timestamp).toInt()
                }
            }

            override fun onChanged(position: Int, count: Int) {
                notifyItemRangeChanged(position, count)
            }

            override fun areContentsTheSame(oldItem: Note, newItem: Note): Boolean {
                return oldItem == newItem
            }

            override fun areItemsTheSame(item1: Note, item2: Note): Boolean {
                return item1.uid == item2.uid
            }

            override fun onInserted(position: Int, count: Int) {
                notifyItemRangeInserted(position, count)
            }

            override fun onRemoved(position: Int, count: Int) {
                notifyItemRangeRemoved(position, count)
            }

            override fun onMoved(fromPosition: Int, toPosition: Int) {
                notifyItemMoved(fromPosition, toPosition)
            }
        })
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): NoteViewHolder {
        return NoteViewHolder(
            LayoutInflater.from(parent.context).inflate(R.layout.item_note_list, parent, false)
        )
    }

    override fun onBindViewHolder(holder: NoteViewHolder, position: Int) {
        holder.bind(sortedList.get(position))
    }

    override fun getItemCount(): Int {
        return sortedList.size()
    }

    fun setItems(notes: List<Note>) {
        sortedList.replaceAll(notes)
    }

    inner class NoteViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

        private val noteText: TextView = itemView.findViewById(R.id.note_text)
        private val completed: CheckBox = itemView.findViewById(R.id.completed)
        private val delete: View = itemView.findViewById(R.id.delete)

        private var note: Note? = null
        private var silentUpdate = false

        init {
            itemView.setOnClickListener {
                note?.let { NoteDetailsActivity.start(itemView.context as Activity, it) }
            }

            delete.setOnClickListener {
                note?.let { App.getInstance().getNoteDao().delete(it) }
            }

            completed.setOnCheckedChangeListener { compoundButton, checked ->
                if (!silentUpdate) {
                    note?.done = checked
                    note?.let { App.getInstance().getNoteDao().update(it) }
                }
                updateStrokeOut()
            }
        }

        fun bind(note: Note) {
            this.note = note

            noteText.text = note.text
            updateStrokeOut()

            silentUpdate = true
            completed.isChecked = note.done
            silentUpdate = false
        }

        private fun updateStrokeOut() {
            if (note?.done == true) {
                noteText.paintFlags = noteText.paintFlags or Paint.STRIKE_THRU_TEXT_FLAG
            } else {
                noteText.paintFlags = noteText.paintFlags and Paint.STRIKE_THRU_TEXT_FLAG.inv()
            }
        }
    }
}
