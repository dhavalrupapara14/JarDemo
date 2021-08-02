package com.jar.demo.utils

import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Observer

/**
 * Custom live data class to clear live data values after it is used once.
 * https://stackoverflow.com/questions/44582019/how-to-clear-livedata-stored-value
 */
class SingleLiveData<T> : MutableLiveData<T?>() {

    override fun observe(owner: LifecycleOwner, observer: Observer<in T?>) {
        super.observe(owner, Observer { t ->
            if (t != null) {
                observer.onChanged(t)
                postValue(null)
            }
        })
    }
}