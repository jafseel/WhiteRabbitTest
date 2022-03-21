package group.whiterabbit.coding_test.databind

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.databinding.ViewDataBinding
import androidx.recyclerview.widget.RecyclerView
import group.whiterabbit.coding_test.BR
import group.whiterabbit.coding_test.helper.Log

class BindableRecyclerViewAdapter(
    private val onItemClicked: RecyclerViewOnItemClicked?
) : RecyclerView.Adapter<BindableViewHolder>() {

    var itemViewModels: List<ItemViewModel> = emptyList()

    //    private var viewTypeToLayoutId: Int = R.layout.row_category
    private var viewTypeToLayoutId: Int = 0

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BindableViewHolder {
        val binding: ViewDataBinding = DataBindingUtil.inflate(
            LayoutInflater.from(parent.context),
            viewTypeToLayoutId,
            parent,
            false
        )
        Log.d("BindableRecyclerViewAdapter onCreateViewHolder")
        return BindableViewHolder(binding)
    }

    override fun getItemViewType(position: Int): Int {
        val item = itemViewModels[position]
//        if (!viewTypeToLayoutId.containsKey(item.viewType)) {
//            viewTypeToLayoutId[item.viewType] = item.layoutId
//        }

        viewTypeToLayoutId = item.layoutId
        return item.viewType
    }

    override fun getItemCount(): Int = itemViewModels.size

    override fun onBindViewHolder(holder: BindableViewHolder, position: Int) {
        val data = itemViewModels[position]
        holder.bind(data, position, itemCount)
        holder.itemView.setOnClickListener {
            onItemClicked?.onItemClicked(data, position)
        }
    }

    private var fullNotify = false

    private var previousSelection = -1

//    fun updateItems(items: List<ItemViewModel>?) {
//        itemViewModels = items ?: emptyList()
//        try {
//
//            notifyDataSetChanged()
////            notifyItemRangeChanged(0, itemViewModels.size)
//        } catch (e: Exception) {
//        }
//    }

    fun updateItems(items: List<ItemViewModel>?) {
//        val lastPosition = itemViewModels.size
        itemViewModels = items ?: emptyList()
//        try {
//            if (lastPosition == 0 || itemViewModels.size <= lastPosition) {
//                notifyDataSetChanged()
//            } else {
//                notifyItemRangeChanged(lastPosition - 1, itemViewModels.size)
//            }
//        } catch (e: Exception) {
            notifyDataSetChanged()
//        }
    }
}

class BindableViewHolder(private val binding: ViewDataBinding) :
    RecyclerView.ViewHolder(binding.root) {
    fun bind(itemViewModel: ItemViewModel, position: Int, itemCount: Int) {
        binding.setVariable(BR.itemViewModel, itemViewModel)
//        binding.setVariable(BR.position, position)
//        binding.setVariable(BR.itemCount, itemCount)
    }
}