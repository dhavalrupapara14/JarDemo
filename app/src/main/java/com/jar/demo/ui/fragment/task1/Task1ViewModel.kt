package com.jar.demo.ui.fragment.task1

import android.view.View
import androidx.lifecycle.viewModelScope
import com.jar.demo.R
import com.jar.demo.base.BaseViewModel
import com.jar.demo.utils.SingleLiveData
import com.jar.demo.utils.VIEW_DISABLE_TIME_VERY_SHORT
import com.jar.demo.utils.disableViewTemporary
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.isActive
import kotlinx.coroutines.launch
import java.util.*
import kotlin.math.abs

/**
 * View model class for Task 1 fragment
 */
class Task1ViewModel : BaseViewModel() {

    companion object {
        const val MAX_ITERATION_LIMIT = 1000000 // 10 Lac iterations, it will take max 2-5 seconds.
    }

    // Random X to generate X position for shape
    private val randomX by lazy {
        Random()
    }
    // Random Y to generate Y position for shape
    private val randomY by lazy {
        Random()
    }

    // Shape container width and height in px
    var shapeContainerWidth = 0
    var shapeContainerHeight = 0
    // Shape width and height in px
    var shapeWidthPx = 0
    var shapeHeightPx = 0

    // Stores X, Y of shape relative to it's container.
    val shapePosList: ArrayList<Pair<Int, Int>> by lazy {
        ArrayList()
    }

    // Observable for sending calculated shape pos back to observer.
    val shapePosLiveData: SingleLiveData<Pair<Int, Int>?> by lazy {
        SingleLiveData()
    }

    /**
     * Called when user taps on button.
     */
    fun onClick(view: View) {
        if (view.id != R.id.ivUndo) {
            // disable view for few ms to avoid multiple taps.
            disableViewTemporary(view, VIEW_DISABLE_TIME_VERY_SHORT)
        }
        viewClickLiveData.value = view.id
    }

    fun calculateNextCollisionFreeShapePos() {
        // Launch a coroutine.
        viewModelScope.launch(Dispatchers.Default) {

            // flag to be set when collision is detected.
            var isColliding = false
            var x = -1
            var y = -1
            var count = 0

            do {
                // Get random X and Y
                x = randomX.nextInt(shapeContainerWidth - shapeWidthPx)
                y = randomY.nextInt(shapeContainerHeight - shapeHeightPx)

                // Check whether this position is colliding with any existing shapes or not
                for (shapePos in shapePosList) {
                    if (
                        abs(x - shapePos.first) <= shapeWidthPx
                        && abs(y - shapePos.second) <= shapeHeightPx
                    ) {
                        // collision detected.
                        isColliding = true
                        break
                    } else {
                        isColliding = false
                    }
                }

                count++

                // repeat it until we get X,Y which doesn't collide with any other existing shapes.
                // repeat it only if coroutine is active and max iteration limit is not exceeded.
            } while (isColliding && isActive && count < MAX_ITERATION_LIMIT)

            // Return value back to main thread
            shapePosLiveData.postValue(
                if (count >= MAX_ITERATION_LIMIT) {
                    Pair(-1, -1) //invalid position
                } else {
                    Pair(x, y)
                }
            )
        }
    }
}