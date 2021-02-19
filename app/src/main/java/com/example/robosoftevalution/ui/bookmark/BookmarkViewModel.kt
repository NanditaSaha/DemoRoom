package com.example.robosoftevalution.ui.bookmark

import android.app.Application
import android.content.Context
import androidx.annotation.Nullable
import com.example.robosoftevalution.appdata.shared.BaseViewModel


class BookmarkViewModel(
    var context: Context,
    application: Application,
    bookmarkRepository: BookmarkRepository
) : BaseViewModel<Nullable>(context, application) {
}