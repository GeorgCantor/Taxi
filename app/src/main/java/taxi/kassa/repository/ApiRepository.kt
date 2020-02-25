package taxi.kassa.repository

import taxi.kassa.model.remote.ApiService

class ApiRepository(private val apiService: ApiService) {

    fun login(phone: String) = apiService.authSendPhone(phone)

    fun signUp(
        name: String,
        phone: String,
        source_id: Int,
        key: String
    ) = apiService.createRequest(name, phone, source_id, key)

    fun getCode(
        phone: String,
        code: String
    ) = apiService.getCode(phone, code)

    fun getOwner() = apiService.getOwner()

    fun getWithdraws() = apiService.getWithdraws()

    fun getAccounts() = apiService.getAccounts()

    fun createAccount(
        firstName: String,
        lastName: String,
        middleName: String,
        accountNumber: String,
        bankCode: String
    ) = apiService.createAccount(
        firstName,
        lastName,
        middleName,
        accountNumber,
        bankCode
    )

    fun deleteAccount(accountId: Int) = apiService.deleteAccount(accountId)
}