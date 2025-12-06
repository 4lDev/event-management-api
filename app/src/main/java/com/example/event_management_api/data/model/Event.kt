// com/example/event_management_api/data/model/Event.kt
package com.example.event_management_api.data.model

import com.google.gson.annotations.SerializedName

data class Event(
    val id: String?,
    val title: String,
    val date: String,
    val time: String,
    val location: String,
    val status: String,

    val description: String?,
    val capacity: Int?,

    @SerializedName("created_at")
    val createdAt: String?,
    @SerializedName("updated_at")
    val updatedAt: String?
)