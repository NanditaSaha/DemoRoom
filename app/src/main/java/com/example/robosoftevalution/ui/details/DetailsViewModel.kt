package com.example.robosoftevalution.ui.details

import android.app.Application
import android.content.Context
import androidx.annotation.Nullable
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.example.robosoftevalution.appdata.shared.BaseViewModel
import com.example.robosoftevalution.appdata.utils.Resource
import com.example.robosoftevalution.data.model.NewsResponse
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers


class DetailsViewModel(
    var context: Context,
    application: Application,
    detailsRepository: DetailsRepository
) : BaseViewModel<Nullable>(context, application) {
    private var detailsRepository: DetailsRepository? = null
    private val homeData = MutableLiveData<Resource<NewsResponse>>()
    init {
        this.detailsRepository = detailsRepository
    }

    fun GetHomeData(country: String, category: String, apikey: String) {

        homeData.postValue(Resource.loading(null))
        getCompositeDisposable()!!.add(
                detailsRepository!!.doGetData(country, category, apikey)
                        .subscribeOn(Schedulers.io())
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribe({ bannerResponse ->

                            homeData.postValue(Resource.success(bannerResponse))
                        }, { throwable ->

                            homeData.postValue(Resource.error("Something Went Wrong", null))
                        })
        )

    }

    fun getHomeData(): LiveData<Resource<NewsResponse>> {
        return homeData
    }

}