package com.tech.baptista.bookstore.repository.network

import com.squareup.moshi.Moshi
import com.squareup.moshi.kotlin.reflect.KotlinJsonAdapterFactory
import retrofit2.Call
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory
import retrofit2.converter.scalars.ScalarsConverterFactory
import retrofit2.http.GET
import retrofit2.http.Query

private const val BASE_URL = "https://www.googleapis.com/books/v1/"
//private const val BASE_URL = "https://mars.udacity.com/"

/**
 * Build the Moshi object that Retrofit will be using, making sure to add the Kotlin adapter for
 * full Kotlin compatibility.
 */
private val moshi = Moshi.Builder()
    .add(KotlinJsonAdapterFactory())
    .build()

private val retrofit = Retrofit.Builder()
    //.addConverterFactory(ScalarsConverterFactory.create())
    .addConverterFactory(MoshiConverterFactory.create(moshi))
    .baseUrl(BASE_URL)
    .build()

/**
 * A public interface that exposes the [getBooks] method
 */
interface BookStoreApiService {
    /**
     * Returns a Coroutine [List] of [Books] which can be fetched with await() if in a Coroutine scope.
     * The @GET annotation indicates that the "volumes" endpoint will be requested with the GET
     * HTTP method
     */
    @GET("volumes")
    suspend fun getJsonResponse(
        @Query("q") search: String = "ios",
        @Query("maxResults") size: Int = 20,
        @Query("startIndex") start: Int = 20,

    ): NetworkBookContainer
    //fun getJsonResponse( @Query("filter") type:String= "ios" ): Call<String>
}

/**
 * A public Api object that exposes the lazy-initialized Retrofit service
 */
object BookStoreApi {
    val retrofitService: BookStoreApiService by lazy { retrofit.create(BookStoreApiService::class.java) }
}