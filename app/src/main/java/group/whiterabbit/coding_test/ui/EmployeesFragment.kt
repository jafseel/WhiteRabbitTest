package group.whiterabbit.coding_test.ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.os.bundleOf
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import dagger.hilt.android.AndroidEntryPoint
import group.whiterabbit.coding_test.R
import group.whiterabbit.coding_test.databinding.FragmentEmployeesBinding
import group.whiterabbit.coding_test.ui.viewmodel.EmployeeViewModel

@AndroidEntryPoint
class EmployeesFragment : BaseFragment<FragmentEmployeesBinding>() {

    private val viewModel: EmployeeViewModel by viewModels()

    override val bindingInflater: (LayoutInflater, ViewGroup?, Boolean) -> FragmentEmployeesBinding
        get() = FragmentEmployeesBinding::inflate

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding?.viewModel = viewModel
        binding?.lifecycleOwner = this
        viewModel.getEmployees(view)
        viewModel.employeeDetailNavigation = {
            findNavController().navigate(
                R.id.action_employeesFragment_to_employeesDetailsFragment,
                bundleOf("empId" to it)
            )
        }
    }

}