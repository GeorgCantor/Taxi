package taxi.kassa.view.withdraws.withdraw_create

import android.os.Build
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.annotation.RequiresApi
import androidx.recyclerview.widget.RecyclerView
import kotlinx.android.synthetic.main.item_card.view.*
import taxi.kassa.R
import taxi.kassa.model.Card

class WithdrawCardsAdapter(
    cards: MutableList<Card>,
    private val clickListener: (View) -> Unit
) : RecyclerView.Adapter<WithdrawCardsAdapter.WithdrawCardViewHolder>() {

    private val cards = mutableListOf<Card>()

    init {
        this.cards.addAll(cards)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): WithdrawCardViewHolder {
        return WithdrawCardViewHolder(
            LayoutInflater.from(parent.context).inflate(R.layout.item_card, parent, false)
        )
    }

    override fun getItemCount(): Int = cards.size

    @RequiresApi(Build.VERSION_CODES.LOLLIPOP)
    override fun onBindViewHolder(holder: WithdrawCardViewHolder, position: Int) {
        val card = cards[position]
        val formattedNumber = "**** ${card.number.substring(12)}"
        holder.cardNumber.text = formattedNumber
        holder.cardIcon.background = holder.itemView.context.getDrawable(card.iconResource)

        holder.itemView.setOnClickListener {
            clickListener(holder.itemView)
        }
    }

    class WithdrawCardViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val cardNumber: TextView = view.card_number
        val cardIcon: ImageView = view.card_icon
    }
}