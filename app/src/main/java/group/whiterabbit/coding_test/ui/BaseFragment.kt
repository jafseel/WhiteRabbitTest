package group.whiterabbit.coding_test.ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.viewbinding.ViewBinding

abstract class BaseFragment <VB : ViewBinding> : Fragment() {

    abstract val bindingInflater: (LayoutInflater, ViewGroup?, Boolean) -> VB

    protected var binding: VB? = null

    private var mLeak: View? = null
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = bindingInflater.invoke(inflater, container, false)
        mLeak = binding?.root
        return mLeak
    }

    override fun onDestroyView() {
        super.onDestroyView()
        mLeak = null
//        hideKeyboard()
    }
}