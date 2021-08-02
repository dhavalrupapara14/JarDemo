package com.jar.demo.network

import com.jar.demo.network.model.ImageListResponse
import com.jar.demo.network.model.Photo
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Query

/**
 * Retrofit API interface containing all api methods.
 */
interface ServiceApi {
    /**
     * Unsplash public api
     * Demo mode app has 50 request/hr limit
     */
    @GET("photos")
    suspend fun fetchPhotosList(
        @Query("order_by") orderBy: String = "popular",
        @Query("page") page: Int,
        @Query("per_page") limit: Int = 10
    ): Response<List<Photo>>

    /**
     * Public api to return animation drama list.
     * For testing purpose
     */
    @GET("anime")
    suspend fun fetchImagesList(
        @Query("q") keyword: String = "naruto",
        @Query("page") page: Int,
        @Query("limit") limit: Int = 10
    ): Response<ImageListResponse>
}