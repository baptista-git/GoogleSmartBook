package com.tech.baptista.bookstore.screens.overview

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.paging.PagingDataAdapter
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.tech.baptista.bookstore.databinding.OverviewItemBinding
import com.tech.baptista.bookstore.domain.Book

class BookPagingAdapter( val onClickListener: BookGridAdapter.OnClickListener) : PagingDataAdapter<Book, BookPagingAdapter.BookViewHolder>(DiffCallback) {

    /**
     * Allows the RecyclerView to determine which items have changed when the [List] of [Book]
     * has been updated.
     */
    companion object DiffCallback : DiffUtil.ItemCallback<Book>() {
        override fun areItemsTheSame(oldItem: Book, newItem: Book): Boolean {
            return oldItem === newItem //objects reference are the same
        }

        override fun areContentsTheSame(oldItem: Book, newItem: Book): Boolean {
            return oldItem.id == newItem.id
        }

    }


    /**
     * The MarsPropertyViewHolder constructor takes the binding variable from the associated
     * GridViewItem, which nicely gives it access to the full [Book] information.
     */
    class BookViewHolder(private var binding: OverviewItemBinding):
        RecyclerView.ViewHolder(binding.root) {
        fun bind(book: Book) {
            binding.book = book
            // This is important, because it forces the data binding to execute immediately,
            // which allows the RecyclerView to make the correct view size measurements
            binding.executePendingBindings()
        }
    }

    /**
     * Custom listener that handles clicks on [RecyclerView] items.  Passes the [Book]
     * associated with the current item to the [onClick] function.
     * @param clickListener lambda that will be called with the current [Book]
     */
    class OnClickListener(val clickListener: (book:Book) -> Unit) {
        fun onClick(book:Book) = clickListener(book)
    }

    override fun onBindViewHolder(holder: BookViewHolder, position: Int) {
        val book = getItem(position)?:return
        holder.itemView.setOnClickListener {
            onClickListener.onClick(book)
        }
        holder.bind(book)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BookViewHolder {
        return BookViewHolder(OverviewItemBinding.inflate(LayoutInflater.from(parent.context)))
    }
}