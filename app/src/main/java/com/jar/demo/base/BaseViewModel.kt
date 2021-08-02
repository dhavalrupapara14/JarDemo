package com.jar.demo.base

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.jar.demo.network.ApiResponseWrapper
import com.jar.demo.utils.SingleLiveData

/**
 * Parent class for all ViewModel classes
 */
abstract class BaseViewModel : ViewModel() {
    // Observable for view clicks
    val viewClickLiveData: SingleLiveData<Int> by lazy {
        SingleLiveData()
    }

    // Observable for loading indicator
    val loadingLiveData: MutableLiveData<Boolean> by lazy {
        MutableLiveData()
    }

    // Observable for api error
    val apiErrorLiveData: MutableLiveData<ApiResponseWrapper<Any>> by lazy {
        MutableLiveData()
    }
}