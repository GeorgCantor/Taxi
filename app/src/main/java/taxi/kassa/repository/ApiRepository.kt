package taxi.kassa.repository

import io.reactivex.Observable
import taxi.kassa.model.remote.ApiService
import taxi.kassa.model.responses.ResponseAPI
import taxi.kassa.model.responses.ResponseAuthSendPhone

class ApiRepository(private val apiService: ApiService) {

    fun login(phone: String): Observable<ResponseAPI<ResponseAuthSendPhone?>?>? = apiService.authSendPhone(phone)
}