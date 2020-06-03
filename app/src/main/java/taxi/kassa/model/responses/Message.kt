package taxi.kassa.model.responses

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

data class Message(
    @SerializedName("id")
    @Expose
    var id: String? = null,
    @SerializedName("text")
    @Expose
    var text: String? = null,
    @SerializedName("status")
    @Expose
    var status: String? = null,
    @SerializedName("side")
    @Expose
    var side: String? = null,
    @SerializedName("created")
    @Expose
    var created: String? = null
)