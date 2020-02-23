package taxi.kassa.model.responses

import com.google.gson.annotations.SerializedName

class AccountsList {

    @SerializedName("info")
    val info: List<Account>? = null

    val count: Int?
        get() = info?.size
}