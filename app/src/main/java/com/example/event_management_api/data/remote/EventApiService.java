// com/example/event_management_api/data/remote/EventApiService.kt
package com.example.event_management_api.data.remote

import com.example.event_management_api.data.model.ApiResponse
import com.example.event_management_api.data.model.Event
import retrofit2.http.*

interface EventApiService {
    // Use Case Wajib #2: GET All Events
    @GET("api.php")
    suspend fun getAllEvents(): ApiResponse<List<Event>>

    // Use Case Wajib #3: GET Event by ID
    @GET("api.php")
    suspend fun getEventById(@Query("id") eventId: String): ApiResponse<Event>

    // Use Case Wajib #1: POST Create Event
    @POST("api.php")
    suspend fun createEvent(@Body eventData: Event): ApiResponse<Event>
}