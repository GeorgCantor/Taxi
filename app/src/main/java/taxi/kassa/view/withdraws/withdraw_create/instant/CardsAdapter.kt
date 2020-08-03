package taxi.kassa.view.withdraws.withdraw_create.instant

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.content.res.AppCompatResources.getDrawable
import androidx.recyclerview.widget.RecyclerView
import kotlinx.android.synthetic.main.item_cardd.view.*
import taxi.kassa.R
import taxi.kassa.model.responses.Card
import taxi.kassa.util.Constants.MASTERCARD
import taxi.kassa.util.Constants.VISA
import taxi.kassa.util.getCardType

class CardsAdapter(
    private val cards: List<Card>,
    private val clickListener: (Card, View) -> Unit
) : RecyclerView.Adapter<CardsAdapter.CardViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int) = CardViewHolder(
        LayoutInflater.from(parent.context).inflate(R.layout.item_cardd, parent, false)
    )

    override fun getItemCount() = cards.size

    override fun onBindViewHolder(holder: CardViewHolder, position: Int) {
        val card = cards[position]
        val formattedNumber = "**** ${card.number?.substring(12)}"

        with(holder) {
            cardNumber.text = formattedNumber

            when (card.number?.getCardType()) {
                MASTERCARD -> cardIcon.background = getDrawable(itemView.context, R.drawable.ic_mastrcard_bg)
                VISA -> cardIcon.background = getDrawable(itemView.context, R.drawable.ic_visa)
            }

            itemView.setOnClickListener { clickListener(card, itemView) }
        }
    }

    class CardViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val bankIcon: ImageView = view.bank_icon
        val cardNumber: TextView = view.card_number
        val cardIcon: ImageView = view.card_icon
    }
}