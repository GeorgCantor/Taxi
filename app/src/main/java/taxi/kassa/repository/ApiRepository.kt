package taxi.kassa.repository

import taxi.kassa.model.Message
import taxi.kassa.model.Notification
import taxi.kassa.model.remote.ApiService
import taxi.kassa.util.Constants.NOTIFICATIONS
import taxi.kassa.util.PreferenceManager

class ApiRepository(
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

    suspend fun createWithdraw(
        sourceId: Int,
        amount: String?,
        accountId: Int
    ) = apiService.createWithdraw(sourceId, amount, accountId)

    suspend fun getOrders(offset: String) = apiService.getOrders(offset)

    fun getNotifications(): MutableList<Notification> {
        val notifications = preferenceManager.getNotifications(NOTIFICATIONS)
        notifications?.let {
            it.sortByDescending { it.date }
            return it
        }

        return arrayListOf()
    }

    fun getChatHistory(): MutableList<Message> {
        return mutableListOf(
            Message(
                topic = "Проблема с выводом денег",
                message = "День добрый, мне не дают вывести деньги! Дело в том что я зарегистрировался в БК винлайн хотел поставить ставку, но на все события максимум был от 200 до 500р . меня это не устроило и я поставил деньги на вывод, не выводили три дня я сразу написал в чате о своей проблеме, мне сказали обратиться в службу поддержки. ",
                date = "14:00, 8 окт. 2019г.",
                isIncoming = false
            ),
            Message(
                topic = "Проблема с выводом денег",
                message = "Обращение принятно и находится на рассмотрении, мы венрнемся с ответом в 17:00",
                date = "14:00, 8 окт. 2019г.",
                isIncoming = true
            ),
            Message(
                topic = "Проблема с выводом денег",
                message = "Меня это не устроило и я поставил деньги на вывод, не выводили три дня я сразу написал в чате о своей проблеме",
                date = "14:00, 8 окт. 2019г.",
                isIncoming = false
            ),
            Message(
                topic = "Проблема с выводом денег",
                message = "Обращение принятно и находится на рассмотрении, мы венрнемся с ответом в 17:00",
                date = "14:00, 8 окт. 2019г.",
                isIncoming = true
            ),
            Message(
                topic = "Проблема с выводом денег",
                message = "День добрый, мне не дают вывести деньги! Дело в том что я зарегистрировался в БК винлайн хотел поставить ставку, но на все события максимум был от 200 до 500р . меня это не устроило и я поставил деньги на вывод, не выводили три дня я сразу написал в чате о своей проблеме, мне сказали обратиться в службу поддержки. ",
                date = "14:00, 8 окт. 2019г.",
                isIncoming = false
            ),
            Message(
                topic = "Проблема с выводом денег",
                message = "Обращение принятно и находится на рассмотрении, мы венрнемся с ответом в 17:00",
                date = "14:00, 8 окт. 2019г.",
                isIncoming = true
            ),
            Message(
                topic = "Проблема с выводом денег",
                message = "Меня это не устроило и я поставил деньги на вывод, не выводили три дня я сразу написал в чате о своей проблеме",
                date = "14:00, 8 окт. 2019г.",
                isIncoming = false
            ),
            Message(
                topic = "Проблема с выводом денег",
                message = "Обращение принятно и находится на рассмотрении, мы венрнемся с ответом в 17:00",
                date = "14:00, 8 окт. 2019г.",
                isIncoming = true
            )
        )
    }
}