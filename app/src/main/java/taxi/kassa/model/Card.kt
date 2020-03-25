package taxi.kassa.model

data class Card(
    val number: Int,
    val type: String,
    val amount: String,
    val iconResource: Int
)