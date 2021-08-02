package com.jar.demo.repository

import com.jar.demo.JarApp
import com.jar.demo.base.BaseRepository
import com.jar.demo.network.ApiResponseWrapper
import com.jar.demo.network.ServiceApi
import com.jar.demo.network.model.ImageListResponse
import com.jar.demo.network.model.Photo
import kotlinx.coroutines.Dispatchers
import javax.inject.Inject

/**
 * Repository for Task 2 Fragment. Contains functions to fetch the required data.
 */
class Task2Repository @Inject constructor() : BaseRepository() {

    @Inject
    lateinit var serviceApi: ServiceApi

    init {
        JarApp.getAppComponent().inject(this)
    }

    /**
     * Get Unsplash public api photos list.
     */
    suspend fun fetchPhotosList(page: Int, limit: Int) : ApiResponseWrapper<List<Photo>> {
        return callApi(Dispatchers.IO) {
            serviceApi.fetchPhotosList(page = page, limit = limit)
        }
    }

    /**
     * Get images list from another public api for testing purpose.
     */
    suspend fun fetchImagesList(page: Int, limit: Int) : ApiResponseWrapper<ImageListResponse> {
        return callApi(Dispatchers.IO) {
            serviceApi.fetchImagesList(page = page, limit = limit)
        }
    }
}