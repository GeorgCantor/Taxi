package taxi.kassa.repository

import io.reactivex.Observable
import taxi.kassa.model.remote.ApiService
import taxi.kassa.model.responses.*

class ApiRepository(private val apiService: ApiService) {

    fun login(phone: String): Observable<ResponseAPI<ResponseAuthSendPhone?>?>? = apiService.authSendPhone(phone)

    fun signUp(
        name: String,
        phone: String,
        source_id: Int,
        key: String
    ): Observable<ResponseAPI<ResponseCreateRequest?>?>? = apiService.createRequest(name, phone, source_id, key)

    fun getCode(
        phone: String,
        code: String
    ): Observable<ResponseAPI<ResponseAuthSendCode?>?>? = apiService.getCode(phone, code)

    fun getOwner(): Observable<ResponseAPI<ResponseOwner?>?>? = apiService.getOwner()

    fun getWithdraws(): Observable<ResponseAPI<Withdraws?>?>? = apiService.getWithdraws()
}