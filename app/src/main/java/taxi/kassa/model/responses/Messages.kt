package taxi.kassa.model.responses

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

class Messages {
    @SerializedName("info")
    @Expose
    var messages: List<Message>? = null

    @SerializedName("next_offset")
    @Expose
    var nextOffset: String? = null
}