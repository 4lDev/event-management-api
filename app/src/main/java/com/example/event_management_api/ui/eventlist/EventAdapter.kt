package com.example.event_management_api.ui.eventlist

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.example.event_management_api.R
import com.example.event_management_api.data.model.Event

class EventAdapter(private var eventList: List<Event>) :
    RecyclerView.Adapter<EventAdapter.EventViewHolder>() {

    class EventViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val tvTitle: TextView = view.findViewById(R.id.tvEventTitle)
        val tvLocation: TextView = view.findViewById(R.id.tvEventLocation)
        val tvDate: TextView = view.findViewById(R.id.tvEventDate)
        val tvStatus: TextView = view.findViewById(R.id.tvEventStatus)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): EventViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_event, parent, false)
        return EventViewHolder(view)
    }

    override fun onBindViewHolder(holder: EventViewHolder, position: Int) {
        val event = eventList[position]
        holder.tvTitle.text = event.title
        holder.tvLocation.text = "📍 ${event.location}"
        holder.tvDate.text = "📅 ${event.date} | ⏰ ${event.time}"
        holder.tvStatus.text = event.status.uppercase()

        // Atur warna status (Mengacu ke colors.xml)
        val statusColorRes = when (event.status) {
            "upcoming" -> R.color.status_upcoming
            "ongoing" -> R.color.status_ongoing
            "completed" -> R.color.status_completed
            "cancelled" -> R.color.status_cancelled
            else -> R.color.black
        }
        holder.tvStatus.setTextColor(ContextCompat.getColor(holder.itemView.context, statusColorRes))

        // KRITIS: Tambahkan Listener untuk Use Case GET by ID (Anggota B)
        holder.itemView.setOnClickListener {
            // Logika untuk membuka EventDetailActivity
        }
    }

    override fun getItemCount(): Int = eventList.size

    fun updateData(newEvents: List<Event>) {
        eventList = newEvents
        notifyDataSetChanged()
    }
}