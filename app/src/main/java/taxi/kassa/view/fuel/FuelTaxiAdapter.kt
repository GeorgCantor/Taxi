package taxi.kassa.view.fuel

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.content.res.AppCompatResources.getDrawable
import androidx.recyclerview.widget.RecyclerView
import kotlinx.android.synthetic.main.item_taxi.view.*
import taxi.kassa.R
import taxi.kassa.model.Taxi

class FuelTaxiAdapter(
    private val taxis: MutableList<Taxi>,
    private val clickListener: (View) -> Unit
) : RecyclerView.Adapter<FuelTaxiAdapter.FuelTaxiViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int) = FuelTaxiViewHolder(
        LayoutInflater.from(parent.context).inflate(R.layout.item_taxi, null)
    )

    override fun getItemCount(): Int = taxis.size

    override fun onBindViewHolder(holder: FuelTaxiViewHolder, position: Int) {
        val taxi = taxis[position]

        with(holder) {
            icon.background = getDrawable(itemView.context, taxi.iconResource)
            taxiName.text = taxi.taxiName
            amount.text = taxi.amount

            itemView.setOnClickListener { clickListener(itemView) }
        }
    }

    class FuelTaxiViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val icon: ImageView = view.taxi_icon
        val taxiName: TextView = view.taxi_name
        val amount: TextView = view.amount_tv
    }
}