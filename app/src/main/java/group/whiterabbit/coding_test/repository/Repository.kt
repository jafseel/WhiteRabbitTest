package group.whiterabbit.coding_test.repository

import android.content.Context
import android.graphics.Color
import android.view.View
import android.widget.LinearLayout
import android.widget.ProgressBar
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.core.text.HtmlCompat
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import group.whiterabbit.coding_test.R
import group.whiterabbit.coding_test.helper.BaseViewModelConnection
import group.whiterabbit.coding_test.helper.PreferenceUtil
import group.whiterabbit.coding_test.network.ApiBody
import group.whiterabbit.coding_test.network.ApiCallServiceTask
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import okhttp3.MediaType
import okhttp3.RequestBody
import org.json.JSONArray
import org.json.JSONObject
import javax.inject.Inject

class Repository @Inject constructor(
    private val context: Context,
    private val apiCallService: ApiCallServiceTask,
    private val preferenceUtil: PreferenceUtil
) {

    suspend fun <T> apiCall(
        apiBody: ApiBody,
        klass: Class<T>,
        view: View?,
        isNextPageUrl: Boolean = false,
        isShowToast: Boolean = true,
        isList: Boolean = false
    ): T? {
        return try {
            val resp = apiCall(apiBody, view, isNextPageUrl, isShowToast)
            Gson().fromJson(resp, klass)
        } catch (e: Exception) {
            null
        }
    }



    suspend fun <T> apiCallList(
        apiBody: ApiBody,
        klass: Class<T>,
        view: View?,
        isNextPageUrl: Boolean = false,
        isShowToast: Boolean = true
    ): List<T>? {
        return try {
            val resp = apiCall(apiBody, view, isNextPageUrl, isShowToast)
            val type = TypeToken.getParameterized(List::class.java, klass).type
            Gson().fromJson(resp, type)
//            Gson().fromJson(resp, object : TypeToken<List<klass>>() {}.type)
        } catch (e: Exception) {
            null
        }
    }



    private suspend fun apiCall(
        apiBody: ApiBody,
        view: View?,
        isNextPageUrl: Boolean = false,
        isShowToast: Boolean = true
    ): String? {
        val llLoading: LinearLayout? = view?.findViewById(R.id.llLoading)
        val progressBar: ProgressBar? = view?.findViewById(R.id.progressBar)
        showProgress(llLoading, apiBody.progressType, progressBar)

        return withContext(Dispatchers.IO) {
            val resp = withContext(Dispatchers.Default) {
                apiCallService.apiCall(
                    apiBody,
                    isNextPageUrl,
                )
            }

            launch(Dispatchers.Main) {
                delay(300)
                hideProgress(llLoading, progressBar, resp, apiBody.progressType)
            }

//            {"status":0,"msg":"This Item Not Exist In Selected Menu. Please Select Any Other Item!"}
            val json: Any? = try {
                if (resp == null) {
                    null
                } else {
                    JSONObject(resp)
                }
            } catch (e: Exception) {
                try {
                    JSONArray(resp)
                } catch (e: Exception) {
                    null
                }
            }

            if (json != null && json is JSONObject) {
                if (isShowToast) {
                    if (json.has("message")) {
                        showToastFromBackground(json.optString("message"))
                    } else if (json.has("Message")) {
                        showToastFromBackground(json.optString("Message"))
                    }
                }

                if (json.has("success") && (json.getInt("success") == 4 || json.getInt("success") == 3)) {
                    logout()
                    return@withContext null
                }
            }


            if (json == null) {
                null
            } else {
                resp
            }
//            resp
        }
    }

    private suspend fun showToastFromBackground(message: String?) {
        withContext(Dispatchers.Main) {
            showToast(message)
        }
    }

//    fun getUserData(): LoginData? {
//        return preferenceUtil.getUserData()
//    }
//
//    fun saveUserData(user: LoginData?): Boolean {
//        return preferenceUtil.saveUserData(user)
//    }

    fun getDeviceToken(): String? {
        return preferenceUtil.getDeviceToken()
    }

    fun storeDeviceToken(token: String?) {
        preferenceUtil.storeDeviceToken(token)
    }

    fun getPreferenceUtil(): PreferenceUtil = preferenceUtil


    private var baseViewModelConnection: BaseViewModelConnection? = null
    fun setBaseViewModel(baseViewModelConnection: BaseViewModelConnection?) {
        this.baseViewModelConnection = baseViewModelConnection
    }

    private suspend fun logout() {
        withContext(Dispatchers.Main) {
            baseViewModelConnection?.logout()
        }
    }

    fun getCartCount(): Int? {
        return preferenceUtil.getCartCount()
    }

    fun getFavouriteCount(): Int? = preferenceUtil.getFavouriteCount()

    fun saveFavouriteCount(count: Int) {
        preferenceUtil.saveFavouriteCount(count)
    }

    private fun showProgress(llLoading: LinearLayout?, type: Int, progressBar: ProgressBar?) {
//        Log.d("hideProgress showProgress is null${progressBar == null}")
        llLoading?.visibility = View.VISIBLE
        progressBar?.visibility = View.VISIBLE
        if (type == ApiBody.PROGRESS_WHITE) {
            llLoading?.setBackgroundColor(Color.WHITE)
        } else {
            llLoading?.setBackgroundColor(Color.TRANSPARENT)
        }
    }

    private fun hideProgress(
        llLoading: LinearLayout?,
        progressBar: ProgressBar?,
        resp: String?,
        progressType: Int
    ) {
//        Log.d("hideProgress progressBar is null${progressBar == null}")
        progressBar?.visibility = View.GONE
        if ((resp != null && resp.trim()
                .isNotEmpty()) || progressType == ApiBody.PROGRESS_TRANSPERENT
        ) {
            llLoading?.visibility = View.GONE
        }
    }


    fun showToast(message: String?) {
        try {
            if (message?.trim().isNullOrEmpty()
                || message.equals("null", true)
                || message.equals("true", true)
            ) {
                return
            }
            val msg: String? = try {
                HtmlCompat.fromHtml(message ?: "", HtmlCompat.FROM_HTML_MODE_COMPACT).toString()
                    .trim()
            } catch (e: Exception) {
                e.printStackTrace()
                message
            } ?: return

            if (!msg.isNullOrEmpty()) {
                Toast.makeText(context, msg, Toast.LENGTH_SHORT).show()
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    fun showAlertConfirmation(
        context: Context?,
        title: String? = null,
        message: String? = null,
        okListener: (() -> Unit)?
    ) {
        try {
            context ?: return
            val alertDialogBuilder = AlertDialog.Builder(context)
            alertDialogBuilder.setTitle(if (title.isNullOrEmpty()) context.getString(R.string.alert_title) else title)
            alertDialogBuilder.setMessage(if (title.isNullOrEmpty()) context.getString(R.string.alert_msg) else message)
            alertDialogBuilder.setPositiveButton(context.getString(R.string.label_yes)) { dialog, _ ->
                dialog.dismiss()
                okListener?.invoke()
            }
            alertDialogBuilder.setNegativeButton(context.getString(R.string.label_no)) { dialog, _ -> dialog.dismiss() }
            val dd = alertDialogBuilder.create()
            dd.show()
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    fun isOnline(): Boolean = apiCallService.isOnline(context)

    fun getRawBOdy(json: Map<String, String>): RequestBody {
//        return JSONObject(json).toString()
//            .toRequestBody("application/json; charset=utf-8".toMediaTypeOrNull())
        return RequestBody.create(
            MediaType.get("application/json; charset=utf-8"),
            JSONObject(json).toString()
        )
    }
}