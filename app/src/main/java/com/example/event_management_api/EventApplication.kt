// com/example/event_management_api/EventApplication.kt
package com.example.event_management_api

import android.app.Application
import com.example.event_management_api.data.remote.EventManager
import com.example.event_management_api.data.remote.RetrofitClient

// Didaftarkan di AndroidManifest.xml: android:name=".EventApplication"
class EventApplication : Application() {
    val eventManager by lazy {
        EventManager(RetrofitClient.instance)
    }
}