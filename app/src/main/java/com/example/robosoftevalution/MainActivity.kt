package com.example.robosoftevalution

import android.os.AsyncTask
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import com.example.robosoftevalution.room.BookmarkDataBase
import com.example.robosoftevalution.room.BookmarkTable

class MainActivity : AppCompatActivity() {
    private var bookmarkDataBase: BookmarkDataBase? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        bookmarkDataBase= BookmarkDataBase.getDatabase(this)

    }


}