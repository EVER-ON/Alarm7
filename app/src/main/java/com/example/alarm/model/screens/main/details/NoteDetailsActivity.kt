package com.example.alarm.model.screens.main.details

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.widget.EditText
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import com.example.alarm.App
import com.example.alarm.R
import com.example.alarm.model.Note


@Suppress("DEPRECATION")
class NoteDetailsActivity : AppCompatActivity() {

    private var note: Note = Note()  // Изменено на создание объекта Note

    private var editText: EditText? = null

    companion object {
        private const val EXTRA_NOTE = "NoteDetailsActivity.EXTRA_NOTE"

        fun start(caller: Activity, note: Note?) {
            val intent = Intent(caller, NoteDetailsActivity::class.java)
            if (note != null) {
                intent.putExtra(EXTRA_NOTE, note)
            }
            caller.startActivity(intent)
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_note_details)

        val toolbar: Toolbar = findViewById(R.id.toolbar)
        setSupportActionBar(toolbar)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar?.setDisplayShowHomeEnabled(true)

        title = getString(R.string.note_details_title)

        editText = findViewById(R.id.text)

        if (intent.hasExtra(EXTRA_NOTE)) {
            note = intent.getParcelableExtra(EXTRA_NOTE) ?: Note()  // Добавлена проверка на null
            editText?.setText(note.text)
        }
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        menuInflater.inflate(R.menu.menu_details, menu)
        return super.onCreateOptionsMenu(menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            android.R.id.home -> finish()
            R.id.action_save -> {
                val editText = editText
                if (editText != null && editText.text.isNotEmpty()) {
                    note.text = editText.text.toString()
                    note.done = false
                    note.timestamp = System.currentTimeMillis()
                    if (intent.hasExtra(EXTRA_NOTE)) {
                        App.getInstance().getNoteDao().update(note)
                    } else {
                        App.getInstance().getNoteDao().insert(note)
                    }
                    finish()
                }
            }
        }
        return super.onOptionsItemSelected(item)
    }
}