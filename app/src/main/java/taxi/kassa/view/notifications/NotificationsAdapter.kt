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
import taxi.kassa.util.invisible

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
        val constraintSet = ConstraintSet()

        holder.title.text = notification.title
        holder.message.text = notification.message
        holder.date.text = notification.date

        when (notification.isNew) {
            true -> holder.statusImage.setImageResource(R.drawable.ic_yellow_circle)
            false -> {
                holder.statusImage.invisible()
                constraintSet.clone(holder.parent)
                constraintSet.connect(
                    R.id.push_title,
                    ConstraintSet.START,
                    R.id.push_message,
                    ConstraintSet.START
                )
                constraintSet.applyTo(holder.parent)
            }
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