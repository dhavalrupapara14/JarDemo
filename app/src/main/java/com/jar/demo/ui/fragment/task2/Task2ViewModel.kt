package com.jar.demo.ui.fragment.task2

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.jar.demo.JarApp
import com.jar.demo.base.BaseViewModel
import com.jar.demo.network.ApiResponseWrapper
import com.jar.demo.network.model.ImageListResponse
import com.jar.demo.network.model.Photo
import com.jar.demo.repository.Task2Repository
import kotlinx.coroutines.launch
import javax.inject.Inject

/**
 * View model class used for Task 2 and Task 3 both.
 */
class Task2ViewModel/*(private val task2Repository: Task2Repository)*/ : BaseViewModel() {

    @Inject
    lateinit var task2Repository: Task2Repository
    var page = 0
    val limit = 30

    val imageListResponseLiveData: MutableLiveData<ImageListResponse> = MutableLiveData()
    val photosListResponseLiveData: MutableLiveData<List<Photo>> = MutableLiveData()

    init {
        JarApp.getAppComponent().inject(this)
    }

    /**
     * Get Unsplash public api photos list.
     */
    fun getPhotosList() {
        loadingLiveData.postValue(true)
        viewModelScope.launch {
            val response = task2Repository.fetchPhotosList(page = page + 1, limit = limit)

            if (response is ApiResponseWrapper.Success) {
                page++
                loadingLiveData.postValue(false)
                photosListResponseLiveData.postValue(response.value)
            } else {
                page--
                loadingLiveData.postValue(false)
                apiErrorLiveData.postValue(response)
            }
        }
    }

    fun getImagesList() {
        loadingLiveData.postValue(true)
        viewModelScope.launch {
            val response = task2Repository.fetchImagesList(page = page + 1, limit = limit)

            if (response is ApiResponseWrapper.Success) {
                page++
                loadingLiveData.postValue(false)
                imageListResponseLiveData.postValue(response.value)
            } else {
                page--
                loadingLiveData.postValue(false)
                apiErrorLiveData.postValue(response)
            }
        }
    }
}