package taxi.kassa.view.withdraws.withdraw_create

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.content.res.AppCompatResources.getDrawable
import androidx.recyclerview.widget.RecyclerView
import kotlinx.android.synthetic.main.item_card.view.*
import taxi.kassa.R
import taxi.kassa.model.Card
import taxi.kassa.util.Constants.MASTERCARD
import taxi.kassa.util.Constants.VISA
import taxi.kassa.util.getCardType

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

    override fun onBindViewHolder(holder: WithdrawCardViewHolder, position: Int) {
        val card = cards[position]
        val formattedNumber = "**** ${card.number.substring(12)}"
        holder.cardNumber.text = formattedNumber

        when (card.number.getCardType()) {
            MASTERCARD -> holder.cardIcon.background = getDrawable(holder.itemView.context, R.drawable.ic_mastrcard_bg)
            VISA -> holder.cardIcon.background = getDrawable(holder.itemView.context, R.drawable.ic_visa)
        }

        holder.itemView.setOnClickListener {
            clickListener(holder.itemView)
        }
    }

    class WithdrawCardViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val cardNumber: TextView = view.card_number
        val cardIcon: ImageView = view.card_icon
    }
}