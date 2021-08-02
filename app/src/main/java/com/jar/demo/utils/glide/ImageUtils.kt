package com.jar.demo.utils.glide

import android.content.Context
import android.widget.ImageView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.jar.demo.JarApp
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.withContext

/**
 * Refer JarAppGlideModule class for information about caching.
 */
class ImageUtils(private val application: JarApp) {

    fun loadImage(url: String, imageView: ImageView, context: Context) {
        Glide.with(context)
            .load(url)
            .diskCacheStrategy(DiskCacheStrategy.ALL)
            .into(imageView)

    }

    /**
     * Clear memory cache of Glide.
     * Note: Call this method on main thread.
     */
    fun clearMemoryCache() {
        Glide.get(application).clearMemory()
    }

    /**
     * Clear disk cache of Glide.
     * Note: Call this method on background thread.
     */
    suspend fun clearDiskCache() = withContext(Dispatchers.Default) {
        Glide.get(application).clearDiskCache()
    }
}