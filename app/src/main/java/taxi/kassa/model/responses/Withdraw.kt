package taxi.kassa.model.responses

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize
import taxi.kassa.util.Constants.APPROVED
import taxi.kassa.util.Constants.CANCELED
import taxi.kassa.util.Constants.NEW
import taxi.kassa.util.Constants.WRITTEN_OFF
import java.text.DateFormatSymbols
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
            val dv = date.toLong() * 1000
            val df = Date(dv)
            val dd = SimpleDateFormat("HH:mm")

            return dd.format(df)
        }

    fun getDate(): String {
        val dv = date.toLong() * 1000
        val df = Date(dv)
        val dd = SimpleDateFormat("dd MMMM yyyy", myDateFormatSymbols)

        return dd.format(df)
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
        val dv = date.toLong() * 1000
        val df = Date(dv)
        val dd = SimpleDateFormat("HH:mm, dd MMMM yyyy", myDateFormatSymbols)

        return dd.format(df)
    }

    companion object {
        private val myDateFormatSymbols: DateFormatSymbols =
            object : DateFormatSymbols() {
                override fun getMonths(): Array<String> {
                    return arrayOf(
                        "янв", "фев", "мар", "апр", "мая", "июн",
                        "июл", "авг", "сен", "окт", "нояб", "дек"
                    )
                }
            }
    }
}