package com.example.robosoftevalution.appdata.shared

import android.app.Application
import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.robosoftevalution.data.api.ApiHelper
import com.example.robosoftevalution.ui.bookmark.BookmarkRepository
import com.example.robosoftevalution.ui.bookmark.BookmarkViewModel
import com.example.robosoftevalution.ui.details.DetailsRepository
import com.example.robosoftevalution.ui.details.DetailsViewModel
import com.example.robosoftevalution.ui.home.HomeRepository
import com.example.robosoftevalution.ui.home.HomeViewModel
import com.example.robosoftevalution.ui.search.SearchRepository
import com.example.robosoftevalution.ui.search.SearchViewModel


class ViewModelFactory(
    private val apiHelper: ApiHelper, private val context: Context,
    private val application: Application
) : ViewModelProvider.Factory {

    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(HomeViewModel::class.java)) {
            return HomeViewModel(context, application, HomeRepository(apiHelper)) as T
        }else if (modelClass.isAssignableFrom(DetailsViewModel::class.java)) {
            return DetailsViewModel(context, application, DetailsRepository(apiHelper)) as T
        }
        else if (modelClass.isAssignableFrom(BookmarkViewModel::class.java)) {
            return BookmarkViewModel(context, application, BookmarkRepository()) as T
        }
        else if (modelClass.isAssignableFrom(SearchViewModel::class.java)) {
            return SearchViewModel(context, application, SearchRepository()) as T
        }
        else
            throw IllegalArgumentException("Unknown class name")
    }

}