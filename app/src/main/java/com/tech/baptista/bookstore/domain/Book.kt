package com.tech.baptista.bookstore.domain

import android.icu.text.Collator.ReorderCodes.PUNCTUATION
import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
data class Book(
    val id: String,
    val title: String,
    val authors: String,
    val description: String,
    val imageLink: String,
    val buyLink: String?,
    val favorite: Boolean
) : Parcelable {

    /**
     * Short description is used for displaying truncated descriptions in the UI
     */
    val shortDescription: String
        get() = description.smartTruncate(200)

    /**
     * Truncate long text with a preference for word boundaries and without trailing punctuation.
     */
    private val PUNCTUATION = listOf(", ", "; ", ": ", " ")

    fun String.smartTruncate(length: Int): String {
        val words = split(" ")
        var added = 0
        var hasMore = false
        val builder = StringBuilder()
        for (word in words) {
            if (builder.length > length) {
                hasMore = true
                break
            }
            builder.append(word)
            builder.append(" ")
            added += 1
        }

        PUNCTUATION.map {
            if (builder.endsWith(it)) {
                builder.replace(builder.length - it.length, builder.length, "")
            }
        }

        if (hasMore) {
            builder.append("...")
        }
        return builder.toString()
    }
}