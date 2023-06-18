package com.tech.baptista.bookstore.screens.detail

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.tech.baptista.bookstore.domain.Book
import com.tech.baptista.bookstore.repository.BookStoreRepository
import com.tech.baptista.bookstore.repository.database.BookStoreDatabase
import com.tech.baptista.bookstore.repository.database.asDatabaseModel
import com.tech.baptista.bookstore.repository.database.asDomainModel
import com.tech.baptista.bookstore.screens.overview.BooksApiStatus
import kotlinx.coroutines.launch

/**
 *  The [ViewModel] associated with the [DetailFragment], containing information about the selected
 *  [Book].
 */
class DetailViewModel(book: Book, application: Application) : AndroidViewModel(application) {

    private val _selectedBook = MutableLiveData<Book>()
    val selectedBook: LiveData<Book>
        get() = _selectedBook

    private val database = BookStoreDatabase.getInstance(application)
    private val booksRepository = BookStoreRepository(database)

    // Initialize the _selectedProperty MutableLiveData
    init {
        _selectedBook.value = book
    }

    fun updateFavoriteOnBook() {
        val book  = _selectedBook.value?:return
        val favorite = book.favorite
        val newboook = book.copy(favorite = !favorite)
        viewModelScope.launch {
            booksRepository.updateBook(newboook)
        }
        _selectedBook.value = newboook
    }

    /**
     * Factory for constructing DetailViewModel with parameter
     */
    class Factory(
        private val book: Book,
        private val application: Application
        ) : ViewModelProvider.Factory {
        override fun <T : ViewModel> create(modelClass: Class<T>): T {
            if (modelClass.isAssignableFrom(DetailViewModel::class.java)) {
                @Suppress("UNCHECKED_CAST")
                return DetailViewModel(book, application) as T
            }
            throw IllegalArgumentException("Unable to construct DetailViewModel")
        }
    }
}