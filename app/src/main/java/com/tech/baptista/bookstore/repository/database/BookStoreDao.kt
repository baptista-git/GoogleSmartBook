package com.tech.baptista.bookstore.repository.database

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update

@Dao
interface BookStoreDao {
    @Query("select * from book_property_table")
    fun getBooks(): LiveData<List<DatabaseBook>>

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    fun insertAll(vararg books: DatabaseBook)

    @Update
    fun update(book: DatabaseBook)

    @Query("SELECT * from book_property_table where id = :key")
    fun get(key: String): DatabaseBook

    @Query("delete from book_property_table where favorite <> 1")
    fun removeUnfavorites()
}