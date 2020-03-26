package taxi.kassa.model

data class Card(
    val number: String,
    val type: String,
    val amount: String,
    val iconResource: Int
)