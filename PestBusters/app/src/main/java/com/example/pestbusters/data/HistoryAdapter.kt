package com.example.pestbusters.data

import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.util.Base64
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.pestbusters.R
import com.example.pestbusters.data.HistoryEntity

class HistoryAdapter(private val historyList: List<HistoryEntity>) :
    RecyclerView.Adapter<HistoryAdapter.HistoryViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): HistoryViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.history_home, parent, false)
        return HistoryViewHolder(view)
    }

    override fun onBindViewHolder(holder: HistoryViewHolder, position: Int) {
        val history = historyList[position]
        holder.bind(history)
    }

    override fun getItemCount(): Int = historyList.size

    inner class HistoryViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        private val imageView: ImageView = view.findViewById(R.id.iv_history_image)
        private val classLabelTextView: TextView = view.findViewById(R.id.tv_class_label)
        private val dateTextView: TextView = view.findViewById(R.id.tv_date)

        fun bind(history: HistoryEntity) {
            classLabelTextView.text = history.classLabel
            dateTextView.text = history.date
            imageView.setImageBitmap(base64ToBitmap(history.imageBase64))
        }

        private fun base64ToBitmap(base64String: String): Bitmap? {
            val decodedBytes = Base64.decode(base64String, Base64.DEFAULT)
            return BitmapFactory.decodeByteArray(decodedBytes, 0, decodedBytes.size)
        }
    }
}
