package taxi.kassa.view.withdraws.withdraw_create.daily

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import kotlinx.android.synthetic.main.item_account.view.*
import taxi.kassa.R
import taxi.kassa.model.responses.Account
import taxi.kassa.util.Constants.ALFABANK
import taxi.kassa.util.Constants.BINBANK
import taxi.kassa.util.Constants.SBERBANK
import taxi.kassa.util.Constants.TINKOFF
import taxi.kassa.util.Constants.VTB_BANK

class AccountsAdapter(
    private val accounts: List<Account>,
    private val clickListener: (Account, View) -> Unit
) : RecyclerView.Adapter<AccountsAdapter.AccountsViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int) = AccountsViewHolder(
        LayoutInflater.from(parent.context).inflate(R.layout.item_account, parent, false)
    )

    override fun getItemCount() = accounts.size

    override fun onBindViewHolder(holder: AccountsViewHolder, position: Int) {
        val account = accounts[position]

        with(holder) {
            bankIcon.setImageResource(
                when {
                    account.bankName.contains(ALFABANK, true) -> R.drawable.ic_alfa
                    account.bankName.contains(BINBANK, true) -> R.drawable.ic_binbank
                    account.bankName.contains(VTB_BANK, true) -> R.drawable.ic_vtb
                    account.bankName.contains(SBERBANK, true) -> R.drawable.ic_sberbank
                    account.bankName.contains(TINKOFF, true) -> R.drawable.ic_tinkoff
                    else -> R.drawable.transparent
                }
            )
            bankName.text = account.bankName
            accountNumber.text = itemView.context.getString(R.string.order_format, account.accountNumber)
            name.text = account.driverName

            itemView.setOnClickListener { clickListener(account, itemView) }
        }
    }

    class AccountsViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val background: ImageView = view.account_background
        val bankIcon: ImageView = view.bank_icon
        val bankName: TextView = view.bank_name_tv
        val accountNumber: TextView = view.account_number
        val name: TextView = view.name_tv
    }
}