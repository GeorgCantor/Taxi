package taxi.kassa.model.responses

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

data class ResponseOwner(
    @SerializedName("balance_city")
    @Expose
    var balanceCity: String,
    @SerializedName("balance_gett")
    @Expose
    var balanceGett: String,
    @SerializedName("balance_yandex")
    @Expose
    var balanceYandex: String,
    @SerializedName("balance_total")
    @Expose
    var balanceTotal: String,
    @SerializedName("balance_fuel")
    @Expose
    var balanceFuel: String,
    @SerializedName("first_name")
    @Expose
    var firstName: String,
    @SerializedName("last_name")
    @Expose
    var lastName: String,
    @SerializedName("phone")
    @Expose
    var phone: String
) {

    val fullName: String
        get() = "$lastName $firstName"
}