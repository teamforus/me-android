package io.forus.me.views.record

import android.arch.lifecycle.LifecycleOwner
import android.content.Context
import android.support.v4.content.ContextCompat
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import io.forus.me.R
import io.forus.me.services.IdentityService
import io.forus.me.services.RecordService

/**
 * Created by martijn.doornik on 03/04/2018.
 */
class RecordCategoryRecyclerAdapter(private val lifecycleOwner: LifecycleOwner) : RecyclerView.Adapter<RecordCategoryRecyclerAdapter.RecordCategoryViewHolder>() {

    private val recordCategories = RecordService.getRecordCategoriesByIdentity(IdentityService.currentAddress)

    override fun getItemCount(): Int {
        return recordCategories.size
    }

    override fun onBindViewHolder(holder: RecordCategoryViewHolder, position: Int) {
        holder.iconView.setImageDrawable(ContextCompat.getDrawable(holder.context, recordCategories[position].iconResource))
        holder.nameView.text = holder.context.resources.getText(recordCategories[position].labelResource)
        holder.recordsList.layoutManager = LinearLayoutManager(holder.context)
        holder.recordsList.adapter = RecordRecyclerAdapter(recordCategories[position], lifecycleOwner)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecordCategoryViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.record_category_list_item_view, parent, false)
        return RecordCategoryViewHolder(parent.context, view)
    }

    class RecordCategoryViewHolder(val context: Context, view:View) : RecyclerView.ViewHolder(view) {
        val iconView: ImageView = view.findViewById(R.id.iconView)
        val nameView:TextView = view.findViewById(R.id.recordCategoryTitle)
        val recordsList: RecyclerView = view.findViewById(R.id.recordList)
    }
}