package taxi.kassa.model.responses

import android.os.Parcelable
import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName
import kotlinx.android.parcel.Parcelize
import taxi.kassa.util.Constants.DAY_YEAR_PATTERN
import taxi.kassa.util.Constants.FULL_PATTERN
import taxi.kassa.util.Constants.HOURS_PATTERN
import taxi.kassa.util.Constants.myDateFormatSymbols
import java.text.SimpleDateFormat
import java.util.*

@Parcelize
data class Order(
    @SerializedName("id")
    @Expose
    var id: String? = null,
    @SerializedName("status")
    @Expose
    var status: String? = null,
    @SerializedName("source_id")
    @Expose
    var sourceId: String? = null,
    @SerializedName("address_from")
    @Expose
    var addressFrom: String? = null,
    @SerializedName("address_to")
    @Expose
    var addressTo: String? = null,
    @SerializedName("amount_client")
    @Expose
    var amountClient: String? = null,
    @SerializedName("amount_driver")
    @Expose
    var amountDriver: String? = null,
    @SerializedName("amount_tip")
    @Expose
    var amountTip: String? = null,
    @SerializedName("amount_fee_agr")
    @Expose
    var amountFeeAgr: String? = null,
    @SerializedName("amount_fee_our")
    @Expose
    var amountFeeOur: String? = null,
    @SerializedName("amount_total")
    @Expose
    var amountTotal: Float? = null,
    @SerializedName("created")
    @Expose
    var created: String? = null,
    @SerializedName("statunt_total")
    @Expose
    var statuntTotal: Float? = null,
    @SerializedName("ant_tip")
    @Expose
    var antTip: String? = null
) : Parcelable {

    val date: String
        get() {
            val longDate = (created?.toInt()?.toLong() ?: 0) * 1000
            val date = Date(longDate)
            val dateFormat = SimpleDateFormat("dd.MM.yy")
            dateFormat.timeZone = TimeZone.getDefault()

            return dateFormat.format(date)
        }

    val hours: String
        get() {
            val longDate = (created?.toInt()?.toLong() ?: 0) * 1000
            val date = Date(longDate)
            val dateFormat = SimpleDateFormat(HOURS_PATTERN)
            dateFormat.timeZone = TimeZone.getDefault()

            return dateFormat.format(date)
        }

    fun getDateForTitle(): String {
        val longDate = (created?.toInt()?.toLong() ?: 0) * 1000
        val date = Date(longDate)
        val dateFormat = SimpleDateFormat(DAY_YEAR_PATTERN, myDateFormatSymbols)
        dateFormat.timeZone = TimeZone.getDefault()

        return dateFormat.format(date)
    }

    fun getDateWithTime(): String {
        val longDate = (created?.toInt()?.toLong() ?: 0) * 1000
        val date = Date(longDate)
        val dateFormat = SimpleDateFormat(FULL_PATTERN, myDateFormatSymbols)
        dateFormat.timeZone = TimeZone.getDefault()

        return dateFormat.format(date)
    }
}