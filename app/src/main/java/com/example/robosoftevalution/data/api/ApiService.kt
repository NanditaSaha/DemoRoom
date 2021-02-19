package com.example.robosoftevalution.data.api

import com.example.robosoftevalution.data.model.NewsResponse
import io.reactivex.Observable

interface ApiService {
    fun fetchTopHeadline(country:String,apikey:String): Observable<NewsResponse>
    fun fetchData(country:String,category:String,apikey:String): Observable<NewsResponse>
}