package taxi.kassa.model.responses

import com.google.gson.annotations.SerializedName

class Withdraws {

    @SerializedName("info")
    val info: MutableList<Withdraw>? = null

    val count: Int
        get() = info!!.size
}