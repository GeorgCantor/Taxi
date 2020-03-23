package taxi.kassa.model

import taxi.kassa.view.registration.connection.ImageType

data class ImageDocument(
    val id: Long,
    val name: String,
    val path: String,
    val type: ImageType
)