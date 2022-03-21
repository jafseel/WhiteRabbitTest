package group.whiterabbit.coding_test.model

data class Company(
    val bs: String?,
    val catchPhrase: String?,
    val name: String?
) {
    val detail get() = "$name, $bs, $catchPhrase"
}