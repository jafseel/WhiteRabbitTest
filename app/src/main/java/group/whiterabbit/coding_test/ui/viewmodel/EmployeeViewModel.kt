package group.whiterabbit.coding_test.ui.viewmodel

import android.view.View
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import group.whiterabbit.coding_test.dao.EmployeeDao
import group.whiterabbit.coding_test.databind.ItemViewModel
import group.whiterabbit.coding_test.databind.RecyclerViewOnItemClicked
import group.whiterabbit.coding_test.model.Employee
import group.whiterabbit.coding_test.network.ApiBody
import group.whiterabbit.coding_test.repository.Repository
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class EmployeeViewModel @Inject constructor(
    private val repository: Repository,
    private val employeeDao: EmployeeDao
) : BaseViewModel(repository) {

    var employeeDetailNavigation: ((Long) -> Unit)? = null
    private var mEmployees = emptyList<Employee>()
    val employees: LiveData<List<ItemViewModel>> get() = _employees
    private val _employees = MutableLiveData<List<ItemViewModel>>(emptyList())
    val employeeSelected = object : RecyclerViewOnItemClicked {
        override fun onItemClicked(item: ItemViewModel, position: Int) {
            if (item is Employee) {
                employeeDetailNavigation?.invoke(item.id)
            }
        }
    }

    val employee: LiveData<Employee> get() = _employee
    private var _employee = MutableLiveData<Employee>()

    private fun getEmployeesApi(view: View?) {
        viewModelScope.launch {
            val list: List<Employee>? =
                repository.apiCallList(
                    ApiBody("employeeList", null, "5d565297300000680030a986"),
                    Employee::class.java,
                    view
                )
            if (!list.isNullOrEmpty()) {
//                _employees.value = list!!
                employeeDao.insertAll(list)
            }
        }
    }

    fun getEmployees(view: View?) {
        if (!mEmployees.isNullOrEmpty()){
            return
        }
        viewModelScope.launch {
            employeeDao.getEmployees().collect { employees ->
                if (!employees.isNullOrEmpty()) {
                    mEmployees = employees
                    _employees.value = mEmployees
                } else {
                    getEmployeesApi(view)
                }
            }
        }
    }

    fun getEmployee(empId: Long) {
        viewModelScope.launch {
            employeeDao.getEmployee(empId).collectLatest { _employee.value = it }
        }
    }

    var searchQuery = ""
    fun searchEmployee(search: String) {
        if (mEmployees.isNullOrEmpty()) {
            return
        }
        if (search.isEmpty()) {
            _employees.value =
                mEmployees
            return
        }
        _employees.value =
            mEmployees.filter { it.name.contains(search, true) || it.email.contains(search, true) }

    }
}