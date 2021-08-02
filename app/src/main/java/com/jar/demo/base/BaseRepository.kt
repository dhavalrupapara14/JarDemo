package com.jar.demo.base

import com.jar.demo.network.ApiResponseWrapper
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.withContext
import okhttp3.ResponseBody
import org.json.JSONObject
import retrofit2.HttpException
import retrofit2.Response
import java.io.IOException
import java.net.SocketTimeoutException


/**
 * Parent class for all repository classes
 */
abstract class BaseRepository {

    /**
     * Higher order function to perform api call and parse response or error.
     */
    protected suspend fun <T> callApi(
        dispatcher: CoroutineDispatcher,
        apiCall: suspend () -> Response<T>
    ): ApiResponseWrapper<T> {
        return withContext(dispatcher) {
            try {
                val httpResponse: Response<T> = apiCall.invoke()
                if (httpResponse.code() == 200) {
                    ApiResponseWrapper.Success(httpResponse.body()!!)
                } else {
                    ApiResponseWrapper.Error(
                        httpResponse.code(),
                        getErrorMessage(httpResponse.errorBody()!!)
                    )
                }
            } catch (error: Throwable) {
                when (error) {
                    is IOException -> {
                        ApiResponseWrapper.NetworkError
                    }
                    is HttpException -> {
                        ApiResponseWrapper.Error(
                            error.code(),
                            getErrorMessage(error.response()?.errorBody()!!)
                        )
                    }
                    is SocketTimeoutException -> {
                        ApiResponseWrapper.TimeOutError
                    }
                    else -> {
                        ApiResponseWrapper.Error(null, error.message)
                    }
                }
            }
        }
    }

    private fun getErrorMessage(responseBody: ResponseBody): String {
        var message: String? = null
        return try {
            message =
                responseBody.string() //once you perform .string(), that value gets removed from responseBody object.
            val jsonObject = JSONObject(message)
            val errorObject = jsonObject.getJSONObject("error")
            errorObject.getString("message")
        } catch (e: Exception) {
            if (message.isNullOrBlank()) e.message!! else message
        }
    }

}