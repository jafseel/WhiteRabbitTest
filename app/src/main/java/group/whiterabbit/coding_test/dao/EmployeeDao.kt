package group.whiterabbit.coding_test.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import group.whiterabbit.coding_test.model.Employee
import kotlinx.coroutines.flow.Flow

@Dao
interface EmployeeDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(user: Employee)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAll(user: List<Employee>)

    @Query("SELECT * FROM employee ORDER BY id ASC")
    fun getEmployees(): Flow<List<Employee>>

    @Query("select * from employee where id= :id")
    fun getEmployee(id: Long): Flow<Employee>
}