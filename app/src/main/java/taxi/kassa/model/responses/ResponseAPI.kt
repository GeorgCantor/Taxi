package taxi.kassa.model.responses

import java.lang.Boolean
import java.util.*

class ResponseAPI<T> {
    private val error: ResponseError? = null

    val success = Boolean.parseBoolean(null)

    val response: T? = null

    val errorMsg: String?
        get() = error?.errorMsg

    val errorData: HashMap<String, String>?
        get() = error?.errorData
}