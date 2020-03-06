package taxi.kassa.view.orders.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import kotlinx.android.synthetic.main.item_order.view.*
import kotlinx.android.synthetic.main.item_withdraw_date.view.*
import taxi.kassa.R
import taxi.kassa.model.responses.Order

class OrdersAdapter(
    orders: MutableList<Order>,
    private val clickListener: (Order) -> Unit
) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    companion object {
        private const val TYPE_ITEM = 0
        private const val TYPE_DATE = 1
        private const val DATE_ITEM_ID = "777"
    }

    private val orders = mutableListOf<Order>()
    private val dates = mutableSetOf<Order>()
    private var lastDate = ""

    init {
        this.orders.addAll(orders)

        if (this.orders.isNotEmpty()) {
            lastDate = this.orders[0].date

            dates.add(
                Order(
                    DATE_ITEM_ID,
                    "",
                    "",
                    "",
                    "",
                    "",
                    "",
                    "",
                    "",
                    "",
                    0F,
                    this.orders[0].created
                )
            )

            this.orders.map {
                if (it.date != lastDate) {
                    dates.add(
                        Order(
                            DATE_ITEM_ID,
                            "",
                            "",
                            "",
                            "",
                            "",
                            "",
                            "",
                            "",
                            "",
                            0F,
                            it.created
                        )
                    )
                    lastDate = it.date
                }
            }

            this.orders.addAll(dates)
            this.orders.sortBy { it.created }
            this.orders.reverse()
        }
    }

    fun updateList(orders: MutableList<Order>) {
        orders.map {
            if (it.date != lastDate) {
                dates.add(
                    Order(
                        DATE_ITEM_ID,
                        "",
                        "",
                        "",
                        "",
                        "",
                        "",
                        "",
                        "",
                        "",
                        0F,
                        it.created
                    )
                )
                lastDate = it.date
            }
        }

        this.orders.addAll(orders)
        this.orders.addAll(dates)

        try {
            for (i in 0 until this.orders.size) {
                if (this.orders[i].id == DATE_ITEM_ID) {
                    if (this.orders[i].date == this.orders[i + 1].date) {
                        this.orders.remove(this.orders[i])
                    }
                }
            }
        } catch (e: IndexOutOfBoundsException) {
        }

        this.orders.sortBy { it.created }
        this.orders.reverse()

        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return when (viewType) {
            TYPE_ITEM -> {
                OrdersViewHolder(
                    LayoutInflater.from(parent.context).inflate(R.layout.item_order, parent, false)
                )
            }
            TYPE_DATE -> {
                DateViewHolder(
                    LayoutInflater.from(parent.context).inflate(R.layout.item_withdraw_date, parent, false)
                )
            }
            else -> OrdersViewHolder(
                LayoutInflater.from(parent.context).inflate(R.layout.item_order, parent, false)
            )
        }
    }

    override fun getItemCount(): Int = orders.size

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        val order = orders[position]

        when (holder) {
            is OrdersViewHolder -> {
                holder.time.text = order.hours
                holder.addressFrom.text = order.addressFrom
                holder.addressTo.text = order.addressTo
                holder.totalAmount.text = holder.itemView.context.getString(
                    R.string.balance_format,
                    order.amountTotal.toString()
                )

                when (order.status) {
                    "0" -> {
                        holder.statusImage.setImageResource(R.drawable.ic_green_circle)
                        holder.status.text = holder.itemView.context.getString(R.string.complete)
                    }
                    "-1" -> {
                        holder.statusImage.setImageResource(R.drawable.ic_red_circle)
                        holder.status.text = holder.itemView.context.getString(R.string.canceled)
                    }
                }

                holder.itemView.setOnClickListener { clickListener(order) }
            }
            is DateViewHolder -> {
                holder.date.text = order.getDateForTitle()
            }
        }
    }

    override fun getItemViewType(position: Int): Int {
        return if (orders[position].id == DATE_ITEM_ID) {
            TYPE_DATE
        } else {
            TYPE_ITEM
        }
    }

    class OrdersViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val time: TextView = view.time_tv
        val statusImage: ImageView = view.circle_image
        val status: TextView = view.status_tv
        val addressFrom: TextView = view.address_from
        val addressTo: TextView = view.address_to
        val totalAmount: TextView = view.total_amount
    }

    class DateViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val date: TextView = view.date
    }
}