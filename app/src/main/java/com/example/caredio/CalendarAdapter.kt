package com.example.caredio

import android.graphics.Color
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView

class CalendarAdapter(private val days: List<String>, private val onItemClick: (String) -> Unit) :
    RecyclerView.Adapter<CalendarAdapter.CalendarViewHolder>() {

    private var selectedPosition = -1 // Variable pour suivre le jour sélectionné

    class CalendarViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val textView: TextView = itemView.findViewById(R.id.calendar_day_text)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CalendarViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_calendar_day, parent, false)
        return CalendarViewHolder(view)
    }

    override fun onBindViewHolder(holder: CalendarViewHolder, position: Int) {
        holder.textView.text = days[position]

        val adapterPosition = holder.adapterPosition
        if (adapterPosition == selectedPosition) {
            holder.textView.setBackgroundResource(R.drawable.selected_day_background)
            holder.textView.setTextColor(Color.WHITE)
        } else {
            holder.textView.setBackgroundResource(android.R.color.transparent)
            holder.textView.setTextColor(Color.BLACK)
        }

        holder.itemView.setOnClickListener {
            val previousPosition = selectedPosition
            selectedPosition = holder.adapterPosition
            notifyItemChanged(previousPosition)
            notifyItemChanged(selectedPosition)
            onItemClick(days[selectedPosition])
        }
    }


    override fun getItemCount() = days.size
}
