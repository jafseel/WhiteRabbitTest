package group.whiterabbit.coding_test.ui

import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import dagger.hilt.android.AndroidEntryPoint
import group.whiterabbit.coding_test.R
import group.whiterabbit.coding_test.databinding.FragmentEmployeeDetailsBinding
import group.whiterabbit.coding_test.databinding.FragmentEmployeesBinding
import group.whiterabbit.coding_test.databinding.FragmentSplashBinding
import group.whiterabbit.coding_test.helper.Log
import group.whiterabbit.coding_test.ui.viewmodel.EmployeeViewModel

@AndroidEntryPoint
class EmployeeDetailsFragment : BaseFragment<FragmentEmployeeDetailsBinding>() {
    override val bindingInflater: (LayoutInflater, ViewGroup?, Boolean) -> FragmentEmployeeDetailsBinding
        get() = FragmentEmployeeDetailsBinding::inflate

    private val viewModel: EmployeeViewModel by viewModels()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding?.viewModel = viewModel
        binding?.lifecycleOwner = this

        arguments?.getLong("empId")?.let {
            viewModel.getEmployee(it)
        }

    }

}