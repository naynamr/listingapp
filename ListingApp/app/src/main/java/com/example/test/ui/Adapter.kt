package com.example.test.ui

import android.content.Context
import android.graphics.drawable.Drawable
import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.RequestManager
import com.example.test.BR
import com.example.test.R
import com.example.test.databinding.ListItemMovieBinding
import com.example.test.model.ContentItem
import com.example.test.util.CommonUtil

class Adapter(
    private val mContext: Context,
    private val mGlide: RequestManager,
    private var galleryList: ArrayList<ContentItem>,
    val loadMore: (Boolean) -> Unit
) :
    RecyclerView.Adapter<Adapter.ViewHolder>() {

    private var query: String = ""
    private val TAG: String = "MainActivity "

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {

        val binding: ListItemMovieBinding = DataBindingUtil.inflate(
            LayoutInflater.from(mContext),
            R.layout.list_item_movie,
            parent,
            false
        )
        return ViewHolder(binding)
    }

    override fun getItemCount(): Int {
        return galleryList.size
    }

    fun updateGalleryListAdapter(list: ArrayList<ContentItem>, queryVal: String) {
        galleryList  = list
        query = queryVal
        notifyDataSetChanged()

        Log.e(TAG, " galleryList ------- " + galleryList.size)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {

        holder.bind(galleryList[position])

        val res: Drawable? = CommonUtil.getImageFromAsset(
            mContext,
            "" + galleryList[position].poster_image
        )

        if (query.isNotEmpty() && galleryList[position].name.toLowerCase()
                .contains(query.toLowerCase())
        ) {
            holder.binding.movieName.setTextColor(
                ContextCompat.getColor(mContext, R.color.colorYellow)
            )
        } else {
            holder.binding.movieName.setTextColor(
                ContextCompat.getColor(mContext, R.color.colorAccent)
            )
        }

        if (res != null) {
            mGlide.load(res)
                .placeholder(R.mipmap.placeholder_for_missing_posters)
                .error(R.mipmap.placeholder_for_missing_posters)
                .into(holder.binding.thumbImg)
        }
    }

    class ViewHolder(val binding: ListItemMovieBinding) : RecyclerView.ViewHolder(binding.root) {

        fun bind(data: Any) {
            binding.setVariable(BR.movie, data)
            binding.executePendingBindings()
        }
    }

}