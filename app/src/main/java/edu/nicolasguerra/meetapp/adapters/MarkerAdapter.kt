package edu.nicolasguerra.meetapp.ui.fragments

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import edu.nicolasguerra.meetapp.R
import edu.nicolasguerra.meetapp.models.dbModel.MarkerEntity

class MarkerAdapter(
    var markers: List<MarkerEntity>,
    private val listener: ItemClickListener
) : RecyclerView.Adapter<MarkerAdapter.MarkerViewHolder>() {

    interface ItemClickListener {
        fun onItemClick(view: View, position: Int)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MarkerViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.list_item_marker, parent, false)
        return MarkerViewHolder(view)
    }

    override fun onBindViewHolder(holder: MarkerViewHolder, position: Int) {
        val marker = markers[position]
        holder.bind(marker)
    }

    override fun getItemCount(): Int {
        return markers.size
    }

    inner class MarkerViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val textViewTitle: TextView = itemView.findViewById(R.id.textViewTitle)

        init {
            itemView.setOnClickListener {
                val position = adapterPosition
                if (position != RecyclerView.NO_POSITION) {
                    listener.onItemClick(it, position)
                }
            }
        }

        fun bind(marker: MarkerEntity) {
            textViewTitle.text = marker.description
        }
    }

    fun submitList(newMarkers: List<MarkerEntity>) {
        markers = newMarkers
        notifyDataSetChanged()
    }
}
