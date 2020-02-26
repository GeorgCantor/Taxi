package taxi.kassa.view.orders

import android.os.Build
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.annotation.RequiresApi
import androidx.recyclerview.widget.RecyclerView
import kotlinx.android.synthetic.main.item_taxi_orders.view.*
import taxi.kassa.R
import taxi.kassa.model.Taxi

class OrdersTaxiAdapter(
    taxis: MutableList<Taxi>,
    private val clickListener: (View, Taxi) -> Unit
) : RecyclerView.Adapter<OrdersTaxiAdapter.OrdersTaxiViewHolder>() {

    private val taxis = mutableListOf<Taxi>()

    init {
        this.taxis.addAll(taxis)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): OrdersTaxiViewHolder {
        return OrdersTaxiViewHolder(
            LayoutInflater.from(parent.context).inflate(
                R.layout.item_taxi_orders, null
            )
        )
    }

    override fun getItemCount(): Int = taxis.size

    @RequiresApi(Build.VERSION_CODES.LOLLIPOP)
    override fun onBindViewHolder(holder: OrdersTaxiViewHolder, position: Int) {
        val taxi = taxis[position]
        holder.icon.background = holder.itemView.context.getDrawable(taxi.iconResource)
        holder.taxiName.text = taxi.taxiName

        holder.itemView.setOnClickListener { clickListener(holder.itemView, taxi) }
    }

    class OrdersTaxiViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val icon: ImageView = view.taxi_icon
        val taxiName: TextView = view.taxi_name
    }
}