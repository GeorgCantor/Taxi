package taxi.kassa.repository

import io.reactivex.Observable
import taxi.kassa.model.remote.ApiService
import taxi.kassa.model.responses.ResponseAPI
import taxi.kassa.model.responses.ResponseAuthSendPhone
import taxi.kassa.model.responses.ResponseCreateRequest

class ApiRepository(private val apiService: ApiService) {

    fun login(phone: String): Observable<ResponseAPI<ResponseAuthSendPhone?>?>? = apiService.authSendPhone(phone)

    fun signUp(
        name: String,
        phone: String,
        source_id: Int,
        key: String
    ): Observable<ResponseAPI<ResponseCreateRequest?>?>? = apiService.createRequest(name, phone, source_id, key)
}