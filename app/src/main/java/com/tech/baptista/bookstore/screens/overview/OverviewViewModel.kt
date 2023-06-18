package com.tech.baptista.bookstore.screens.overview

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
import kotlinx.coroutines.launch


enum class BooksApiStatus { LOADING, ERROR, DONE }

class OverviewViewModel(application: Application) : AndroidViewModel(application) {
    // The internal MutableLiveData /external immutable LiveData String that stores the response of the most recent request
    private val _response = MutableLiveData<String>()
    val response: LiveData<String>
        get() = _response

    // stores the most recent response status
    private val _status = MutableLiveData<BooksApiStatus>()
    val status: LiveData<BooksApiStatus>
        get() = _status

    // stores the most recent Books
    private val _books = MutableLiveData<List<Book>>()
    val properties: LiveData<List<Book>>
        get() = _books

    // handle navigation to the selected book
    private val _navigateToSelectedBook = MutableLiveData<Book?>()
    val navigateToSelectedBook: LiveData<Book?>
        get() = _navigateToSelectedBook


    private val database = BookStoreDatabase.getInstance(application)
    private val booksRepository = BookStoreRepository(database)

    val booklist = booksRepository.books

    private var searchString: String = "IOS"
    private var baseIndex: Int = 0

    init {
        filterToFavorites()
    }

    fun refreshBookDatabase(search: String) {
        searchString = search
        baseIndex = 0
        viewModelScope.launch {
            _status.value = BooksApiStatus.LOADING
            booksRepository.newBookColection(search = searchString)
            _status.value = BooksApiStatus.DONE
        }
    }

    fun updateBookDatabase() {
        baseIndex += 20
        viewModelScope.launch {
            _status.value = BooksApiStatus.LOADING
            booksRepository.updateColection(search = searchString, index = baseIndex)
            _status.value = BooksApiStatus.DONE
        }
    }

    fun filterToFavorites() {
        viewModelScope.launch {
            _status.value = BooksApiStatus.LOADING
            booksRepository.filterToFavorites()
            _status.value = BooksApiStatus.DONE
        }
    }

    /**
     * When the property is clicked, set the [_navigateToSelectedBook] [MutableLiveData]
     * @param books The [Book] that was clicked on.
     */
    fun displayBookDetails(book: Book) {
        _navigateToSelectedBook.value = book
    }

    /**
     * After the navigation has taken place, make sure navigateToSelectedBook is set to null
     */
    fun displayBookDetailsComplete() {
        _navigateToSelectedBook.value = null
    }


    /**
     * Factory for constructing OverviewViewModel with parameter
     */
    class Factory(val app: Application) : ViewModelProvider.Factory {
        override fun <T : ViewModel> create(modelClass: Class<T>): T {
            if (modelClass.isAssignableFrom(OverviewViewModel::class.java)) {
                @Suppress("UNCHECKED_CAST")
                return OverviewViewModel(app) as T
            }
            throw IllegalArgumentException("Unable to construct OverviewViewModel")
        }
    }

}