package group.whiterabbit.coding_test.databind

import androidx.annotation.LayoutRes

interface ItemViewModel {
    @get:LayoutRes
    val layoutId: Int
    val viewType: Int
        get() = layoutId
}