package taxi.kassa.model.responses

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName
import java.text.DateFormatSymbols
import java.text.SimpleDateFormat
import java.util.*

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
) {

    val date: String
        get() {
            val dv = (created?.toInt()?.toLong() ?: 0) * 1000
            val df = Date(dv)
            val dd = SimpleDateFormat("dd.MM.yy")

            return dd.format(df)
        }

    val hours: String
        get() {
            val dv = (created?.toInt()?.toLong() ?: 0) * 1000
            val df = Date(dv)
            val dd = SimpleDateFormat("HH:mm")

            return dd.format(df)
        }

    fun getDateForTitle(): String {
        val dv = (created?.toInt()?.toLong() ?: 0) * 1000
        val df = Date(dv)
        val dd = SimpleDateFormat("dd MMMM yyyy", myDateFormatSymbols)

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