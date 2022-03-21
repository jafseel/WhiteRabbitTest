package group.whiterabbit.coding_test.model

import androidx.room.Embedded

data class Address(
    val city: String?,
    @Embedded(prefix = "address_geo_")
    val geo: Geo?,
    val street: String?,
    val suite: String?,
    val zipcode: String?
) {
    val address get() = "$city, $street, $suite"
}