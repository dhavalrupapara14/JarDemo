package com.jar.demo.injection.module

import com.jar.demo.BuildConfig
import com.jar.demo.BuildConfig.BASE_URL
import com.jar.demo.network.ServiceApi
import dagger.Module
import dagger.Provides
import okhttp3.Interceptor
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory
import java.util.concurrent.TimeUnit
import javax.inject.Singleton

/**
 * Module which provides all required dependencies about network
 * Used for Dagger DI
 */
@Module
class NetworkModule {

    /**
     * Provides the Post service implementation.
     * @param retrofit the Retrofit object used to instantiate the service
     * @return the Post service implementation.
     */
    @Provides
    @Singleton
    internal fun providePostApi(retrofit: Retrofit): ServiceApi {
        return retrofit.create(ServiceApi::class.java)
    }

    /**
     * Provides the Retrofit object.
     * @return the Retrofit object
     */
    @Provides
    @Singleton
    internal fun provideRetrofitInterface(): Retrofit {
        val loggingInterceptor: HttpLoggingInterceptor = HttpLoggingInterceptor().apply {
            this.level = HttpLoggingInterceptor.Level.BODY
        }

        val headerInterceptor = Interceptor {
            it.proceed(
                it.request().newBuilder()
                    .addHeader(
                        "Authorization",
                        "Client-ID " + "add your access key" //TODO: add your unsplash api access key here.
                    ).build()
            )
        }

        val clientBuilder: OkHttpClient.Builder = OkHttpClient.Builder()
        clientBuilder.readTimeout(3 * 60, TimeUnit.SECONDS)
        clientBuilder.connectTimeout(60, TimeUnit.SECONDS)
        if (BuildConfig.DEBUG) {
            clientBuilder.addInterceptor(loggingInterceptor)
        }
        clientBuilder.addInterceptor(headerInterceptor)

        return Retrofit.Builder()
            .baseUrl(BASE_URL)
            .addConverterFactory(MoshiConverterFactory.create())
            .client(clientBuilder.build())
            .build()
    }

}