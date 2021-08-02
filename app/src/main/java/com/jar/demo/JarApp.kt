package com.jar.demo

import android.app.Application
import com.jar.demo.injection.component.AppComponent
import com.jar.demo.injection.component.DaggerAppComponent
import com.jar.demo.injection.module.AppModule

/**
 * Application class
 */
class JarApp : Application() {
    companion object {
        private var appComponent: AppComponent? = null
        private var instance: JarApp? = null

        fun getAppComponent(): AppComponent {
            return appComponent!!
        }

        /**
         * Using this instance, we can access any method from this class.
         */
        fun getInstance(): JarApp {
            return instance ?: JarApp()
        }
    }

    override fun onCreate() {
        super.onCreate()
        instance = this
        // initialize dagger app component.
        appComponent = DaggerAppComponent.builder()!!.appModule(AppModule(this)).build()
    }

}