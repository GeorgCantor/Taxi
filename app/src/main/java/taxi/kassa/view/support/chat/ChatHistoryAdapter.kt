package taxi.kassa.view.support.chat

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import kotlinx.android.synthetic.main.item_incoming_message.view.*
import kotlinx.android.synthetic.main.item_sent_message.view.*
import taxi.kassa.R
import taxi.kassa.model.responses.Message
import taxi.kassa.util.Constants.ADMIN
import taxi.kassa.util.Constants.DRIVER
import taxi.kassa.util.Constants.FULL_PATTERN
import taxi.kassa.util.convertToTime

class ChatHistoryAdapter(messages: MutableList<Message>) :
    ListAdapter<Message, RecyclerView.ViewHolder>(DiffCallback) {

    companion object {
        object DiffCallback : DiffUtil.ItemCallback<Message>() {
            override fun areItemsTheSame(oldItem: Message, newItem: Message) = oldItem == newItem

            override fun areContentsTheSame(old: Message, new: Message) = old.id == new.id
        }

        private const val TYPE_SENT = 0
        private const val TYPE_INCOMING = 1
    }

    private val messages = mutableListOf<Message>()

    init {
        this.messages.addAll(messages)
    }

    fun updateList(messages: MutableList<Message>) {
        this.messages.addAll(messages)
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return when (viewType) {
            TYPE_SENT -> {
                SentViewHolder(
                    LayoutInflater.from(parent.context).inflate(
                        R.layout.item_sent_message,
                        parent,
                        false
                    )
                )
            }
            TYPE_INCOMING -> {
                IncomingViewHolder(
                    LayoutInflater.from(parent.context).inflate(
                        R.layout.item_incoming_message,
                        parent,
                        false
                    )
                )
            }
            else -> SentViewHolder(
                LayoutInflater.from(parent.context).inflate(
                    R.layout.item_withdraw,
                    parent,
                    false
                )
            )
        }
    }

    override fun getItemCount(): Int = this.messages.size

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        val message = messages[position]
        val longDate = (message.created?.toInt()?.toLong() ?: 0) * 1000

        when (holder) {
            is SentViewHolder -> {
//                holder.topic.text = message.text
                holder.sentMessage.text = message.text
                holder.sentDate.text = longDate.convertToTime(FULL_PATTERN)
            }
            is IncomingViewHolder -> {
                holder.message.text = message.text
                holder.date.text = longDate.convertToTime(FULL_PATTERN)
            }
        }
    }

    override fun getItemViewType(position: Int): Int {
        return when (messages[position].side) {
            ADMIN -> TYPE_INCOMING
            DRIVER -> TYPE_SENT
            else -> TYPE_INCOMING
        }
    }

    class SentViewHolder(view: View) : RecyclerView.ViewHolder(view) {
//        val topic: TextView = view.topic_title
        val sentMessage: TextView = view.sent_message_tv
        val sentDate: TextView = view.sent_date_time_tv
    }

    class IncomingViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val message: TextView = view.message_tv
        val date: TextView = view.date_time_tv
    }
}