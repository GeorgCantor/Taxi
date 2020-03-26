package taxi.kassa.view.accounts

import android.os.Build
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.annotation.RequiresApi
import androidx.recyclerview.widget.RecyclerView
import kotlinx.android.synthetic.main.item_accounts_card.view.*
import taxi.kassa.R
import taxi.kassa.model.Card

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

    @RequiresApi(Build.VERSION_CODES.LOLLIPOP)
    override fun onBindViewHolder(holder: AccountsCardViewHolder, position: Int) {
        val card = cards[position]
        holder.cardNumber.text = card.number.toString()
        holder.cardIcon.background = holder.itemView.context.getDrawable(card.iconResource)
    }

    class AccountsCardViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val cardNumber: TextView = view.card_number
        val cardIcon: ImageView = view.card_icon
    }
}