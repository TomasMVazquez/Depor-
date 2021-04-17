package com.applications.toms.depormas.ui.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.applications.toms.depormas.domain.Sport
import com.applications.toms.depormas.databinding.RecyclerSportItemBinding

class SportAdapter(private val clickListener: SportListener):
    ListAdapter<Sport, SportAdapter.ViewHolder>(ClassDiffCallback()) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder.from(
            parent
        )
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val item = getItem(position)
        holder.bind(item!!,clickListener)
    }

    class ViewHolder private constructor(val binding: RecyclerSportItemBinding): RecyclerView.ViewHolder(binding.root){

        fun bind(item: Sport, clickListener: SportListener) {
            val res = itemView.context
            binding.clickListener = clickListener
            binding.sport = item
            binding.sportTitle.text = item.name?.replace("_","\n")
            binding.sportImg.setImageResource(item.getDrawableInt(res))
            binding.executePendingBindings()
        }

        companion object {
            fun from(parent: ViewGroup): ViewHolder {
                val layoutInflater =  LayoutInflater.from(parent.context)
                val binding = RecyclerSportItemBinding.inflate(layoutInflater,parent,false)
                return ViewHolder(
                    binding
                )
            }
        }
    }

    class ClassDiffCallback : DiffUtil.ItemCallback<Sport>() {
        override fun areItemsTheSame(oldItem: Sport, newItem: Sport): Boolean {
            return oldItem.id == newItem.id
        }

        override fun areContentsTheSame(oldItem: Sport, newItem: Sport): Boolean {
            return oldItem == newItem
        }

    }
}

class SportListener(val clickListener: (sport: Sport) -> Unit){
    fun onClick(sport: Sport) = clickListener(sport)
}