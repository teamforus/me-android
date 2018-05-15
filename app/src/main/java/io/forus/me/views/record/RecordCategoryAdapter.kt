package io.forus.me.views.record

import android.content.Context
import android.support.v4.content.ContextCompat
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import io.forus.me.R
import io.forus.me.services.IdentityService
import io.forus.me.services.RecordService

/**
 * Created by martijn.doornik on 03/04/2018.
 */
class RecordCategoryAdapter(private val recordsFragment: RecordsFragment) : RecyclerView.Adapter<RecordCategoryAdapter.RecordCategoryViewHolder>() {

    private val recordCategories = RecordService.getRecordCategoriesByIdentity(IdentityService.currentAddress)

    override fun getItemCount(): Int {
        return recordCategories.size
    }

    override fun onBindViewHolder(holder: RecordCategoryViewHolder, position: Int) {
        holder.iconView.setImageDrawable(ContextCompat.getDrawable(holder.context, recordCategories[position].iconResource))
        holder.nameView.text = holder.context.resources.getText(recordCategories[position].labelResource)
        holder.recordList.layoutManager = LinearLayoutManager(holder.context)
        holder.recordList.adapter = RecordAdapter(recordCategories[position], recordsFragment, holder)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecordCategoryViewHolder {
        val wrapper = LayoutInflater.from(parent.context).inflate(R.layout.record_category_list_item_view, parent, false)
        val view =  RecordCategoryViewHolder(parent.context, wrapper)
        wrapper.setOnClickListener {
            view.isExpanded = !view.isExpanded
        }
        return view
    }

    class RecordCategoryViewHolder(val context: Context, view:View) : RecyclerView.ViewHolder(view) {
        private var _isExpanded = false
        var isExpanded: Boolean
                get() = _isExpanded
                set(isNowExpanded) {
                    if (isNowExpanded != _isExpanded) {
                        // Set icon that shows that this item can expand correctly (up or down)
                        this.expandButton.setImageDrawable(ContextCompat.getDrawable(context,
                                if (isNowExpanded) R.drawable.ic_expand_less_black_24dp
                                else R.drawable.ic_expand_more_black_24dp
                        ))
                        // Show or hide list of corresponding records
                        this.recordListWrapper.visibility =
                                if (isNowExpanded) View.VISIBLE
                                else View.GONE
                        this._isExpanded = isNowExpanded
                    }
                }

        val expandButton: ImageView = view.findViewById(R.id.expandButton)
        val iconView: ImageView = view.findViewById(R.id.iconView)
        val nameView:TextView = view.findViewById(R.id.recordCategoryTitle)
        val recordList: RecyclerView = view.findViewById(R.id.recordList)
        private val recordListEmpty: TextView = view.findViewById(R.id.recordListEmpty)
        private val recordListWrapper: LinearLayout = view.findViewById(R.id.recordListWrapper)
        private val progressBar: ProgressBar = view.findViewById(R.id.spinner)

        fun onRecordListChange() {
            this.progressBar.visibility = View.GONE
            val isEmpty = recordList.adapter.itemCount == 0
            this.recordList.visibility =
                    if (isEmpty) View.GONE
                    else View.VISIBLE
            this.recordListEmpty.visibility =
                    if (isEmpty) View.VISIBLE
                    else View.GONE
        }
    }
}