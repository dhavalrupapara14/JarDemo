package com.jar.demo.ui.fragment.task2

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.jar.demo.repository.Task2Repository
import com.jar.demo.utils.FACTORY_ERROR_VIEW_MODEL_UNKNOWN

/**
 * View model factory class for task 2 view mode.
 * Right now not using this way as we have a better way to initialize view models.
 */
class Task2ViewModelFactory (private val task2Repository: Task2Repository) : ViewModelProvider.Factory {
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(Task2ViewModel::class.java)) {
//            return Task2ViewModel(task2Repository) as T
            return Task2ViewModel() as T
        }
        throw IllegalArgumentException(FACTORY_ERROR_VIEW_MODEL_UNKNOWN)
    }
}