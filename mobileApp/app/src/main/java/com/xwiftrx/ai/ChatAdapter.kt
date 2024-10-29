package com.xwiftrx.ai

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.xwiftrx.ai.databinding.ItemChatMessageBinding

data class ChatMessage(val message: String, val isUser: Boolean)

class ChatAdapter(private val messages: List<ChatMessage>) : RecyclerView.Adapter<ChatAdapter.ChatViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ChatViewHolder {
        val binding = ItemChatMessageBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ChatViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ChatViewHolder, position: Int) {
        val message = messages[position]
        holder.bind(message)
    }

    override fun getItemCount(): Int {
        return messages.size
    }

    class ChatViewHolder(private val binding: ItemChatMessageBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bind(message: ChatMessage) {
            if (message.isUser) {
                binding.userMessageTextView.text = message.message
                binding.userMessageContainer.visibility = View.VISIBLE
                binding.aiMessageContainer.visibility = View.GONE
            } else {
                binding.aiMessageTextView.text = message.message
                binding.aiMessageContainer.visibility = View.VISIBLE
                binding.userMessageContainer.visibility = View.GONE
            }
        }
    }
}
