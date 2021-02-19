package com.example.robosoftevalution.data.model

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize


@Parcelize
data class ArticalModel (
    var source : SourceModel,
    var author : String,
    var title : String,
    var description : String,
    var url : String,
    var urlToImage : String,
    var publishedAt : String,
    var content : String
    ):Parcelable

@Parcelize
data class SourceModel (
    var id : String,
    var name : String
):Parcelable