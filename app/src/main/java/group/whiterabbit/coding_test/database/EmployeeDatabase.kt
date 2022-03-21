package group.whiterabbit.coding_test.database

import androidx.room.Database
import androidx.room.RoomDatabase
import group.whiterabbit.coding_test.dao.EmployeeDao
import group.whiterabbit.coding_test.model.Employee

@Database(entities = [Employee::class], version = 1)
abstract class EmployeeDatabase : RoomDatabase() {
    abstract fun employeeDao():EmployeeDao

}