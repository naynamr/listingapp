package com.example.test.ui

import android.content.Context
import android.content.res.Configuration
import android.os.Bundle
import android.os.Handler
import android.util.Log
import android.view.View
import android.view.inputmethod.InputMethodManager
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.example.test.R
import com.example.test.ui.viewmodel.MainViewModel
import com.example.test.util.CommonUtil
import kotlinx.android.synthetic.main.activity_main.*


class MainActivity : AppCompatActivity() {

    private val TAG: String = "MainActivity "
    private lateinit var mainViewModel: MainViewModel
    private var adapter: Adapter? = null
    private var itemCount = 3
    private var searchQuery = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        mainViewModel = ViewModelProvider(this).get(MainViewModel::class.java)
        getJsonData()


        mainViewModel.response.observe(this, Observer {
            loadData()
        })


        search_view.queryHint = getString(R.string.search_movies)
        search_btn.setOnClickListener {
            main_layout.visibility = View.GONE
            search_view.visibility = View.VISIBLE
            search_view.isIconified = false
            search_view.setQuery("", false)
            searchQuery = ""
        }

        search_view.setOnCloseListener(object : android.widget.SearchView.OnCloseListener,
            androidx.appcompat.widget.SearchView.OnCloseListener {
            override fun onClose(): Boolean {
                main_layout.visibility = View.VISIBLE
                search_view.visibility = View.GONE
                searchQuery = ""
                hideKeyBoard()
                return true
            }
        })

        back.setOnClickListener {
            finish()
        }

        search_view.setOnQueryTextListener(object :
            android.widget.SearchView.OnQueryTextListener,
            androidx.appcompat.widget.SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String?): Boolean {
                searchQuery = query!!
                search(searchQuery)
                hideKeyBoard()
                return true
            }

            override fun onQueryTextChange(newText: String?): Boolean {
                if (searchQuery.isNotEmpty() && newText!!.isEmpty()) {
                    mainViewModel.getOriginalList()
                    hideKeyBoard()
                }
                searchQuery = newText!!
                return true
            }
        })

        recycler_view.addOnScrollListener(object : RecyclerView.OnScrollListener() {
            override fun onScrollStateChanged(recyclerView: RecyclerView, newState: Int) {
                super.onScrollStateChanged(recyclerView, newState)
            }

            override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                super.onScrolled(recyclerView, dx, dy)
                val manager = recyclerView.layoutManager as GridLayoutManager?
                if (searchQuery.isEmpty() && manager != null && manager.findLastCompletelyVisibleItemPosition() == mainViewModel.response.value!!.size - 1) {
                    getJsonData()
                }
            }
        })


    }

    private fun hideKeyBoard() {
        val imm = getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
        imm.hideSoftInputFromWindow(search_view.windowToken, 0)
    }

    private fun search(query: String) {
        if (query.length > 2) {
            mainViewModel.search(query)
        }
    }

    override fun onConfigurationChanged(newConfig: Configuration) {
        super.onConfigurationChanged(newConfig)

        if (newConfig.orientation == Configuration.ORIENTATION_LANDSCAPE) {
            itemCount = 7
        } else {
            itemCount = 3
        }
        init()
    }

    private fun loadData() {
        if (adapter == null) {
            init()
        } else {
            Handler().postDelayed({
                adapter!!.updateGalleryListAdapter(mainViewModel.response.value!!, searchQuery)
            }, 200)
        }
    }

    private fun init(){
        val options = RequestOptions()
        val glide = Glide.with(this)
            .setDefaultRequestOptions(options)
        recycler_view.layoutManager =
            GridLayoutManager(this, itemCount, GridLayoutManager.VERTICAL, false)
        adapter = Adapter(this, glide, mainViewModel.response.value!!, {

        })
        recycler_view.adapter = adapter
    }

    /**
     *  For Fetching data
     */
    private fun getJsonData() {
        var json = ""
        if (mainViewModel.getPageCount() == 0) {
            json = CommonUtil.getDataFromAsset(this, "CONTENTLISTINGPAGE-PAGE1.json")!!
        } else if (mainViewModel.getPageCount() == 1) {
            json = CommonUtil.getDataFromAsset(this, "CONTENTLISTINGPAGE-PAGE2.json")!!
        } else if (mainViewModel.getPageCount() == 2) {
            json = CommonUtil.getDataFromAsset(this, "CONTENTLISTINGPAGE-PAGE3.json")!!
        } else return
        mainViewModel.getDataFromJson(json)
    }

}