package taxi.kassa.model.responses

import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import kotlinx.android.parcel.Parcelize
import taxi.kassa.util.Constants.DAY_YEAR_PATTERN
import taxi.kassa.util.Constants.FULL_PATTERN
import taxi.kassa.util.Constants.HOURS_PATTERN
import taxi.kassa.util.Constants.myDateFormatSymbols
import java.text.SimpleDateFormat
import java.util.*

@Parcelize
class Withdraw(
    @SerializedName("source_id")
    val sourceId: String,
    @SerializedName("amount")
    val amount: String,
    @SerializedName("amount_fee")
    val amountFee: String,
    @SerializedName("date")
    val date: Int,
    @SerializedName("status")
    val status: String
) : Parcelable {

    val hours: String
        get() {
            val longDate = date.toLong() * 1000
            val date = Date(longDate)
            val dateFormat = SimpleDateFormat(HOURS_PATTERN)

            return dateFormat.format(date)
        }

    fun getDate(): String {
        val longDate = date.toLong() * 1000
        val date = Date(longDate)
        val dateFormat = SimpleDateFormat(DAY_YEAR_PATTERN, myDateFormatSymbols)

        return dateFormat.format(date)
    }

    fun getWithdrawalDate(): String {
        val longDate = date.toLong() * 1000
        val date = Date(longDate)
        val dateFormat = SimpleDateFormat(FULL_PATTERN, myDateFormatSymbols)

        return dateFormat.format(date)
    }
}