package group.whiterabbit.coding_test.model

import androidx.room.Embedded
import androidx.room.Entity
import androidx.room.PrimaryKey
import group.whiterabbit.coding_test.R
import group.whiterabbit.coding_test.databind.ItemViewModel


@Entity(tableName = "employee")
data class Employee(
    @PrimaryKey val id: Long,
    @Embedded(prefix = "address_")
    val address: Address?,
    @Embedded(prefix = "company_")
    val company: Company?,
    val username: String,
    val email: String,
    val name: String,
    val phone: String?,
    val profile_image: String?,
    val website: String?,
) : ItemViewModel{
    override val layoutId: Int get() = R.layout.row_employee

}
