package com.jar.demo.base

import android.content.Context
import androidx.appcompat.app.ActionBar
import androidx.fragment.app.Fragment
import com.google.android.material.snackbar.Snackbar
import com.jar.demo.R
import com.jar.demo.network.ApiResponseWrapper

/**
 * Parent class for all fragment classes
 */
abstract class BaseFragment : Fragment() {

    // Interface used by fragments to communicate with the base/host activity.
    private lateinit var baseActionListener: IBaseActionListener

    override fun onAttach(context: Context) {
        super.onAttach(context)
        if (context is IBaseActionListener)
            baseActionListener = context
    }

    /**
     * Replace the fragment in the host activity
     */
    protected fun replaceFragment(fragment: Fragment, addToBackStack: Boolean, tag: String) {
        baseActionListener.replaceFragment(fragment, addToBackStack, tag)
    }

    /**
     * Get support action bar if available.
     */
    protected fun getActivitySupportActionBar(): ActionBar? {
        return baseActionListener.getActivitySupportActionBar()
    }

    /**
     * Show toast using given message and toast length.
     */
    protected fun showToast(message: String, length: Int) {
        baseActionListener.showToast(message, length)
    }

    /**
     * Shows cancellable progress dialog.
     */
    protected fun showLoadingCancellable() {
        baseActionListener.showLoadingCancellable()
    }

    /**
     * Hides cancellable progress dialog.
     */
    protected fun hideLoadingCancellable() {
        baseActionListener.hideLoadingCancellable()
    }

    /**
     * Set title and back button values of Action bar.
     */
    protected fun setUpActionBar(title: String, setDisplayHomeAsUpEnabled: Boolean) {
        getActivitySupportActionBar()?.title = title
        getActivitySupportActionBar()?.setDisplayHomeAsUpEnabled(setDisplayHomeAsUpEnabled)
    }

    /**
     * Common method to handle different type of api errors.
     */
    protected fun handleApiError(response: ApiResponseWrapper<Any>) {
        when(response) {
            is ApiResponseWrapper.NetworkError -> {
                showToast(getString(R.string.no_internet), Snackbar.LENGTH_SHORT)
            }
            is ApiResponseWrapper.TimeOutError -> {
                showToast(getString(R.string.api_timeout), Snackbar.LENGTH_SHORT)
            }
            is ApiResponseWrapper.Error -> {
                showToast(response.errorMessage ?: "", Snackbar.LENGTH_LONG)
            }
            else -> {}
        }
    }
}