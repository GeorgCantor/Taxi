package taxi.kassa.view.notifications

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.constraintlayout.widget.ConstraintSet
import androidx.recyclerview.widget.RecyclerView
import kotlinx.android.synthetic.main.item_notification.view.*
import taxi.kassa.R
import taxi.kassa.model.Notification
import taxi.kassa.util.changeConstraint
import taxi.kassa.util.invisible

class NotificationsAdapter(
    notifications: MutableList<Notification>,
    private val clickListener: (Notification) -> Unit
) : RecyclerView.Adapter<NotificationsAdapter.NotificationsViewHolder>() {

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

        with(holder) {
            title.text = notification.title
            message.text = notification.message
            date.text = notification.date

            when (notification.isNew) {
                true -> statusImage.setImageResource(R.drawable.ic_yellow_circle)
                false -> {
                    statusImage.invisible()
                    changeConstraint(
                        parent,
                        R.id.push_title,
                        ConstraintSet.START,
                        R.id.push_message,
                        ConstraintSet.START,
                        0
                    )
                }
            }

            itemView.setOnClickListener { clickListener(notification) }
        }
    }

    class NotificationsViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val title: TextView = view.push_title
        val message: TextView = view.push_message
        val date: TextView = view.date_tv
        val statusImage: ImageView = view.status_image
        val parent: ConstraintLayout = view.parent_layout
    }
}