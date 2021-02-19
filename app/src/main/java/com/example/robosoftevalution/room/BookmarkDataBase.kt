package com.example.robosoftevalution.room

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase

@Database(entities = arrayOf(BookmarkTable::class,ArticalTable::class), version = 1)
abstract class BookmarkDataBase:RoomDatabase() {
    abstract fun bookmarkDao(): BookmarkDAO
    companion object {
        private var INSTANCE: BookmarkDataBase? = null
        fun getDatabase(context: Context): BookmarkDataBase? {
            if (INSTANCE == null) {
                synchronized(BookmarkDataBase::class) {
                    INSTANCE = Room.databaseBuilder(
                        context.getApplicationContext(),
                        BookmarkDataBase::class.java, "bookmark.db"
                    ).build()
                }
            }
            return INSTANCE
        }
    }
}