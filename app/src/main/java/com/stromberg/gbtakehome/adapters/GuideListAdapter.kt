package com.stromberg.gbtakehome.adapters

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.core.view.isVisible
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.stromberg.gbtakehome.R
import com.stromberg.gbtakehome.databinding.GuideListItemBinding
import com.stromberg.gbtakehome.models.local.Guide

class GuideListAdapter : ListAdapter<Guide, GuideListAdapter.ViewHolder>(DiffUtilItemCallback) {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(
            GuideListItemBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            ),
            parent.context,
        )
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        currentList[position].let { guide ->
            Glide
                .with(holder.context)
                .load(guide.icon)
                .placeholder(R.drawable.ic_icon_placeholder)
                .into(holder.icon)

            holder.name.text = guide.name
            holder.startAndEndDate.text = "${guide.startDate} - ${guide.endDate}"
            holder.url.text = guide.url
            holder.venue.text = guide.venue?.let { "${it.city}, ${it.city}" }
            holder.venue.isVisible = guide.venue?.city?.isNotEmpty() == true
                    && guide.venue?.state?.isNotEmpty() == true
        }
    }

    override fun getItemCount(): Int = currentList.size

    inner class ViewHolder(binding: GuideListItemBinding, val context: Context) : RecyclerView.ViewHolder(binding.root) {
        val icon: ImageView = binding.icon
        val name: TextView = binding.name
        val startAndEndDate: TextView = binding.startAndEndDate
        val url: TextView = binding.url
        val venue: TextView = binding.venue
    }

    object DiffUtilItemCallback : DiffUtil.ItemCallback<Guide>() {
        override fun areItemsTheSame(oldItem: Guide, newItem: Guide): Boolean =
            oldItem.url == newItem.url

        override fun areContentsTheSame(oldItem: Guide, newItem: Guide): Boolean =
            oldItem == newItem
    }
}