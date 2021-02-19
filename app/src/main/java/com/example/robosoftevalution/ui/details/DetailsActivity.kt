package com.example.robosoftevalution.ui.details

import android.graphics.drawable.Drawable
import android.os.AsyncTask
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.MenuItem
import android.webkit.WebViewClient
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.LinearLayoutManager
import com.bumptech.glide.Glide
import com.bumptech.glide.load.DataSource
import com.bumptech.glide.load.engine.GlideException
import com.bumptech.glide.request.RequestListener
import com.bumptech.glide.request.RequestOptions
import com.bumptech.glide.request.target.Target
import com.example.robosoftevalution.R
import com.example.robosoftevalution.appdata.shared.ViewModelFactory
import com.example.robosoftevalution.data.api.ApiHelper
import com.example.robosoftevalution.data.api.ApiServiceImpl
import com.example.robosoftevalution.data.model.NewsResponse
import com.example.robosoftevalution.databinding.ActivityDetailsBinding
import com.example.robosoftevalution.room.ArticalTable
import com.example.robosoftevalution.room.BookmarkDataBase
import com.example.robosoftevalution.ui.adapter.ArticalAdapter
import com.example.robosoftevalution.ui.bookmark.BookmarkActivity


class DetailsActivity : AppCompatActivity() {
    private lateinit var binding: ActivityDetailsBinding
    private lateinit var detailsViewModel: DetailsViewModel
    private lateinit var url:String
    private lateinit var imageurl:String
    private lateinit var title:String
    private lateinit var desc:String
    private lateinit var name:String
    private lateinit var linearLayoutManager: LinearLayoutManager
    private lateinit var homeAdapter: ArticalAdapter
    private lateinit var dataArrayList: ArrayList<ArticalTable>
    private lateinit var data: NewsResponse
    private var bookmarkDataBase: BookmarkDataBase? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding=DataBindingUtil.setContentView(this, R.layout.activity_details)
        bookmarkDataBase= BookmarkDataBase.getDatabase(this)
        setupViewModel()
        setUpHeader()
        setUpWebview()
        initRvCategory()
        GetDataFromDbArtical(this).execute()
    }

    private fun setupViewModel() {
        detailsViewModel = ViewModelProviders.of(
                this,
                ViewModelFactory(ApiHelper(ApiServiceImpl()), this, application)
        ).get(DetailsViewModel::class.java)

    }


    fun setUpHeader(){
        url= intent.getStringExtra("url").toString()
        imageurl= intent.getStringExtra("imageurl").toString()
        desc= intent.getStringExtra("desc").toString()
        title= intent.getStringExtra("title").toString()
        name= intent.getStringExtra("name").toString()

        setSupportActionBar(binding.toolbarDetails)
        supportActionBar?.setDisplayShowHomeEnabled(true)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar?.setTitle(url)

        binding.tvTitledetails.text=title
        binding.tvNameDetails.text=name

        try {
            Glide
                    .with(this@DetailsActivity)
                    .load(imageurl)
                    .apply(
                            RequestOptions().placeholder(R.drawable.place_holder)
                                    .error(R.drawable.place_holder)
                    )
                    .listener(object : RequestListener<Drawable?> {
                        override fun onLoadFailed(
                                e: GlideException?,
                                model: Any,
                                target: Target<Drawable?>,
                                isFirstResource: Boolean
                        ): Boolean {
                            return false
                        }

                        override fun onResourceReady(
                                resource: Drawable?,
                                model: Any,
                                target: Target<Drawable?>,
                                dataSource: DataSource,
                                isFirstResource: Boolean
                        ): Boolean {
                            return false
                        }
                    })
                    .into(binding.ivAvtardetails)
        } catch (e: Exception) {
            e.printStackTrace()
        }

    }
    fun setUpWebview(){
        binding.webviewContent.webViewClient= WebViewClient()
        binding.webviewContent.loadDataWithBaseURL(null, desc, "text/html", "UTF-8", null)
        binding.webviewContent.settings.javaScriptEnabled = true
        binding.webviewContent.settings.setSupportZoom(true)
    }

    private fun initRvCategory() {

        dataArrayList = ArrayList<ArticalTable>()
        linearLayoutManager = LinearLayoutManager(this@DetailsActivity)
        binding.rvPopulardetails.layoutManager = linearLayoutManager
        dataArrayList.clear()
        homeAdapter = ArticalAdapter(this@DetailsActivity, dataArrayList)
        binding.rvPopulardetails.adapter = homeAdapter

    }
    private class GetDataFromDbArtical(var context: DetailsActivity) : AsyncTask<Void, Void, List<ArticalTable>>() {
        override fun doInBackground(vararg params: Void?): List<ArticalTable> {
            return context.bookmarkDataBase!!.bookmarkDao().getAllArticle()
        }
        override fun onPostExecute(chapterList: List<ArticalTable>?) {
            if (chapterList!!.size > 0) {
                context.dataArrayList.addAll(chapterList)
                context.homeAdapter.notifyDataSetChanged()
            }
        }
    }


    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when(item.itemId)
        {
            android.R.id.home->onBackPressed()
        }
        return super.onOptionsItemSelected(item)
    }
}