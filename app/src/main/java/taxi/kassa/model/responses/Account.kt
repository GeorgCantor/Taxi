package taxi.kassa.model.responses

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

class Account(
    val id: Int,
    @SerializedName("account_number")
    @Expose
    val accountNumber: String,
    @SerializedName("account_corr")
    @Expose
    val accountCorr: String,
    @SerializedName("bank_name")
    @Expose
    val bankName: String,
    @SerializedName("driver_name")
    @Expose
    val driverName: String,
    @SerializedName("card_number")
    @Expose
    val cardNumber: String,
    @SerializedName("date")
    @Expose
    val cardDate: String,
    @SerializedName("readonly")
    @Expose
    val readOnly: Boolean,
    @SerializedName("auto_pay")
    @Expose
    val autoPay: String
)