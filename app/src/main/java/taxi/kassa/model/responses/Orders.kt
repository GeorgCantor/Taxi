package taxi.kassa.model.responses

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

class Orders {
    @SerializedName("info")
    @Expose
    var orders: List<Order>? = null

    @SerializedName("next_offset")
    @Expose
    var nextOffset: String? = null
}