package taxi.kassa.view.support.chat

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import kotlinx.android.synthetic.main.item_incoming_message.view.*
import kotlinx.android.synthetic.main.item_sent_message.view.*
import taxi.kassa.R
import taxi.kassa.model.Message

class ChatHistoryAdapter(messages: MutableList<Message>) :
    RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    companion object {
        private const val TYPE_SENT = 0
        private const val TYPE_INCOMING = 1
    }

    private val messages = mutableListOf<Message>()

    init {
        this.messages.addAll(messages)
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

        when(holder) {
            is SentViewHolder -> {
                holder.topic.text = message.topic
                holder.sentMessage.text = message.message
                holder.sentDate.text = message.date
            }
            is IncomingViewHolder -> {
                holder.message.text = message.message
                holder.date.text = message.date
            }
        }
    }

    override fun getItemViewType(position: Int): Int {
        return when (messages[position].isIncoming) {
            true -> TYPE_INCOMING
            false -> TYPE_SENT
        }
    }

    class SentViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val topic: TextView = view.topic_title
        val sentMessage: TextView = view.sent_message_tv
        val sentDate: TextView = view.sent_date_time_tv
    }

    class IncomingViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val message: TextView = view.message_tv
        val date: TextView = view.date_time_tv
    }
}