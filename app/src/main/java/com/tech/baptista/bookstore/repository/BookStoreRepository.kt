package com.tech.baptista.bookstore.repository

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.map
import com.tech.baptista.bookstore.domain.Book
import com.tech.baptista.bookstore.repository.database.BookStoreDatabase
import com.tech.baptista.bookstore.repository.database.asDatabaseModel
import com.tech.baptista.bookstore.repository.database.asDomainModel
import com.tech.baptista.bookstore.repository.network.BookStoreApi
import com.tech.baptista.bookstore.repository.network.asDatabaseModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class BookStoreRepository(private val database: BookStoreDatabase) {

    /**
     * A list of books that can be shown on the screen.
     */
    val books: LiveData<List<Book>> =
        database.bookStoreDao.getBooks().map {
            it.asDomainModel()
        }


    suspend fun newBookColection(search: String) {
        withContext(Dispatchers.IO) {
            val booklist =
                BookStoreApi.retrofitService.getJsonResponse(search = search)
            database.bookStoreDao.removeUnfavorites()
            database.bookStoreDao.insertAll(*booklist.asDatabaseModel())
        }
    }

    suspend fun updateColection(search: String, index: Int) {
        withContext(Dispatchers.IO) {
            val booklist =
                BookStoreApi.retrofitService.getJsonResponse(search = search, start = index)
            database.bookStoreDao.insertAll(*booklist.asDatabaseModel())
        }
    }

    suspend fun filterToFavorites(){
        withContext(Dispatchers.IO) {
            database.bookStoreDao.removeUnfavorites()
        }
    }

    suspend fun updateBook(book: Book){
        withContext(Dispatchers.IO) {
            database.bookStoreDao.update(book.asDatabaseModel())
        }
    }
}