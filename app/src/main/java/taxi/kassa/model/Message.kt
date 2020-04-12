package taxi.kassa.model

data class Message(
    val topic: String,
    val message: String,
    val date: String,
    val isIncoming: Boolean
)