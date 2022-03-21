package group.whiterabbit.coding_test.ui.viewmodel

import android.content.Context
import android.widget.Toast
import androidx.lifecycle.ViewModel
import group.whiterabbit.coding_test.repository.Repository

abstract class BaseViewModel(private val repository: Repository) : ViewModel() {

    fun showToast(context: Context?, message: String?) {
        if (message.isNullOrEmpty() || context == null) {
            return
        }
        Toast.makeText(context, message, Toast.LENGTH_SHORT).show()

    }

}