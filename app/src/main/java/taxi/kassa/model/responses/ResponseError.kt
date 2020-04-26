package taxi.kassa.model.responses

import com.google.gson.annotations.SerializedName
import java.util.*

class ResponseError {
    @SerializedName("error_msg")
    val errorMsg: String? = null

    @SerializedName("error_data")
    val errorData: HashMap<String, String>? = null
}