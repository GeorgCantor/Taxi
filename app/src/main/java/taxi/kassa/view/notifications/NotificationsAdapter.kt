package taxi.kassa.view.notifications

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import kotlinx.android.synthetic.main.item_notification.view.*
import taxi.kassa.R
import taxi.kassa.model.Notification

class NotificationsAdapter(notifications: MutableList<Notification>) :
    RecyclerView.Adapter<NotificationsAdapter.NotificationsViewHolder>() {

    private val notifications = mutableListOf<Notification>()

    init {
        this.notifications.addAll(notifications)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): NotificationsViewHolder {
        return NotificationsViewHolder(
            LayoutInflater.from(parent.context).inflate(R.layout.item_notification, parent, false)
        )
    }

    override fun getItemCount(): Int = notifications.size

    override fun onBindViewHolder(holder: NotificationsViewHolder, position: Int) {
        val notification = notifications[position]

        holder.title.text = notification.title
        holder.message.text = notification.message
        holder.date.text = notification.date
    }

    class NotificationsViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val title: TextView = view.push_title
        val message: TextView = view.push_message
        val date: TextView = view.date_tv
    }
}