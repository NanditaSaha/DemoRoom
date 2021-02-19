package com.example.robosoftevalution.ui.search

import android.os.AsyncTask
import android.os.AsyncTask.execute
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.ArrayAdapter
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.ViewModelProviders
import com.example.robosoftevalution.R
import com.example.robosoftevalution.appdata.shared.ViewModelFactory
import com.example.robosoftevalution.data.api.ApiHelper
import com.example.robosoftevalution.data.api.ApiServiceImpl
import com.example.robosoftevalution.databinding.ActivitySearchBinding
import com.example.robosoftevalution.room.ArticalTable
import com.example.robosoftevalution.room.BookmarkDataBase
import com.example.robosoftevalution.room.BookmarkTable
import com.example.robosoftevalution.ui.bookmark.BookmarkViewModel
import com.example.robosoftevalution.ui.details.DetailsActivity

class SearchActivity : AppCompatActivity() {
    private lateinit var binding:ActivitySearchBinding
    private lateinit var searchViewModel: SearchViewModel
    private var bookmarkDataBase: BookmarkDataBase? = null
    private lateinit var dataArrayList: ArrayList<String>


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding= DataBindingUtil.setContentView(this, R.layout.activity_search)
        dataArrayList = ArrayList<String>()
        setupViewModel()
        bookmarkDataBase= BookmarkDataBase.getDatabase(this)
        GetDataFromDbArtical(this).execute()
    }
    private fun setupViewModel() {
        searchViewModel = ViewModelProviders.of(
            this,
            ViewModelFactory(ApiHelper(ApiServiceImpl()), this, application)
        ).get(SearchViewModel::class.java)

    }

    private class GetDataFromDbArtical(var context: SearchActivity) : AsyncTask<Void, Void, List<ArticalTable>>() {
        override fun doInBackground(vararg params: Void?): List<ArticalTable> {
            return context.bookmarkDataBase!!.bookmarkDao().getAllArticle()
        }
        override fun onPostExecute(chapterList: List<ArticalTable>?) {
            if (chapterList!!.size > 0) {
                for (i in 0..chapterList!!.size-1)
                {
                    context.dataArrayList.add(chapterList[i].title)
                }

                val adapter = ArrayAdapter(context,
                    android.R.layout.simple_list_item_1,  context.dataArrayList)
                context.binding.autoTextView.setAdapter(adapter)
            }
        }
    }
}