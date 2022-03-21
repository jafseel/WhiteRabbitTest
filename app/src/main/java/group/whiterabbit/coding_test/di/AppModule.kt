package group.whiterabbit.coding_test.di

import android.content.Context
import androidx.room.Room
import group.whiterabbit.coding_test.helper.PreferenceUtil
import group.whiterabbit.coding_test.network.ApiCallServiceTask
import group.whiterabbit.coding_test.repository.Repository
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import group.whiterabbit.coding_test.dao.EmployeeDao
import group.whiterabbit.coding_test.database.EmployeeDatabase
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object AppModule {

    @Provides
    @Singleton
    fun provideApiCallServiceTask(@ApplicationContext context: Context): ApiCallServiceTask =
        ApiCallServiceTask(context)

    @Provides
    @Singleton
    fun provideRepository(
        @ApplicationContext context: Context,
        apiCallServiceTask: ApiCallServiceTask,
        preferenceUtil: PreferenceUtil,
    ): Repository =
        Repository(context, apiCallServiceTask, preferenceUtil)

    @Provides
    @Singleton
    fun providePreferenceUtil(@ApplicationContext context: Context): PreferenceUtil =
        PreferenceUtil(context)

    @Provides
    fun provideEmployeeDao(employeeDatabase: EmployeeDatabase): EmployeeDao =
        employeeDatabase.employeeDao()

    @Provides
    @Singleton
    fun provideEmployeeDatabase(@ApplicationContext context: Context): EmployeeDatabase =
        Room.databaseBuilder(context, EmployeeDatabase::class.java, "EmployeesDB").build()

}