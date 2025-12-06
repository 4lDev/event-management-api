package com.example.event_management_api.ui.eventlist

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.event_management_api.R
import com.example.event_management_api.data.remote.RetrofitClient
import com.google.android.material.floatingactionbutton.FloatingActionButton
import kotlinx.coroutines.launch
import java.lang.Exception

class EventListActivity : AppCompatActivity() {

    private lateinit var recyclerView: RecyclerView
    private lateinit var eventAdapter: EventAdapter
    private lateinit var fabAddEvent: FloatingActionButton

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_event_list)

        // 1. Setup Views
        recyclerView = findViewById(R.id.rvEvents)
        fabAddEvent = findViewById(R.id.fabAddEvent)

        // 2. Setup RecyclerView
        eventAdapter = EventAdapter(emptyList())
        recyclerView.layoutManager = LinearLayoutManager(this)
        recyclerView.adapter = eventAdapter

        // 3. Setup Listener untuk FAB (Use Case Create Event)
        fabAddEvent.setOnClickListener {
            val intent = Intent(this, com.example.event_management_api.ui.eventcreate.CreateEventActivity::class.java)
            startActivity(intent)
        }

        // 4. Ambil data
        fetchEvents()
    }

    override fun onResume() {
        super.onResume()
        fetchEvents()
    }

    private fun fetchEvents() {
        lifecycleScope.launch {
            try {
                val response = RetrofitClient.instance.getAllEvents()

                if (response.status == 200 && response.data != null) {
                    eventAdapter.updateData(response.data)
                } else {
                    Toast.makeText(this@EventListActivity, "Gagal: ${response.message}", Toast.LENGTH_SHORT).show()
                }
            } catch (e: Exception) {
                Log.e("API_FATAL", "Exception: ${e.message}", e)
            }
        }
    }
}