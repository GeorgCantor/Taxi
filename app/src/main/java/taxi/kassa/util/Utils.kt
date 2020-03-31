package taxi.kassa.util

import java.text.DateFormatSymbols
import java.text.SimpleDateFormat
import java.util.*

fun convertLongToTime(time: Long, pattern: String): String {
    val date = Date(time)
    val dateFormat = SimpleDateFormat(pattern, myDateFormatSymbols)

    return dateFormat.format(date)
}

val myDateFormatSymbols: DateFormatSymbols = object : DateFormatSymbols() {
    override fun getMonths(): Array<String> {
        return arrayOf(
            "янв", "фев", "мар", "апр", "мая", "июн",
            "июл", "авг", "сен", "окт", "нояб", "дек"
        )
    }
}