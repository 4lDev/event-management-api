package com.example.event_management_api.ui.eventcreate

import android.app.DatePickerDialog
import android.app.TimePickerDialog
import android.content.Context
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.ArrayAdapter
import android.widget.Button
import android.widget.EditText
import android.widget.Spinner
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.event_management_api.R
import com.example.event_management_api.data.model.Event
import com.google.android.material.textfield.TextInputEditText
import java.util.Calendar

class CreateEventActivity : AppCompatActivity() {

    // Deklarasi Komponen UI
    private lateinit var etTitle: TextInputEditText
    private lateinit var etDate: TextInputEditText
    private lateinit var etTime: TextInputEditText
    private lateinit var etLocation: TextInputEditText
    private lateinit var etDescription: TextInputEditText
    private lateinit var etCapacity: TextInputEditText
    private lateinit var spinnerStatus: Spinner
    private lateinit var btnCreateEvent: Button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_create_event)

        // Inisialisasi View
        initViews()
        setupListeners()
    }

    private fun initViews() {
        etTitle = findViewById(R.id.etTitle)
        etDate = findViewById(R.id.etDate)
        etTime = findViewById(R.id.etTime)
        etLocation = findViewById(R.id.etLocation)
        etDescription = findViewById(R.id.etDescription)
        etCapacity = findViewById(R.id.etCapacity)
        spinnerStatus = findViewById(R.id.spinnerStatus)
        btnCreateEvent = findViewById(R.id.btnCreateEvent)

        // Setup Spinner
        val statusArray = resources.getStringArray(R.array.status_array)
        val adapter = ArrayAdapter(this, android.R.layout.simple_spinner_dropdown_item, statusArray)
        spinnerStatus.adapter = adapter
    }

    private fun setupListeners() {
        // Klik pada EditText Tanggal untuk memunculkan Date Picker
        etDate.setOnClickListener { showDatePickerDialog(etDate) }

        // Klik pada EditText Waktu untuk memunculkan Time Picker
        etTime.setOnClickListener { showTimePickerDialog(etTime) }

        // Klik Tombol Create Event
        btnCreateEvent.setOnClickListener {
            if (validateForm()) {
                // Panggil fungsi pengiriman data
                submitEventData()
            }
        }
    }

    // Fungsi Helper untuk Date Picker
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

    // Fungsi Helper untuk Time Picker
    private fun showTimePickerDialog(editText: EditText) {
        val calendar = Calendar.getInstance()
        val hour = calendar.get(Calendar.HOUR_OF_DAY)
        val minute = calendar.get(Calendar.MINUTE)

        val timePickerDialog = TimePickerDialog(this,
                { _, selectedHour, selectedMinute ->
                        val time = String.format("%02d:%02d:00", selectedHour, selectedMinute) // Format HH:MM:SS
                        editText.setText(time)
                }, hour, minute, true)
        timePickerDialog.show()
    }

    // Validasi Form Dasar (Pastikan field wajib terisi)
    private fun validateForm(): Boolean {
        if (etTitle.text.isNullOrEmpty()) {
            etTitle.error = "Judul wajib diisi"
            return false
        }
        if (etDate.text.isNullOrEmpty()) {
            etDate.error = "Tanggal wajib diisi"
            return false
        }
        if (etTime.text.isNullOrEmpty()) {
            etTime.error = "Waktu wajib diisi"
            return false
        }
        if (etLocation.text.isNullOrEmpty()) {
            etLocation.error = "Lokasi wajib diisi"
            return false
        }
        return true
    }

    // Fungsi yang akan memanggil Anggota A (Data Logic)
    private fun submitEventData() {
        val title = etTitle.text.toString()
        val date = etDate.text.toString()
        val time = etTime.text.toString()
        val location = etLocation.text.toString()
        val description = etDescription.text.toString().ifEmpty { null }
        val capacityString = etCapacity.text.toString()
        val capacity = capacityString.toIntOrNull() // Null jika kosong/tidak valid
        val status = spinnerStatus.selectedItem.toString()

        val newEvent = Event(
                id = null, // ID di-generate server
                title = title,
                date = date,
                time = time,
                location = location,
                description = description,
                capacity = capacity ?: 0, // Kirim 0 jika null (atau sesuaikan dengan kebutuhan validasi API)
                status = status,
                createdAt = null,
                updatedAt = null
        )

        // KRITIS: Di sini Anggota A harus menulis fungsi untuk pengiriman data
        // Untuk saat ini, kita akan panggil fungsi dummy (Lihat bagian Anggota A)
        Log.d("CREATE_EVENT", "Data siap kirim: $newEvent")
        (application as? EventApplication)?.eventManager?.createEvent(newEvent, this)
    }
}