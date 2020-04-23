package taxi.kassa.view.accounts

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.content.res.AppCompatResources.getDrawable
import androidx.recyclerview.widget.RecyclerView
import kotlinx.android.synthetic.main.item_accounts_card.view.*
import taxi.kassa.R
import taxi.kassa.model.Card
import taxi.kassa.util.Constants.MASTERCARD
import taxi.kassa.util.Constants.VISA
import taxi.kassa.util.getCardType

class AccountsCardsAdapter(cards: MutableList<Card>) :
    RecyclerView.Adapter<AccountsCardsAdapter.AccountsCardViewHolder>() {

    private val cards = mutableListOf<Card>()

    init {
        this.cards.addAll(cards)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): AccountsCardViewHolder {
        return AccountsCardViewHolder(
            LayoutInflater.from(parent.context).inflate(R.layout.item_accounts_card, parent, false)
        )
    }

    override fun getItemCount(): Int = cards.size

    override fun onBindViewHolder(holder: AccountsCardViewHolder, position: Int) {
        val card = cards[position]
        val formattedNumber = "**** ${card.number.substring(12)}"
        holder.cardNumber.text = formattedNumber

        when (card.number.getCardType()) {
            MASTERCARD -> holder.cardIcon.background = getDrawable(holder.itemView.context, R.drawable.ic_mastrcard_bg)
            VISA -> holder.cardIcon.background = getDrawable(holder.itemView.context, R.drawable.ic_visa)
        }
    }

    class AccountsCardViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val cardNumber: TextView = view.card_number
        val cardIcon: ImageView = view.card_icon
    }
}