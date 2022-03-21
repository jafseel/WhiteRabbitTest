package group.whiterabbit.coding_test.databind

import android.view.View
import android.widget.ImageView
import androidx.core.view.isVisible
import group.whiterabbit.coding_test.helper.GlideApp
import androidx.databinding.BindingAdapter
import androidx.recyclerview.widget.RecyclerView
import group.whiterabbit.coding_test.R


@BindingAdapter("android:visibility")
fun setVisibility(view: View, isVisible: Boolean?) {
    view.isVisible = isVisible ?: true
}

@BindingAdapter(value = ["android:src", "isRounded"], requireAll = false)
fun glideImage(view: ImageView?, imageUrl: String?, isRounded: Boolean = false) {

    var request = GlideApp.with(view?.context?.applicationContext ?: return)
        .load(imageUrl ?: "")
//        .transition(DrawableTransitionOptions.withCrossFade())
        .placeholder(if (isRounded) R.drawable.place_holder_circle else R.drawable.place_holder)
    if (isRounded) {
        request = request.circleCrop()
    }
    request.into(view)
}


@BindingAdapter(
    value = ["itemViewModels", "onItemClicked"],
    requireAll = false
)
fun bindItemViewModels(
    recyclerView: RecyclerView,
    itemViewModels: List<ItemViewModel>?,
    onItemClicked: RecyclerViewOnItemClicked?
) {
    val adapter =
        getOrCreateAdapter(recyclerView, onItemClicked)
    adapter.updateItems(itemViewModels)
}


private fun getOrCreateAdapter(
    recyclerView: RecyclerView,
    onItemClicked: RecyclerViewOnItemClicked?
): BindableRecyclerViewAdapter {
    return if (recyclerView.adapter != null && recyclerView.adapter is BindableRecyclerViewAdapter) {
        recyclerView.adapter as BindableRecyclerViewAdapter
    } else {
        val bindableRecyclerAdapter =
            BindableRecyclerViewAdapter(onItemClicked)
        recyclerView.adapter = bindableRecyclerAdapter
        bindableRecyclerAdapter
    }
}


interface RecyclerViewOnItemClicked {
    fun onItemClicked(item: ItemViewModel, position: Int)
}
