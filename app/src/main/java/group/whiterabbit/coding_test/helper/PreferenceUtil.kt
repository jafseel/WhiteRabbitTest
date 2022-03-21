package group.whiterabbit.coding_test.helper

import android.content.Context
import android.content.SharedPreferences
import javax.inject.Inject

class PreferenceUtil @Inject constructor(private val context: Context) {
    private val sharedPref: SharedPreferences =
        context.getSharedPreferences("KruuzPref", Context.MODE_PRIVATE)
    private val editor: SharedPreferences.Editor = sharedPref.edit()

    fun storeDeviceToken(token: String?) {
        editor.putString("token", token).commit()
    }

    fun getDeviceToken(): String? = sharedPref.getString("token", null)
    fun getAppLanguage(): String = sharedPref.getString("language", "en") ?: "en"
    fun updateAppLanguage(language: String): Boolean =
        editor.putString("language", language).commit()

//    fun getUserData(): LoginData? {
//        try {
//            return Gson().fromJson(
//                sharedPref.getString("loginData", null),
//                LoginData::class.java
//            )
//        } catch (e: Exception) {
//            e.printStackTrace()
//        }
//        return null
//    }
//
//    fun saveUserData(user: LoginData?): Boolean {
//        Log.d("saveUserData data: $user")
//        return if (user != null) {
//            editor.putString("loginData", Gson().toJson(user)).commit()
//        } else {
//            editor.putString("loginData", null).commit()
//        }
//    }

    fun getCartCount(): Int {
        return sharedPref.getInt("cartCount", 0)
    }

    fun saveCartCount(cartTotal: Int): Boolean {
        return editor.putInt("cartCount", cartTotal).commit()
    }

    fun saveFavouriteCount(count: Int) {
        editor.putInt("favouriteCount", count).commit()
    }

    fun changeFirstTime() {
        editor.putBoolean("isFirstTime", false).commit()
    }


    fun notificationOnOff(notificationEnabled: Boolean) {
        editor.putBoolean("notificationEnabled", notificationEnabled).commit()
    }

    fun notificationOnOff() = sharedPref.getBoolean("notificationEnabled", true)

    fun isFirstTime() = sharedPref.getBoolean("isFirstTime", true)

    fun getFavouriteCount(): Int = sharedPref.getInt("favouriteCount", 0)

//    fun getCartItems(): String {
//        return sharedPref.getString("cartItems", "[]") ?: "[]"
//    }
//
//    fun updateCartItems(list: List<CartProductModel>): Boolean {
//        editor.putString("cartItems", Gson().toJson(list) ?: "[]")
//        editor.putInt("cartCount", list.size)
//        return editor.commit()
//    }
//
//    fun getFavouriteItems(): String {
//        return sharedPref.getString("favouriteItems", "[]") ?: "[]"
//    }
//
//    fun updateFavouriteItems(list: List<CartProductModel>): Boolean {
//        editor.putString("favouriteItems", Gson().toJson(list) ?: "[]")
//        editor.putInt("favouriteCount", list.size)
//        return editor.commit()
//    }
//
//    fun removeALlCart(): Boolean {
//        editor.putString("cartItems", "[]")
//        editor.putInt("cartCount", 0)
//        return editor.commit()
//    }
}