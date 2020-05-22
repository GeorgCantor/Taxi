package taxi.kassa.model

data class Notification(
    val title: String,
    val message: String,
    val date: String,
    val longDate: Long,
    var isNew: Boolean
)