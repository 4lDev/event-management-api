package com.example.event_management_api.ui.eventcreate

import android.app.DatePickerDialog
import android.app.TimePickerDialog
import android.content.Context
import android.os.Bundle
import android.view.View
import android.widget.ArrayAdapter
import android.widget.Button
import android.widget.EditText
import android.widget.Spinner
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.event_management_api.EventApplication
import com.example.event_management_api.R
import com.example.event_management_api.data.model.Event
import com.google.android.material.textfield.TextInputEditText
import java.util.Calendar

class CreateEventActivity : AppCompatActivity() {

    private lateinit var etTitle: TextInputEditText
    private lateinit var etDate: TextInputEditText
    private lateinit var etTime: TextInputEditText
    private lateinit var etLocation: TextInputEditText
    private lateinit var etDescription: TextInputEditText
    private lateinit var etCapacity: TextInputEditText
    private lateinit var spinnerStatus: Spinner
    private lateinit var btnCreateEvent: Button

    // Akses EventManager (Anggota A)
    private val eventManager by lazy {
        (application as EventApplication).eventManager
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_create_event)

        initViews()
        setupListeners()
    }

    private fun initViews() {
        // Binding View (pastikan ID ini benar di XML!)
        etTitle = findViewById(R.id.etTitle)
        etDate = findViewById(R.id.etDate)
        etTime = findViewById(R.id.etTime)
        etLocation = findViewById(R.id.etLocation)
        etDescription = findViewById(R.id.etDescription)
        etCapacity = findViewById(R.id.etCapacity)
        spinnerStatus = findViewById(R.id.spinnerStatus)
        btnCreateEvent = findViewById(R.id.btnCreateEvent)

        // Setup Spinner Status
        val statusArray = resources.getStringArray(R.array.status_array)
        val adapter = ArrayAdapter(this, android.R.layout.simple_spinner_dropdown_item, statusArray)
        spinnerStatus.adapter = adapter
    }

    private fun setupListeners() {
        etDate.setOnClickListener { showDatePickerDialog(etDate) }
        etTime.setOnClickListener { showTimePickerDialog(etTime) }

        btnCreateEvent.setOnClickListener {
            if (validateForm()) {
                submitEventData()
            }
        }
    }

    private fun showDatePickerDialog(editText: EditText) {
        val calendar = Calendar.getInstance()
        val year = calendar.get(Calendar.YEAR)
        val month = calendar.get(Calendar.MONTH)
        val day = calendar.get(Calendar.DAY_OF_MONTH)

        val datePickerDialog = DatePickerDialog(this,
                { _, selectedYear, selectedMonth, selectedDay ->
                        val date = String.format("%d-%02d-%02d", selectedYear, selectedMonth + 1, selectedDay)
                        editText.setText(date)
                }, year, month, day)
        datePickerDialog.show()
    }

    private fun showTimePickerDialog(editText: EditText) {
        val calendar = Calendar.getInstance()
        val hour = calendar.get(Calendar.HOUR_OF_DAY)
        val minute = calendar.get(Calendar.MINUTE)

        val timePickerDialog = TimePickerDialog(this,
                { _, selectedHour, selectedMinute ->
                        val time = String.format("%02d:%02d:00", selectedHour, selectedMinute)
                        editText.setText(time)
                }, hour, minute, true)
        timePickerDialog.show()
    }

    private fun validateForm(): Boolean {
        // Validasi Field Wajib (Anggota B harus memastikan ini lengkap)
        if (etTitle.text.isNullOrEmpty()) { etTitle.error = "Judul wajib diisi"; return false }
        if (etDate.text.isNullOrEmpty()) { etDate.error = "Tanggal wajib diisi"; return false }
        if (etTime.text.isNullOrEmpty()) { etTime.error = "Waktu wajib diisi"; return false }
        if (etLocation.text.isNullOrEmpty()) { etLocation.error = "Lokasi wajib diisi"; return false }
        return true
    }

    private fun submitEventData() {
        // Ekstraksi data form
        val newEvent = Event(
                id = null,
                title = etTitle.text.toString(),
                date = etDate.text.toString(),
                time = etTime.text.toString(),
                location = etLocation.text.toString(),
                description = etDescription.text.toString().ifEmpty { null },
        capacity = etCapacity.text.toString().toIntOrNull() ?: 0,
                status = spinnerStatus.selectedItem.toString(),
                createdAt = null,
                updatedAt = null
        )

        // Panggil Anggota A (EventManager) untuk mengirim data
        eventManager.createEvent(newEvent, this) {
            // Callback sukses: Tutup Activity dan memicu refresh di EventListActivity
            Toast.makeText(this, "Event berhasil dipublikasi!", Toast.LENGTH_SHORT).show()
            finish()
        }
    }
}