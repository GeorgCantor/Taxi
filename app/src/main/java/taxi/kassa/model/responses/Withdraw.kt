package taxi.kassa.model.responses

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize
import taxi.kassa.util.Constants.APPROVED
import taxi.kassa.util.Constants.CANCELED
import taxi.kassa.util.Constants.DAY_YEAR_PATTERN
import taxi.kassa.util.Constants.FULL_PATTERN
import taxi.kassa.util.Constants.HOURS_PATTERN
import taxi.kassa.util.Constants.NEW
import taxi.kassa.util.Constants.WRITTEN_OFF
import taxi.kassa.util.myDateFormatSymbols
import java.text.SimpleDateFormat
import java.util.*

@Parcelize
class Withdraw(
    val source_id: String,
    val amount: String,
    val date: Int,
    val status: Int
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

    fun getStatus(): String {
        val id = status
        var result = ""
        when (id) {
            0 -> result = NEW
            1 -> result = APPROVED
            2 -> result = WRITTEN_OFF
            -1 -> result = CANCELED
        }

        return result
    }

    fun getWithdrawalDate(): String {
        val longDate = date.toLong() * 1000
        val date = Date(longDate)
        val dateFormat = SimpleDateFormat(FULL_PATTERN, myDateFormatSymbols)

        return dateFormat.format(date)
    }
}