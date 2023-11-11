package com.example.alarm.model.screens.main.details

import android.app.Activity
import android.app.DatePickerDialog
import android.app.DatePickerDialog.OnDateSetListener
import android.app.TimePickerDialog
import android.app.TimePickerDialog.OnTimeSetListener
import android.content.Intent
import android.os.Bundle
import android.text.format.DateUtils
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import com.example.alarm.App
import com.example.alarm.R
import com.example.alarm.model.Note
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Locale

@Suppress("DEPRECATION")
class NoteDetailsActivity : AppCompatActivity() {
    private var EditDate: Boolean = false
    private var EditTime: Boolean = false
    private var note: Note = Note()
    private var editText: EditText? = null
    private var editTitle: EditText? = null
    var currentDateTime: TextView? = null
    var dateAndTime: Calendar = Calendar.getInstance()

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

    private var date = Calendar.getInstance()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_note_details)

        currentDateTime = findViewById(R.id.currentDateTime)
        setInitialDateTime()

        val toolbar: Toolbar = findViewById(R.id.toolbar)
        setSupportActionBar(toolbar)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar?.setDisplayShowHomeEnabled(true)

        title = getString(R.string.note_details_title)

        editText = findViewById(R.id.text)
        editTitle = findViewById(R.id.textTitle)

        if (intent.hasExtra(EXTRA_NOTE)) {
            note = intent.getParcelableExtra(EXTRA_NOTE) ?: Note()
            editText?.setText(note.text)
            editTitle?.setText(note.titleNote)
            val formattedDate = convertLongToDateString(note.DataNote)
            currentDateTime?.text = formattedDate
            date.timeInMillis = note.DataNote
            Log.d("NoteDetailsActivity", "Note object on create: $note")
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
                val editTitle = editTitle
                if (editText != null && editText.text.isNotEmpty() && editTitle != null && editTitle.text.isNotEmpty()) {
                    note.text = editText.text.toString()
                    note.titleNote = editTitle.text.toString()
                    note.done = false
                    note.timestamp = System.currentTimeMillis()

                    // Проверка, если datenote не установлено (равно 0), то устанавливаем значение из dateAndTime
                    if (EditDate == true || EditTime == true) {


                        var calendarDateAndTime = dateAndTime


                        var yearDateAndTime = calendarDateAndTime.get(Calendar.YEAR)
                        var monthDateAndTime = calendarDateAndTime.get(Calendar.MONTH) + 1
                        var dayDateAndTime = calendarDateAndTime.get(Calendar.DAY_OF_MONTH)
                        var hourDateAndTime = calendarDateAndTime.get(Calendar.HOUR_OF_DAY)
                        var minuteDateAndTime = calendarDateAndTime.get(Calendar.MINUTE)


                        var calendar = Calendar.getInstance()

                        calendar.timeInMillis = note.DataNote
                        var year = calendar.get(Calendar.YEAR)
                        var month = calendar.get(Calendar.MONTH) + 1
                        var day = calendar.get(Calendar.DAY_OF_MONTH)
                        var hour = calendar.get(Calendar.HOUR_OF_DAY)
                        var minute = calendar.get(Calendar.MINUTE)
                        if (year != yearDateAndTime) {
                            year = yearDateAndTime
                        }

                        if (month != monthDateAndTime) {
                            month = monthDateAndTime
                        }

                        if (day != dayDateAndTime) {
                            day = dayDateAndTime
                        }

                        if (hour != hourDateAndTime) {
                            hour = hourDateAndTime
                        }

                        if (minute != minuteDateAndTime) {
                            minute = minuteDateAndTime
                        }
                        calendar.set(Calendar.YEAR, year)
                        calendar.set(Calendar.MONTH, month - 1)
                        calendar.set(Calendar.DAY_OF_MONTH, day)
                        calendar.set(Calendar.HOUR_OF_DAY, hour)
                        calendar.set(Calendar.MINUTE, minute)

                        note.DataNote = convertDateToLong(calendar)
                    }





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

    fun convertTimeToLong(calendar: Calendar): Long {
        return calendar.get(Calendar.HOUR_OF_DAY).toLong() * 60 + calendar.get(Calendar.MINUTE)
    }

    var temp: Long = note.DataNote

    fun setDate(v: View?) {
        Log.d(
            "NoteDetailsActivity",
            "Note.DataNote1: ${note.DataNote}, dateAndTime: ${dateAndTime.timeInMillis}"
        )
        val currentDate = Calendar.getInstance()
        val currentDateNote = Calendar.getInstance()
        if (date.timeInMillis != 0L) {

            currentDate.timeInMillis = note.DataNote
            dateAndTime=date
        }


        EditDate = true
        val datePicker = DatePickerDialog(
            this@NoteDetailsActivity,
            DatePickerDialog.OnDateSetListener { _, year, monthOfYear, dayOfMonth ->
                dateAndTime[Calendar.YEAR] = year
                dateAndTime[Calendar.MONTH] = monthOfYear
                dateAndTime[Calendar.DAY_OF_MONTH] = dayOfMonth
                setInitialDateTime()
            },
            dateAndTime[Calendar.YEAR],
            dateAndTime[Calendar.MONTH],
            dateAndTime[Calendar.DAY_OF_MONTH]
        )

        datePicker.datePicker.minDate = currentDateNote.timeInMillis
        datePicker.show()
    }

    fun setTime(v: View?) {
        val currentTime = Calendar.getInstance()

        val currentDateNote = Calendar.getInstance()
        if (date.timeInMillis != 0L) {

            currentTime.timeInMillis = note.DataNote
            dateAndTime=date
        }

            EditTime = true
            val timePicker = TimePickerDialog(
                this@NoteDetailsActivity,
                TimePickerDialog.OnTimeSetListener { _, hourOfDay, minute ->
                    dateAndTime[Calendar.HOUR_OF_DAY] = hourOfDay
                    dateAndTime[Calendar.MINUTE] = minute
                    val currentTime = Calendar.getInstance()
                    if (dateAndTime.timeInMillis < currentTime.timeInMillis) {
                        Toast.makeText(
                            this@NoteDetailsActivity,
                            "Выберите время не раньше текущего",
                            Toast.LENGTH_SHORT
                        ).show()
                    } else {
                        setInitialDateTime()
                    }
                },
                dateAndTime[Calendar.HOUR_OF_DAY],
                dateAndTime[Calendar.MINUTE],
                true
            )

            timePicker.show()


    }

    private fun setInitialDateTime() {
        Log.d(
            "NoteDetailsActivity",
            "seint: ${note.DataNote}, dateAndTime: ${dateAndTime.timeInMillis}"
        )

        currentDateTime!!.text = DateUtils.formatDateTime(
            this,
            dateAndTime.timeInMillis,
            DateUtils.FORMAT_SHOW_DATE or DateUtils.FORMAT_SHOW_YEAR or DateUtils.FORMAT_SHOW_TIME
        )
    }

    var t = OnTimeSetListener { _, hourOfDay, minute ->
        dateAndTime[Calendar.HOUR_OF_DAY] = hourOfDay
        dateAndTime[Calendar.MINUTE] = minute
        setInitialDateTime()
    }

    var d = OnDateSetListener { _, year, monthOfYear, dayOfMonth ->
        dateAndTime[Calendar.YEAR] = year
        dateAndTime[Calendar.MONTH] = monthOfYear
        dateAndTime[Calendar.DAY_OF_MONTH] = dayOfMonth
        setInitialDateTime()
    }

    fun convertDateToLong(calendar: Calendar): Long {
        return calendar.timeInMillis

    }

    private fun convertLongToDateString(timestamp: Long): String {
        val dateFormat = SimpleDateFormat("dd MMMM yyyy, HH:mm", Locale.getDefault())
        return dateFormat.format(timestamp)
    }
}
