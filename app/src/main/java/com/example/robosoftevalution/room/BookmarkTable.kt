package com.example.robosoftevalution.room

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "bookmark")
data class BookmarkTable (
    @PrimaryKey
   // var id: Int,
    @ColumnInfo(name = "title") var title: String,
    @ColumnInfo(name = "desc") var desc: String,
    @ColumnInfo(name = "name") var name: String,
    @ColumnInfo(name = "image") var image: String
)