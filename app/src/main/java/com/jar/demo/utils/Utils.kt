package com.jar.demo.utils

import android.content.Context
import android.os.Handler
import android.os.Looper
import android.view.View
import java.net.URLDecoder
import kotlin.math.roundToInt

/**
 * Utility file where we put all utility methods.
 */

/**
 * Disables view for given duration to avoid multiple taps by user
 */
fun disableViewTemporary(
    view: View?,
    millies: Long
) {
    try {
        if (view != null && view.isEnabled) {
            view.isEnabled = false
            Handler(Looper.getMainLooper()).postDelayed({
                //check null as view may became null if activity/fragment is cleared.
                view?.let {
                    it.isEnabled = true
                }
            }, millies)
        }
    } catch (e: Exception) {
        e.printStackTrace()
    }
}

/**
 * Convert dp to pixels
 */
fun dpToPixel(
    dp: Float,
    context: Context
): Int {
    return try {
        (dp * context.resources.displayMetrics.density).roundToInt()
    } catch (e: Exception) {
        e.printStackTrace()
        0
    }
}

/**
 * Extension function for making a view visibility GONE
 */
fun View.gone() {
    this.visibility = View.GONE
}

/**
 * Extension function for making a view visibility VISIBLE
 */
fun View.visible() {
    this.visibility = View.VISIBLE
}

/**
 * Extension function for making a view visibility INVISIBLE
 */
fun View.invisible() {
    this.visibility = View.INVISIBLE
}

/**
 * Decode url using URL decoder.
 */
fun decodeUrl(inputUrl: String): String {
    return try {
        URLDecoder.decode(inputUrl.trim(), "UTF-8")
    } catch (e1: Exception) {
        inputUrl.replace("\\u0026", "&")
    }
}