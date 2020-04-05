package taxi.kassa.view.orders.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.content.res.AppCompatResources.getDrawable
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
            LayoutInflater.from(parent.context).inflate(R.layout.item_taxi_orders, null)
        )
    }

    override fun getItemCount(): Int = taxis.size

    override fun onBindViewHolder(holder: OrdersTaxiViewHolder, position: Int) {
        val taxi = taxis[position]
        with(holder) {
            icon.background = getDrawable(itemView.context, taxi.iconResource)
            taxiName.text = taxi.taxiName

            itemView.setOnClickListener { clickListener(itemView, taxi) }
        }
    }

    class OrdersTaxiViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val icon: ImageView = view.taxi_icon
        val taxiName: TextView = view.taxi_name
    }
}