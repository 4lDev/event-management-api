// com/example/event_management_api/data/model/ApiResponse.kt
package com.example.event_management_api.data.model

data class ApiResponse<T>(
        val status: Int,
        val message: String,
        val data: T?,
        val timestamp: String?
)