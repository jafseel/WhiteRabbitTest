package group.whiterabbit.coding_test.network

import android.content.Context
import android.net.ConnectivityManager
import android.widget.Toast
import group.whiterabbit.coding_test.BuildConfig
import group.whiterabbit.coding_test.helper.Constants
import group.whiterabbit.coding_test.helper.Log
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import okhttp3.*
import okio.Buffer
import java.io.IOException
import java.net.InetSocketAddress
import java.net.Socket
import java.util.concurrent.TimeUnit
import javax.inject.Inject

class ApiCallServiceTask @Inject constructor(private val context: Context) {
    private val API_URL = Constants.BASE_URL + "v2/"

    private val client = OkHttpClient.Builder()
        .connectTimeout(30, TimeUnit.SECONDS)
        .writeTimeout(30, TimeUnit.SECONDS)
        .readTimeout(30, TimeUnit.SECONDS)
        .build()


    suspend fun apiCall(
        apiBody: ApiBody,
        isNextPageUrl: Boolean,
    ): String? {

        if (withContext(Dispatchers.Main) { !isOnline(context) }) {
            Log.d("No Internet Connection")
            withContext(Dispatchers.Main) {
                Toast.makeText(context, "No Internet Connection...", Toast.LENGTH_SHORT).show()
            }
            return ""
        }

        val builder = Request.Builder()
        val url = if (!isNextPageUrl) {
            API_URL + apiBody.url
        } else {
            apiBody.url
        }
        builder.url(url)
        builder.tag(apiBody.tag)

        val fromBody = if (apiBody.body != null) {
            addCommonParameters(apiBody.body)
        } else {
            apiBody.body
        }
        val request = if (fromBody == null) {
//            builder.get().build()
            builder.build()
        } else {
            builder.post(fromBody).build()
        }

        try {
            if (BuildConfig.DEBUG) {
                Log.d("api url:$url")
                if (request.body() != null) {
//                    val buffer = Buffer()
//                    request.body()!!.writeTo(buffer)
//                    Log.d("api parameters:" + buffer.readUtf8())
                    Log.d("api parameters:" + bodyToString(request.body()))
                }
                Log.d("api header: ${request.headers()}")
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }

        cancelApiCallWithTag(apiBody.tag)

        return try {
            val resp = client.newCall(request).execute()

            val body = if (resp.isSuccessful) {
                resp.body()?.string()
            } else {
                Log.d("Api fail response: ${resp.body()?.string()}")
                null
            }
            Log.d("Api call response: $body")
            body
        } catch (e: Exception) {
            e.printStackTrace()
            null
        }
    }

    private fun addCommonParameters(body: RequestBody?): RequestBody? {
//        return try {
//            var bodyStr = bodyToString(body)
//            val formBody = FormBody.Builder()
//            if (checkParameterIsNotContain(bodyStr, "device_id")) {
//                formBody.add("device_id", getDeviceId(context))
//            }
//
//            if (checkParameterIsNotContain(bodyStr, "device_type")) {
//                formBody.add("device_type", "Android")
//            }
//
////            formBody.add("lang", if (languageCode == "ar") "2" else "1")
//
//            bodyStr += (if (bodyStr?.isNotEmpty() == true) "&" else "") + bodyToString(formBody.build())
//            RequestBody.create(
//                body?.contentType()
//                    ?: MediaType.parse("application/x-www-form-urlencoded;charset=UTF-8"),
//                bodyStr ?: ""
//            )
//        } catch (e: Exception) {
//            e.printStackTrace()
//            body
//        }
        return body
    }

    private fun checkParameterIsNotContain(body: String?, parameter: String): Boolean {
        return body?.startsWith("$parameter=") != true || !body.contains("&$parameter=")
    }

    private fun bodyToString(request: RequestBody?): String? {
        return try {
            val buffer = Buffer()
            if (request != null) request.writeTo(buffer) else return ""
            buffer.readUtf8()
        } catch (e: IOException) {
            null
        }
    }

    fun isOnline(context: Context): Boolean {
        val isOnline = try {
            val connectivityManager =
                context.applicationContext.getSystemService(Context.CONNECTIVITY_SERVICE)
                        as ConnectivityManager
            val networkInfo = connectivityManager.activeNetworkInfo
            return networkInfo != null && networkInfo.isConnected
        } catch (e: Exception) {
            e.printStackTrace()
            false
        }
        return if (isOnline) {
            true
        } else {
            isOnline()
        }
    }

    private fun cancelApiCallWithTag(tag: Any) {
        try {
            for (call in client.dispatcher().queuedCalls()) {
                if (call.request().tag() == tag) {
                    call.cancel()
                    Log.e("ApiCall isCanceled:" + call.isCanceled)
                }
            }
            for (call in client.dispatcher().runningCalls()) {
                if (call.request().tag() == tag) {
                    call.cancel()
                    Log.e("ApiCall isCanceled:" + call.isCanceled)
                }
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }

    }

    private fun isOnline(): Boolean {
        return try {
            val timeoutMs = 1500
            val sock = Socket()
            val socketAddress = InetSocketAddress("8.8.8.8", 53)
            sock.connect(socketAddress, timeoutMs)
            sock.close()
            true
        } catch (e: Exception) {
            false
        }
    }

}
