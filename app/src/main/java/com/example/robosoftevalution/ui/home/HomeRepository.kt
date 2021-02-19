package com.example.robosoftevalution.ui.home


import com.example.robosoftevalution.data.api.ApiHelper
import com.example.robosoftevalution.data.model.NewsResponse

import io.reactivex.Observable

class HomeRepository(private val apiHelper: ApiHelper) {
    fun doGetTopHeadline(country:String,apikey:String): Observable<NewsResponse> {
        return apiHelper.fetchTopHeadline(country,apikey)
    }

    fun doGetData(country:String,category:String,apikey:String): Observable<NewsResponse> {
        return apiHelper.fetchData(country,category,apikey)
    }


}