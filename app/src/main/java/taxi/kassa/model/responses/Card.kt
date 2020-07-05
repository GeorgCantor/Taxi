package taxi.kassa.model.responses

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

data class Card(
    @SerializedName("id")
    @Expose
    var id: String? = null,
    @SerializedName("card_number")
    @Expose
    var number: String? = null,
    @SerializedName("created")
    @Expose
    var created: String? = null
)