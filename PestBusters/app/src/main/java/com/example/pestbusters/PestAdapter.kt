package com.example.pestbusters

import android.content.Intent
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
import com.example.pestbusters.response.PestData

class PestAdapter(private val pestList: List<PestData>) :
    RecyclerView.Adapter<PestAdapter.PestViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PestViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.pestgallery_card, parent, false) // Use pestgallery_card as the layout
        return PestViewHolder(view)
    }

    override fun onBindViewHolder(holder: PestViewHolder, position: Int) {
        val pest = pestList[position]
        holder.bind(pest)
    }

    override fun getItemCount(): Int = pestList.size

    inner class PestViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        private val pestImage: ImageView = view.findViewById(R.id.iv_pest_image)
        private val pestName: TextView = view.findViewById(R.id.tv_pest_name)
        private val pestDescription: TextView = view.findViewById(R.id.tv_pest_description)

        fun bind(pest: PestData) {
            pestName.text = pest.Pest_name
            pestDescription.text = pest.Treatment

            // Decode Base64 string to Bitmap
            val bitmap = pest.Image?.let { base64ToBitmap(it) }
            if (bitmap != null) {
                pestImage.setImageBitmap(bitmap)
            } else {
                pestImage.setImageResource(R.drawable.ic_baseline_image_placeholder) // Placeholder for invalid images
            }

            itemView.setOnClickListener {
                val context = itemView.context
                val intent = Intent(context, DetailActivity::class.java).apply {
                    putExtra(DetailActivity.CLASS_LABEL, pest.Pest_name) // Pass pest name as class label
                }
                context.startActivity(intent)
            }
        }

        private fun base64ToBitmap(base64Str: String): Bitmap? {
            return try {
                val decodedBytes = Base64.decode(base64Str, Base64.DEFAULT)
                BitmapFactory.decodeByteArray(decodedBytes, 0, decodedBytes.size)
            } catch (e: IllegalArgumentException) {
                e.printStackTrace()
                null
            }
        }
    }
}
