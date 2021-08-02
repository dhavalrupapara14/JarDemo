package com.jar.demo.ui.fragment.home

import android.view.View
import com.jar.demo.base.BaseViewModel
import com.jar.demo.utils.VIEW_DISABLE_TIME_VERY_SHORT
import com.jar.demo.utils.disableViewTemporary

/**
 * View model class for home fragment.
 */
class HomeViewModel : BaseViewModel() {

    /**
     * Called when user taps on button.
     */
    fun onClick(view: View) {
        // disable view for few ms to avoid multiple taps.
        disableViewTemporary(view, VIEW_DISABLE_TIME_VERY_SHORT)
        viewClickLiveData.value = view.id
    }
}