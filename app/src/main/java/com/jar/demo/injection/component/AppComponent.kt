package com.jar.demo.injection.component

import com.jar.demo.injection.module.AppModule
import com.jar.demo.injection.module.NetworkModule
import com.jar.demo.repository.Task2Repository
import com.jar.demo.ui.activity.MainActivity
import com.jar.demo.ui.fragment.task3.ImagesAdapter
import com.jar.demo.ui.fragment.task2.PhotoAdapter
import com.jar.demo.ui.fragment.task2.Task2ViewModel
import dagger.Component
import javax.inject.Singleton

/**
 * Application component class for Dagger DI
 */
@Singleton
@Component(
    modules = [NetworkModule::class, AppModule::class]
)
interface AppComponent {
    fun inject(task2Repository: Task2Repository)
    fun inject(task2ViewModel: Task2ViewModel)
    fun inject(photoAdapter: PhotoAdapter)
    fun inject(imagesAdapter: ImagesAdapter)
    fun inject(mainActivity: MainActivity)
}