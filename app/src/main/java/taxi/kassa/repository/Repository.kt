package taxi.kassa.repository

import kotlinx.coroutines.Deferred
import kotlinx.coroutines.async
import kotlinx.coroutines.coroutineScope
import taxi.kassa.model.Notification
import taxi.kassa.model.remote.ApiService
import taxi.kassa.util.Constants.NOTIFICATIONS
import taxi.kassa.util.Constants.TOKEN
import taxi.kassa.util.PreferenceManager

class Repository(
    private val apiService: ApiService,
    private val preferenceManager: PreferenceManager
) {

    suspend fun login(phone: String) = apiService.authSendPhone(phone)

    suspend fun signUp(
        name: String,
        phone: String,
        source_id: Int,
        key: String
    ) = apiService.createRequest(name, phone, source_id, key)

    suspend fun getCode(
        phone: String,
        code: String
    ) = apiService.getCode(phone, code)

    suspend fun getOwner() = apiService.getOwner()

    suspend fun getWithdraws() = apiService.getWithdraws()

    suspend fun getAccounts() = apiService.getAccounts()

    suspend fun createAccount(
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

    suspend fun deleteAccount(accountId: Int) = apiService.deleteAccount(accountId)

    suspend fun getCards() = apiService.getCards()

    suspend fun addCard(cardNumber: String) = apiService.addCard(cardNumber)

    suspend fun createWithdraw(
        sourceId: Int,
        amount: String?,
        accountId: Int
    ) = apiService.createWithdraw(sourceId, amount, accountId)

    suspend fun getOrders(offset: String) = apiService.getOrders(offset)

    suspend fun getChatHistory(offset: String) = apiService.getMessages(offset)

    suspend fun sendMessage(message: String) = apiService.sendMessage(message)

    suspend fun getNotificationsAsync(): Deferred<MutableList<Notification>> = coroutineScope {
        async {
            val notifications = preferenceManager.getNotifications(NOTIFICATIONS)
            notifications?.let {
                it.sortByDescending { it.longDate }
                return@async it
            }

            return@async mutableListOf<Notification>()
        }
    }

    suspend fun saveNotificationsAsync(notifications: MutableList<Notification>) = coroutineScope {
        async {
            preferenceManager.saveNotifications(NOTIFICATIONS, notifications)
        }
    }

    suspend fun getTokenAsync(): Deferred<String> = coroutineScope {
        async {
            return@async preferenceManager.getString(TOKEN) ?: ""
        }
    }
}