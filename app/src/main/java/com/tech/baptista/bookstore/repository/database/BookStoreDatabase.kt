package com.tech.baptista.bookstore.repository.database

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase

@Database(entities = [DatabaseBook::class], version = 1, exportSchema = false)
abstract class BookStoreDatabase : RoomDatabase() {
    // Connects the database to the DAO.
    abstract val bookStoreDao: BookStoreDao

    companion object {
        @Volatile
        private var INSTANCE: BookStoreDatabase? = null

        fun getInstance(context: Context): BookStoreDatabase {
            synchronized(BookStoreDatabase::class.java) {
                var instance = INSTANCE
                if (instance == null) {
                    instance = Room.databaseBuilder(
                        context.applicationContext,
                        BookStoreDatabase::class.java,
                        "books_store_database")
                        .fallbackToDestructiveMigration()
                        .build()
                    INSTANCE = instance
                }

                return instance
            }
        }
    }
}