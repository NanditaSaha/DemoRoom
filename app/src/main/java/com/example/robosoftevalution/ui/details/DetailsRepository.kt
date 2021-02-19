package com.example.robosoftevalution.ui.details

import com.example.robosoftevalution.data.api.ApiHelper
import com.example.robosoftevalution.data.model.NewsResponse
import io.reactivex.Observable

class DetailsRepository(private val apiHelper: ApiHelper) {
    fun doGetData(country:String,category:String,apikey:String): Observable<NewsResponse> {
        return apiHelper.fetchData(country,category,apikey)
    }
}