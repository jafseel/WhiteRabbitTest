package group.whiterabbit.coding_test.network

import okhttp3.RequestBody

data class ApiBody  (
    val tag: String,
    val body: RequestBody?,
    val url: String,
    val progressType: Int = PROGRESS_TRANSPERENT,
    val parameter: String = ""
) {
    companion object {
        const val PROGRESS_TRANSPERENT = 1
        const val PROGRESS_WHITE = 2
    }
}