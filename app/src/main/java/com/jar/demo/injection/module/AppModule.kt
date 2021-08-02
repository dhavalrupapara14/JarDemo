package com.jar.demo.injection.module

import com.jar.demo.JarApp
import com.jar.demo.utils.AppSharedPref
import com.jar.demo.utils.glide.ImageUtils
import dagger.Module
import dagger.Provides
import javax.inject.Singleton

/**
 * App module class which has functions to provide dependencies.
 * Used for Dagger DI
 */
@Module
class AppModule(private val application: JarApp) {

    @Singleton
    @Provides
    internal fun providesApplication(): JarApp {
        return application
    }

    @Singleton
    @Provides
    internal fun providesImageUtils(): ImageUtils {
        return ImageUtils(application)
    }

    @Singleton
    @Provides
    internal fun providesSharedPref(): AppSharedPref {
        return AppSharedPref(application)
    }
}