// com/example/event_management_api/data/remote/EventManager.kt
package com.example.event_management_api.data.remote

import android.content.Context
import android.widget.Toast
import com.example.event_management_api.data.model.Event
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import retrofit2.HttpException
import java.io.IOException

class EventManager(private val apiService: EventApiService) {

    fun createEvent(newEvent: Event, context: Context, onSuccess: () -> Unit) {
        CoroutineScope(Dispatchers.IO).launch {
            try {
                val response = apiService.createEvent(newEvent)

                withContext(Dispatchers.Main) {
                    if (response.status == 201 && response.data != null) {
                        Toast.makeText(context, "✅ Event '${response.data.title}' berhasil dibuat!", Toast.LENGTH_LONG).show()
                        onSuccess() // Beri tahu Anggota B (Activity)
                    } else {
                        Toast.makeText(context, "Gagal (Status ${response.status}): ${response.message}", Toast.LENGTH_LONG).show()
                    }
                }
            } catch (e: HttpException) {
                withContext(Dispatchers.Main) {
                    Toast.makeText(context, "Error API: ${e.code()} - ${e.message()}", Toast.LENGTH_LONG).show()
                }
            } catch (e: IOException) {
                withContext(Dispatchers.Main) {
                    Toast.makeText(context, "Error Jaringan. Periksa koneksi.", Toast.LENGTH_LONG).show()
                }
            }
        }
    }
}