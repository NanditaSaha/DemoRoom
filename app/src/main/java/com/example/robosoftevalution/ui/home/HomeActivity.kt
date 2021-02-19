package com.example.robosoftevalution.ui.home

import android.content.Intent
import android.graphics.drawable.Drawable
import android.os.AsyncTask
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.LinearLayoutManager
import com.bumptech.glide.Glide
import com.bumptech.glide.load.DataSource
import com.bumptech.glide.load.engine.GlideException
import com.bumptech.glide.request.RequestListener
import com.bumptech.glide.request.RequestOptions
import com.bumptech.glide.request.target.Target
import com.example.robosoftevalution.MainActivity
import com.example.robosoftevalution.R
import com.example.robosoftevalution.appdata.shared.ViewModelFactory
import com.example.robosoftevalution.appdata.utils.AppConfig
import com.example.robosoftevalution.appdata.utils.Status
import com.example.robosoftevalution.appdata.utils.Utils
import com.example.robosoftevalution.data.api.ApiHelper
import com.example.robosoftevalution.data.api.ApiServiceImpl
import com.example.robosoftevalution.data.model.Articles
import com.example.robosoftevalution.data.model.NewsResponse
import com.example.robosoftevalution.databinding.ActivityHomeBinding
import com.example.robosoftevalution.room.ArticalTable
import com.example.robosoftevalution.room.BookmarkDataBase
import com.example.robosoftevalution.room.BookmarkTable
import com.example.robosoftevalution.ui.adapter.HomeAdapter
import com.example.robosoftevalution.ui.bookmark.BookmarkActivity
import com.example.robosoftevalution.ui.details.DetailsActivity
import com.example.robosoftevalution.ui.interfaces.OnBookmarkClickListner
import com.example.robosoftevalution.ui.interfaces.OnViewClickListner
import com.example.robosoftevalution.ui.search.SearchActivity
import com.kaopiz.kprogresshud.KProgressHUD



class HomeActivity : AppCompatActivity(), OnViewClickListner, OnBookmarkClickListner {
    private lateinit var homeViewModel: HomeViewModel
    private lateinit var dataArrayList: ArrayList<Articles>
    private lateinit var linearLayoutManager: LinearLayoutManager
    private lateinit var homeAdapter: HomeAdapter
    private lateinit var binding: ActivityHomeBinding
    private var hud: KProgressHUD? = null
    private lateinit var data: NewsResponse
    private var bookmarkDataBase: BookmarkDataBase? = null


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_home)
        setupViewModel()
        initRvCategory()
        setupSwipe()
        bookmarkDataBase= BookmarkDataBase.getDatabase(this)

        binding.ivBookmark.setOnClickListener {
            val intent=Intent(this@HomeActivity, BookmarkActivity::class.java)
            startActivity(intent)
        }
        binding.ivSearch.setOnClickListener {
            val intent=Intent(this@HomeActivity, SearchActivity::class.java)
            startActivity(intent)
        }

        if (Utils.isInternetON()) {
            hud = Utils.showLoader(this)
            homeViewModel.GetHomeTopHeadline(AppConfig.COUNTRY, AppConfig.APIKEY)
            homeViewModel.GetHomeData(AppConfig.COUNTRY, AppConfig.CATEGORY, AppConfig.APIKEY)

            observeData()
        } else {
            Utils.showAlertDialog(this@HomeActivity, "No Internet!")
        }

    }

    private fun setupViewModel() {



        homeViewModel = ViewModelProviders.of(
                this,
                ViewModelFactory(ApiHelper(ApiServiceImpl()), this, application)
        ).get(HomeViewModel::class.java)

    }

    private fun initRvCategory() {

        dataArrayList = ArrayList<Articles>()
        linearLayoutManager = LinearLayoutManager(this@HomeActivity)
        binding.rvhome.layoutManager = linearLayoutManager
        homeAdapter = HomeAdapter(this@HomeActivity, dataArrayList)
        homeAdapter.setOnClickListner(this)
        homeAdapter.setOnBookmarkClickListner(this)
        binding.rvhome.adapter = homeAdapter

    }

    private fun setupSwipe() {
        binding.swipe.setOnRefreshListener {
            if (Utils.isInternetON()) {
                hud = Utils.showLoader(this)
                homeViewModel.GetHomeData(AppConfig.COUNTRY, AppConfig.CATEGORY, AppConfig.APIKEY)
                observeData()
                binding.swipe.isRefreshing = false
            } else {
                Utils.showAlertDialog(this@HomeActivity, "No Internet!")
            }
        }
    }

    override fun onResume() {
        super.onResume()

    }

    private fun observeData() {
        homeViewModel.getHomeData().observe(this, Observer {
            when (it.status) {

                Status.SUCCESS -> {
                    if (binding.swipe.isRefreshing)
                        binding.swipe.isRefreshing = false
                    Utils.dismissLoader(hud!!)
                    it.data!!.let { resources ->
                        if (resources.articles.size > 0) {
                            data=resources
                            dataArrayList.clear()
                            dataArrayList.addAll(resources.articles)
                            homeAdapter.notifyDataSetChanged()
                            for( i in 0..resources.articles.size-1)
                            {
                                val articalObj = ArticalTable(resources.articles[i].title,resources.articles[i].description,resources.articles[i].source.name,resources.articles[i].urlToImage)
                                InsertTaskArtical(this, articalObj).execute()
                            }
                        }
                    }
                }
                Status.LOADING -> {
                    Utils.dismissLoader(hud!!)
                }
                Status.ERROR -> {
                    Utils.dismissLoader(hud!!)
                }
            }
        })
        homeViewModel.getHomeTopHeadline().observe(this, Observer {
            when (it.status) {

                Status.SUCCESS -> {
                    if (binding.swipe.isRefreshing)
                        binding.swipe.isRefreshing = false
                    Utils.dismissLoader(hud!!)
                    it.data!!.let { resources ->
                        if (resources.articles.size > 0) {

                            binding.tvTitletop.text = resources.articles[0].title
                            binding.tvDesctop.text = resources.articles[0].description
                            binding.tvNametop.text = resources.articles[0].source.name
                            try {
                                Glide
                                        .with(this)
                                        .load(resources.articles[0].urlToImage)
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
                                        .into(binding.ivAvtartop)
                            } catch (e: Exception) {
                                e.printStackTrace()
                            }

                        }

                    }
                }
                Status.LOADING -> {
                    Utils.dismissLoader(hud!!)
                }
                Status.ERROR -> {
                    Utils.dismissLoader(hud!!)
                }
            }
        })

    }

    override fun onViewClick(title:String,imageurl:String,url:String,desc:String,name:String) {
        val intent=Intent(this@HomeActivity, DetailsActivity::class.java)
        intent.putExtra("title",title)
        intent.putExtra("imageurl",imageurl)
        intent.putExtra("url",url)
        intent.putExtra("desc",desc)
        intent.putExtra("name",name)
        Log.e("datacount",dataArrayList.size.toString())
       // intent.putParcelableArrayListExtra("data",dataArrayList)
        startActivity(intent)
    }

   override fun onBookmarkClick(title: String, imageurl: String, desc: String, name: String) {
        val bookmarkObj = BookmarkTable(title,desc,name,imageurl)
        InsertTask(this, bookmarkObj).execute()
    }

    private class InsertTask(var context: HomeActivity, var bookmark: BookmarkTable) : AsyncTask<Void, Void, Boolean>() {
        override fun doInBackground(vararg params: Void?): Boolean {
            context.bookmarkDataBase!!.bookmarkDao().insertAll(bookmark)
            return true
        }
        override fun onPostExecute(bool: Boolean?) {
            if (bool!!) {
               // Toast.makeText(context, "Added to Database", Toast.LENGTH_LONG).show()
            }
        }
    }
    private class InsertTaskArtical(var context: HomeActivity, var bookmark: ArticalTable) : AsyncTask<Void, Void, Boolean>() {
        override fun doInBackground(vararg params: Void?): Boolean {
            context.bookmarkDataBase!!.bookmarkDao().insertAllArticle(bookmark)
            return true
        }
        override fun onPostExecute(bool: Boolean?) {
            if (bool!!) {

            }
        }
    }
}