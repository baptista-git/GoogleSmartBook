package com.tech.baptista.bookstore.repository.database

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.tech.baptista.bookstore.domain.Book
import com.tech.baptista.bookstore.repository.network.NetworkBookContainer

@Entity(tableName = "book_property_table")
data class DatabaseBook(
    @PrimaryKey
    val id: String,
    val title: String,
    val authors: String,
    val description: String,
    val image_link: String,
    val buy_link: String? = null,
    val favorite:Boolean = false
    )

// extension function which converts from database objects to domain objects
fun List<DatabaseBook>.asDomainModel(): List<Book> {
    return map {
        Book(
            id=it.id,
            title = it.title,
            authors = it.authors,
            description = it.description,
            imageLink = it.image_link,
            buyLink = it.buy_link,
            favorite = it.favorite
        )
    }
}
// extension function which converts from database objects to domain objects
fun DatabaseBook.asDomainModel(): Book {
    return Book(
        id = id,
        title = title,
        authors = authors,
        description = description,
        imageLink = image_link,
        buyLink = buy_link,
        favorite = favorite
    )
}

//  extension function that converts from domain entity to database entity
fun Book.asDatabaseModel(): DatabaseBook {
    return DatabaseBook(
        id = id,
        title = title,
        authors = authors,
        description = description,
        image_link = imageLink,
        buy_link = buyLink,
        favorite = favorite
    )
}