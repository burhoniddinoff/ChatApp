package com.example.chatapp.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.chatapp.databinding.FromLayoutBinding
import com.example.chatapp.databinding.ToLayoutBinding
import com.example.chatapp.model.Message

class MessageAdapter(
    private val uid: String
) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    var messageList = mutableListOf<Message>()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return if (viewType == 2) {
            FromViewHolder(
                FromLayoutBinding.inflate(LayoutInflater.from(parent.context), parent, false)
            )
        } else {
            ToViewHolder(
                ToLayoutBinding.inflate(LayoutInflater.from(parent.context), parent, false)
            )
        }
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        val message = messageList[position]

        if (holder is FromViewHolder) {
            holder.bind(message)
        } else if (holder is ToViewHolder) {
            holder.bind(message)
        }
    }

    override fun getItemCount(): Int = messageList.size


    override fun getItemViewType(position: Int): Int {
        val currentSentMessage = messageList[position]
        return if (uid == currentSentMessage.senderId) {
            2
        } else {
            1
        }
    }

    inner class FromViewHolder(private val binding: FromLayoutBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(message: Message) {
            binding.textView.text = message.text
            binding.textTime.text = message.time
        }
    }

    inner class ToViewHolder(private val binding: ToLayoutBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(message: Message) {
            binding.textView.text = message.text
            binding.textTime.text = message.time
        }
    }
}