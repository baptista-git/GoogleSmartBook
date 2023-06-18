package com.tech.baptista.bookstore.repository.network

import com.squareup.moshi.JsonClass
import com.tech.baptista.bookstore.domain.Book
import com.tech.baptista.bookstore.repository.database.DatabaseBook
import kotlinx.android.parcel.Parcelize

/**
 * DataTransferObjects go in this file. These are responsible for parsing responses from the server
 * or formatting objects to send to the server. You should convert these to domain objects before
 * using them.
 */

@JsonClass(generateAdapter = true)
data class NetworkBookContainer(
    val kind: String,
    val totalItems: Int,
    val items: List<NetworkBookItem>
)

@JsonClass(generateAdapter = true)
data class NetworkBookItem(
    val id: String,
    val volumeInfo: NetworkBookVolumeInfo,
    val saleInfo: NetworkBookSaleInfo?
)

@JsonClass(generateAdapter = true)
data class NetworkBookVolumeInfo(
    val title: String,
    val authors: List<String> = emptyList(),
    val description: String = "Description goes here",
    val imageLinks: NetworkBookImageLinks?
)

@JsonClass(generateAdapter = true)
data class NetworkBookImageLinks(
    val thumbnail: String?,
)

@JsonClass(generateAdapter = true)
data class NetworkBookSaleInfo(
    val saleability: String,
    val buyLink: String?
)

//  extension function that converts from network entities to domain entities
fun NetworkBookContainer.asDomainModel(): List<Book> {
    return items.map {
        Book(
            id=it.id,
            title = it.volumeInfo.title,
            authors = it.volumeInfo.authors.joinToString("; "),
            description = it.volumeInfo.description,
            imageLink = it.volumeInfo?.imageLinks?.thumbnail?:"EMPTY_IMAGE_ERROR",
            buyLink = if (it.saleInfo?.saleability == "FOR_SALE") it.saleInfo.buyLink else null,
            favorite = false
        )
    }
}

//  extension function that converts from network entities to database entities
fun NetworkBookContainer.asDatabaseModel(): Array<DatabaseBook>  {
    return items.map {
        DatabaseBook(
            id=it.id,
            title = it.volumeInfo.title,
            authors = it.volumeInfo.authors.joinToString("; "),
            description = it.volumeInfo.description,
            image_link = it.volumeInfo?.imageLinks?.thumbnail?:"EMPTY_IMAGE_ERROR",
            buy_link = if (it.saleInfo?.saleability == "FOR_SALE") it.saleInfo.buyLink else null,
        )
    }.toTypedArray()
}

