// com/example/event_management_api/ui/eventdetail/EventDetailActivity.kt

package com.example.event_management_api.ui.eventdetail

import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import com.example.event_management_api.R
import com.example.event_management_api.data.model.Event
import com.example.event_management_api.data.remote.RetrofitClient
import kotlinx.coroutines.launch
import java.lang.Exception
import androidx.core.content.ContextCompat

class EventDetailActivity : AppCompatActivity() {

    // Kunci untuk Intent (Wajib Sama dengan yang dipakai di EventAdapter)
    companion object {
        const val EXTRA_EVENT_ID = "extra_event_id"
    }

    // Deklarasi View yang akan di-bind (Anggota B harus memastikan ID ini ada di XML)
    private lateinit var tvDetailTitle: TextView
    private lateinit var tvDetailStatus: TextView
    private lateinit var tvDetailDateTime: TextView
    private lateinit var tvDetailLocation: TextView
    private lateinit var tvDetailDescription: TextView
    private lateinit var tvDetailCapacity: TextView
    private lateinit var tvDetailCreatedAt: TextView
    private lateinit var tvError: TextView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        // XML ini harus dibuat Anggota B di langkah berikutnya
        setContentView(R.layout.activity_event_detail)
    
        initViews()

        // 1. Ambil ID dari Intent
        val eventId = intent.getStringExtra(EXTRA_EVENT_ID)

        if (eventId != null) {
            // 2. Jika ID ada, panggil fungsi fetch
            fetchEventDetail(eventId)
        } else {
            // 3. Jika ID hilang (error)
            showError("ID Event tidak ditemukan.")
        }
    }

    private fun initViews() {
        tvDetailTitle = findViewById(R.id.tvDetailTitle)
        tvDetailStatus = findViewById(R.id.tvDetailStatus)
        tvDetailDateTime = findViewById(R.id.tvDetailDateTime)
        tvDetailLocation = findViewById(R.id.tvDetailLocation)
        tvDetailDescription = findViewById(R.id.tvDetailDescription)
        tvDetailCapacity = findViewById(R.id.tvDetailCapacity)
        tvDetailCreatedAt = findViewById(R.id.tvDetailCreatedAt)
        tvError = findViewById(R.id.tvError)
    }

    private fun fetchEventDetail(eventId: String) {
        // Tampilkan loading/sembunyikan error saat fetching
        tvError.visibility = View.GONE
        // Anda bisa menambahkan ProgressBar di sini

        lifecycleScope.launch {
            try {
                // Panggil endpoint GET by ID (Anggota A)
                val response = RetrofitClient.instance.getEventById(eventId)

                if (response.status == 200 && response.data != null) {
                    // Sukses: tampilkan data
                    bindDataToView(response.data)
                } else if (response.status == 404) {
                    // Event tidak ditemukan
                    showError("Event ID '$eventId' tidak ditemukan.")
                } else {
                    // Error API lainnya
                    showError("Gagal memuat detail event: ${response.message}")
                }
            } catch (e: Exception) {
                // Error Jaringan/Fatal
                showError("Error Jaringan atau Server: ${e.message}")
                Log.e("DETAIL_FATAL", "Exception saat fetch detail: ${e.message}", e)
            }
        }
    }

    private fun bindDataToView(event: Event) {
        tvDetailTitle.text = event.title
        tvDetailDateTime.text = "Tanggal & Waktu: ${event.date} | ${event.time}"
        tvDetailLocation.text = "Lokasi: ${event.location}"

        // Deskripsi (Opsional, tangani jika null/kosong)
        tvDetailDescription.text = event.description ?: "Tidak ada deskripsi."

        // Kapasitas (Tampilkan 0 jika null)
        tvDetailCapacity.text = "Kapasitas: ${event.capacity ?: 0} orang"

        // Status & Warna (Sama seperti di Adapter)
        tvDetailStatus.text = event.status.uppercase()
        val statusColorRes = when (event.status) {
            "upcoming" -> R.color.status_upcoming
            "ongoing" -> R.color.status_ongoing
            "completed" -> R.color.status_completed
            "cancelled" -> R.color.status_cancelled
            else -> R.color.black
        }
        tvDetailStatus.setTextColor(ContextCompat.getColor(this, statusColorRes))

        // Created At
        tvDetailCreatedAt.text = "Dibuat: ${event.createdAt ?: "-"}"

        // Atur Judul ActionBar (optional)
        supportActionBar?.title = event.title
    }

    private fun showError(message: String) {
        tvError.text = "🛑 ERROR: $message"
        tvError.visibility = View.VISIBLE
        // Sembunyikan detail lainnya jika ada error
        findViewById<View>(R.id.detail_content_group)?.visibility = View.GONE
    }
}