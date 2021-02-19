package com.example.robosoftevalution.ui.search

import android.app.Application
import android.content.Context
import androidx.annotation.Nullable
import com.example.robosoftevalution.appdata.shared.BaseViewModel


class SearchViewModel (
    var context: Context,
    application: Application,
    searchRepository: SearchRepository
) : BaseViewModel<Nullable>(context, application) {
}