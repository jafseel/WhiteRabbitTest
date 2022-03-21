package group.whiterabbit.coding_test.helper

import android.util.Log
import group.whiterabbit.coding_test.BuildConfig
object Log{
        private const val TAG = "TSET_TAG"

        fun d(message: String, tag: String = TAG) {
            if (BuildConfig.DEBUG) {
                Log.d(tag, message)
            }
        }

        fun e(message: String, tag: String = TAG) {
            Log.e(tag, message)
        }

        fun w(message: String, tag: String = TAG, exception: Exception? = null) {
            if (BuildConfig.DEBUG) {
                Log.w(tag, message, exception)
            }
        }

}