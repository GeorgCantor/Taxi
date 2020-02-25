package taxi.kassa.view.withdraw_create

import android.os.Build
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.annotation.RequiresApi
import androidx.recyclerview.widget.RecyclerView
import kotlinx.android.synthetic.main.item_taxi.view.*
import taxi.kassa.R
import taxi.kassa.model.Taxi

class WithdrawTaxiAdapter(
    taxis: MutableList<Taxi>,
    private val clickListener: (View) -> Unit
) : RecyclerView.Adapter<WithdrawTaxiAdapter.WithdrawTaxiViewHolder>() {

    private val taxis = mutableListOf<Taxi>()

    init {
        this.taxis.addAll(taxis)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): WithdrawTaxiViewHolder {
        return WithdrawTaxiViewHolder(
            LayoutInflater.from(parent.context).inflate(
                R.layout.item_taxi, null
            )
        )
    }

    override fun getItemCount(): Int = taxis.size

    @RequiresApi(Build.VERSION_CODES.LOLLIPOP)
    override fun onBindViewHolder(holder: WithdrawTaxiViewHolder, position: Int) {
        val taxi = taxis[position]
        holder.icon.background = holder.itemView.context.getDrawable(taxi.iconResource)
        holder.taxiName.text = taxi.taxiName
        holder.amount.text = taxi.amount

        holder.itemView.setOnClickListener { clickListener(holder.itemView) }
    }

    class WithdrawTaxiViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val icon: ImageView = view.taxi_icon
        val taxiName: TextView = view.taxi_name
        val amount = view.amount_tv
    }
}