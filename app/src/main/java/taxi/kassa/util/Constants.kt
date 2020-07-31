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
    const val TEST_NUMBER = "3333333333"
    const val NOTIFICATIONS = "notifications"
    const val PUSH_COUNTER = "counter"
    const val MESSAGES_COUNTER = "messages_counter"
    const val TOTAL_BALANCE = "total_balance"
    const val PHONE_MASK = "+7 ([000]) [000]-[00]-[00]"
    const val CARD_MASK = "[0000] [0000] [0000] [0000]"
    const val TITLE = "1"
    const val MESSAGE = "2"
    const val MASTERCARD = "Mastercard"
    const val VISA = "Visa"
    const val NOT_FROM_PUSH = "openNotFromPush"
    const val UNREAD = "unread"

    // an error occurs when there is no Internet and no data in the cache
    const val ERROR_504 = "HTTP 504 Unsatisfiable Request (only-if-cached)"

    const val YANDEX = "yandex"
    const val GETT = "gett"
    const val CITY = "city"
    const val CITYMOBIL = "citymobil"

    const val DRIVER = "driver"
    const val ADMIN = "admin"

    const val HOURS_PATTERN = "HH:mm"
    const val DAY_YEAR_PATTERN = "dd MMMM yyyy"
    const val FULL_PATTERN = "HH:mm, dd MMMM yyyy"
    const val PUSH_PATTERN = "HH:mm, dd MMMM"

    const val PHONE_REQUEST = 11
    const val YANDEX_REQUEST = 15
    const val GETT_REQUEST = 16
    const val CITY_REQUEST = 17

    // types of photos sent to the server
    const val DRIVER_LICENCE_FRONT = 1
    const val DRIVER_LICENCE_BACK = 2
    const val PASSPORT_FIRST = 3
    const val PASSPORT_REGISTRATION = 4
    const val STS_FRONT = 5
    const val STS_BACK = 6
    const val LICENCE_FRONT = 7
    const val LICENCE_BACK = 8
    const val SELFIE = 9
    const val CAR_FRONT = 10
    const val CAR_BACK = 11
    const val CAR_LEFT = 12
    const val CAR_RIGHT = 13

    val myDateFormatSymbols: DateFormatSymbols = object : DateFormatSymbols() {
        override fun getMonths(): Array<String> {
            return arrayOf(
                "янв", "фев", "мар", "апр", "мая", "июн",
                "июл", "авг", "сен", "окт", "нояб", "дек"
            )
        }
    }
}