package com.example.robosoftevalution.ui.bookmark

import android.os.AsyncTask
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.MenuItem
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.robosoftevalution.MainActivity
import com.example.robosoftevalution.R
import com.example.robosoftevalution.appdata.shared.ViewModelFactory
import com.example.robosoftevalution.data.api.ApiHelper
import com.example.robosoftevalution.data.api.ApiServiceImpl
import com.example.robosoftevalution.data.model.Articles
import com.example.robosoftevalution.databinding.ActivityBookmarkBinding
import com.example.robosoftevalution.room.BookmarkDataBase
import com.example.robosoftevalution.room.BookmarkTable
import com.example.robosoftevalution.ui.adapter.BookmarkAdapter
import com.example.robosoftevalution.ui.adapter.HomeAdapter
import com.example.robosoftevalution.ui.details.DetailsViewModel

class BookmarkActivity : AppCompatActivity() {
   private lateinit var binding:ActivityBookmarkBinding
    private lateinit var bookmarkViewModel:BookmarkViewModel
    private var bookmarkDataBase: BookmarkDataBase? = null
    private lateinit var dataArrayList: ArrayList<BookmarkTable>
    private lateinit var linearLayoutManager: LinearLayoutManager
    private lateinit var bookAdapter: BookmarkAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding= DataBindingUtil.setContentView(this, R.layout.activity_bookmark)
        setupViewModel()
        setUpHeader()
        initRvCategory()
        bookmarkDataBase= BookmarkDataBase.getDatabase(this)
        GetDataFromDb(this).execute()

    }

    private fun setupViewModel() {
        bookmarkViewModel = ViewModelProviders.of(
            this,
            ViewModelFactory(ApiHelper(ApiServiceImpl()), this, application)
        ).get(BookmarkViewModel::class.java)

    }

    private class GetDataFromDb(var context: BookmarkActivity) : AsyncTask<Void, Void, List<BookmarkTable>>() {
        override fun doInBackground(vararg params: Void?): List<BookmarkTable> {
            return context.bookmarkDataBase!!.bookmarkDao().getAll()
        }
        override fun onPostExecute(chapterList: List<BookmarkTable>?) {
            if (chapterList!!.size > 0) {
                Log.e("SIZE",chapterList!!.size.toString())
                context.dataArrayList.addAll(chapterList)
                context.bookAdapter.notifyDataSetChanged()
            }
        }
    }

    fun setUpHeader() {

        setSupportActionBar(binding.toolbarBookmark)
        supportActionBar?.setDisplayShowHomeEnabled(true)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)

    }
    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when(item.itemId)
        {
            android.R.id.home->onBackPressed()
        }
        return super.onOptionsItemSelected(item)
    }
    private fun initRvCategory() {

        dataArrayList = ArrayList<BookmarkTable>()
        linearLayoutManager = LinearLayoutManager(this@BookmarkActivity)
        binding.rvBookmark.layoutManager = linearLayoutManager
        dataArrayList.clear()
        bookAdapter = BookmarkAdapter(this@BookmarkActivity, dataArrayList)
        binding.rvBookmark.adapter = bookAdapter

    }
}