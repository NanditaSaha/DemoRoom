package com.example.robosoftevalution.room

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "article")
class ArticalTable (
    @PrimaryKey
    @ColumnInfo(name = "title") var title: String,
    @ColumnInfo(name = "desc") var desc: String,
    @ColumnInfo(name = "name") var name: String,
    @ColumnInfo(name = "image") var image: String
)