package com.jar.demo.ui.activity

import android.os.Bundle
import androidx.appcompat.app.ActionBar
import androidx.fragment.app.Fragment
import androidx.fragment.app.commit
import androidx.lifecycle.lifecycleScope
import com.jar.demo.JarApp
import com.jar.demo.R
import com.jar.demo.base.BaseActivity
import com.jar.demo.ui.fragment.home.HomeFragment
import com.jar.demo.utils.AppSharedPref
import com.jar.demo.utils.glide.ImageUtils
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import javax.inject.Inject

/**
 * Main launcher activity which hosts all fragments.
 * We don't need view model for this activity as it has no purpose as of now.
 */
class MainActivity : BaseActivity() {
    @Inject
    lateinit var imageUtils: ImageUtils

    @Inject
    lateinit var appSharedPref: AppSharedPref

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        JarApp.getAppComponent().inject(this)

        // replace home fragment in the container.
        replaceFragment(
            HomeFragment.newInstance(),
            false,
            HomeFragment::class.java.simpleName
        )

        clearGlideCacheIfRequired()
    }

    /**
     * Method used to replace the fragment in this activity.
     * This gets called mostly from fragments.
     */
    override fun replaceFragment(fragment: Fragment, addToBackStack: Boolean, tag: String) {
        supportFragmentManager.commit {
            if (addToBackStack)
                addToBackStack(tag)

            replace(R.id.container, fragment, tag)
        }
    }

    /**
     * Method used to get support action bar if available.
     * This gets called mostly from fragments.
     */
    override fun getActivitySupportActionBar(): ActionBar? {
        return supportActionBar
    }

    /**
     * Check if glide cache is expired, if yes then clear it.
     * Current expiry duration is 24 hours.
     */
    private fun clearGlideCacheIfRequired() {
        val lastGlideCacheClearedTime = appSharedPref.getLastGlideCacheClearedTime()

        if (lastGlideCacheClearedTime > 0) {
            // if (current time - last time)  > 24 hours
            if ((System.currentTimeMillis() - lastGlideCacheClearedTime) > (1000 * 3600 * 24)) {
                // Clear both cache
                imageUtils.clearMemoryCache()

                lifecycleScope.launch(Dispatchers.Default) {
                    // This will be performed quickly but it needs to be called on background thread.
                    // So no need to worry about cancelling this coroutine.
                    imageUtils.clearDiskCache()

                    withContext(Dispatchers.Main) {
                        appSharedPref.setLastGlideCacheClearedTime(System.currentTimeMillis())
//                        showToast("Cache cleared", Toast.LENGTH_SHORT)
                    }
                }
            }

        } else {
            //Set first time after app install.
            appSharedPref.setLastGlideCacheClearedTime(System.currentTimeMillis())
        }
    }
}