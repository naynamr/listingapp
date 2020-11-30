package com.example.test.ui.viewmodel

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.test.model.ContentItem
import com.example.test.model.MovieJsonResponse
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken


class MainViewModel : ViewModel() {

    private val _response = MutableLiveData<ArrayList<ContentItem>>()
    val response: LiveData<ArrayList<ContentItem>>
        get() = _response

    private var _originalData = ArrayList<ContentItem>()
    private var _pageCount = 0

    fun getPageCount(): Int {
        return _pageCount
    }

    private fun setPageCount(count: Int) {
        _pageCount = count
    }

    fun getDataFromJson(json: String) {
        val data: MovieJsonResponse = Gson().fromJson(
            json,
            object : TypeToken<MovieJsonResponse>() {}.type
        )
        setPageCount(data.page.page_num.toInt())
        if (!_response.value.isNullOrEmpty()) {
            var list = _response.value!!
            list.addAll(data.page.content_items.content)
            _response.value = list
        } else {
            _response.value = data.page.content_items.content
        }
        storeOriginalList( _response.value!!)
    }
    private fun storeOriginalList(list: ArrayList<ContentItem>) {
        if (!_originalData.isEmpty()) {
            _originalData.clear()
        }
        _originalData.addAll(list)
    }

    fun getOriginalList() {
        if (!_response.value!!.isEmpty()) {
            _response.value!!.clear()
        }
        _response.value = _originalData
    }

    fun search(query: String) {
        if (!_originalData.isEmpty()) {
            val listSearch: List<ContentItem> = _originalData.filter { item ->
                item.name.toLowerCase().contains(query.toLowerCase())
            }
            _response.value = listSearch as ArrayList<ContentItem>
        }
    }

}