package com.jar.demo.base

import androidx.appcompat.app.ActionBar
import androidx.fragment.app.Fragment

/**
 * Interface used by fragments to communicate with the base/host activity.
 */
interface IBaseActionListener {
    fun replaceFragment(fragment: Fragment, addToBackStack: Boolean, tag: String)
    fun showToast(message: String, length: Int)
    fun getActivitySupportActionBar(): ActionBar?
    fun showLoadingCancellable()
    fun hideLoadingCancellable()
}