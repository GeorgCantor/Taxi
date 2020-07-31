package taxi.kassa.model.responses

import com.google.gson.annotations.SerializedName

class ResponseFuelBalance {
    @SerializedName("new_fuel_balance")
    var newFuelBalance: Float? = null
}