package taxi.kassa.view.withdraws

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import kotlinx.android.synthetic.main.item_withdrawal.view.*
import taxi.kassa.R
import taxi.kassa.model.responses.Withdraw

class WithdrawsAdapter(withdraws: MutableList<Withdraw>) :
    RecyclerView.Adapter<WithdrawsAdapter.WithdrawsViewHolder>() {

    private val withdraws = mutableListOf<Withdraw>()

    init {
        this.withdraws.addAll(withdraws)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int) = WithdrawsViewHolder(
        LayoutInflater.from(parent.context).inflate(R.layout.item_withdrawal, null)
    )

    override fun getItemCount(): Int = withdraws.size

    override fun onBindViewHolder(holder: WithdrawsViewHolder, position: Int) {
        val withdraw = withdraws[position]
        val status = withdraw.status

        holder.time.text = withdraw.date.toString()
        holder.amount.text = withdraw.amount
        holder.status.text = status

        when (status) {
            "Одобрено" -> holder.statusImage.setImageResource(R.drawable.ic_yellow_circle)
            "Списано" -> holder.statusImage.setImageResource(R.drawable.ic_green_circle)
            "Оменено" -> holder.statusImage.setImageResource(R.drawable.ic_red_circle)
        }
    }

    class WithdrawsViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val time: TextView = view.time_tv
        val amount: TextView = view.amount_tv
        val status: TextView = view.status_tv
        val statusImage: ImageView = view.circle_image
    }
}