package com.example.robosoftevalution.room

import androidx.room.*

@Dao
interface BookmarkDAO {
    @Query("SELECT * FROM bookmark")
    fun getAll(): List<BookmarkTable>

    @Query("SELECT * FROM article")
    fun getAllArticle(): List<ArticalTable>

    @Query("SELECT * FROM bookmark")
    fun findByTitle(): BookmarkTable

    @Insert
    fun insertAll(vararg todo: BookmarkTable)
    @Insert
    fun insertAllArticle(vararg article: ArticalTable)

    @Delete
    fun delete(todo: BookmarkTable)

    @Update
    fun updateTodo(vararg todos: BookmarkTable)
}