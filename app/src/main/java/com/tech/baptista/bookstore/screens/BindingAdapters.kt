package com.tech.baptista.bookstore.screens

import android.graphics.drawable.Drawable
import android.util.Log
import android.view.View
import android.widget.ImageView
import androidx.annotation.DrawableRes
import androidx.appcompat.content.res.AppCompatResources
import androidx.core.net.toUri
import androidx.databinding.BindingAdapter
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.tech.baptista.bookstore.R
import com.tech.baptista.bookstore.domain.Book
import com.tech.baptista.bookstore.screens.overview.BookGridAdapter
import com.tech.baptista.bookstore.screens.overview.BooksApiStatus

/**
 * Uses the Glide library to load an image by URL into an [ImageView]
 */
@BindingAdapter("imageUrl")
fun bindImage(imgView: ImageView, imgUrl: String?) {
    imgUrl?.let {
        val imgUri = imgUrl.toUri().buildUpon().scheme("https").build()
        Glide.with(imgView.context)
            .load(imgUri)
            .apply( RequestOptions()
                .placeholder(R.drawable.loading_animation)
                .error(R.drawable.ic_broken_image))
            .into(imgView)
    }
}
/**
 * When there is no Mars property data (data is null), hide the [RecyclerView], otherwise show it.
 */
@BindingAdapter("listData")
fun bindRecyclerView(recyclerView: RecyclerView, data: List<Book>?) {
    val adapter = recyclerView.adapter as BookGridAdapter
    adapter.submitList(data)
}

/**
 * This binding adapter displays the [BooksApiStatus] of the network request in an image view.  When
 * the request is loading, it displays a loading_animation.  If the request has an error, it
 * displays a broken image to reflect the connection error.  When the request is finished, it
 * hides the image view.
 */
@BindingAdapter("bookStoreApiStatus")
fun bindStatus(statusImageView: ImageView, status: BooksApiStatus) {
    when (status) {
        BooksApiStatus.LOADING -> {
            statusImageView.visibility = View.VISIBLE
            statusImageView.setImageResource(R.drawable.loading_animation)
        }
        BooksApiStatus.ERROR -> {
            statusImageView.visibility = View.VISIBLE
            statusImageView.setImageResource(R.drawable.ic_connection_error)
        }
        BooksApiStatus.DONE -> {
            statusImageView.visibility = View.GONE
        }
    }
}

@BindingAdapter("favoriteStatus")
fun bindFab(fab: FloatingActionButton, status: Boolean) {

    if(status == true){
        fab.setImageDrawable(AppCompatResources.getDrawable(fab.context, R.drawable.baseline_favorite_24))
    } else {
        fab.setImageDrawable(AppCompatResources.getDrawable(fab.context, R.drawable.favorite_border))
    }
}
