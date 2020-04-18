package taxi.kassa.util

import java.text.DateFormatSymbols

object Constants {
    var accessToken = ""

    const val API_VERSION = "1"
    const val KEY = "ZVc3THdVdzM5dDhXQVBoR1ZJNk80SmNxdjBGcEhGUzY4YkJIcmV"
    const val MAIN_STORAGE = "MainStorage"
    const val PHONE = "phone"
    const val TOKEN = "access_token"
    const val NEW = "Новая"
    const val APPROVED = "Одобрено"
    const val WRITTEN_OFF = "Списано"
    const val CANCELED = "Отменено"
    const val WITHDRAW = "withdraw"
    const val TAXI = "taxi"
    const val CONNECTION = "connection"
    const val SUPPORT_PHONE_NUMBER = "+74993505558"
    const val NOTIFICATIONS = "notifications"
    const val PUSH_COUNTER = "counter"
    const val MESSAGES_COUNTER = "messages_counter"
    const val TOTAL_BALANCE = "total_balance"
    const val PHONE_MASK = "+7 ([000]) [000]-[00]-[00]"

    const val YANDEX = "yandex"
    const val GETT = "gett"
    const val CITYMOBIL = "citymobil"

    const val TITLE = "1"
    const val MESSAGE = "2"

    const val HOURS_PATTERN = "HH:mm"
    const val DAY_YEAR_PATTERN = "dd MMMM yyyy"
    const val FULL_PATTERN = "HH:mm, dd MMMM yyyy"
    const val PUSH_PATTERN = "HH:mm, dd MMMM"

    const val MASTERCARD = "Mastercard"
    const val VISA = "Visa"

    val myDateFormatSymbols: DateFormatSymbols = object : DateFormatSymbols() {
        override fun getMonths(): Array<String> {
            return arrayOf(
                "янв", "фев", "мар", "апр", "мая", "июн",
                "июл", "авг", "сен", "окт", "нояб", "дек"
            )
        }
    }
}