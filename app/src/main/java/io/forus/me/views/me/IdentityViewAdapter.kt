package io.forus.me.views.me

import android.arch.lifecycle.LifecycleOwner
import android.arch.lifecycle.LiveData
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import io.forus.me.R
import io.forus.me.entities.Identity
import io.forus.me.helpers.LiveDataAdapter
import io.forus.me.services.IdentityService

/**
 * Created by martijn.doornik on 27/03/2018.
 */
class IdentityViewAdapter(activity: AppCompatActivity, data: LiveData<List<Identity>>)
    : LiveDataAdapter<Identity, IdentityViewAdapter.IdentityViewHolder>(activity, data) {

    override fun onBindViewHolder(holder: IdentityViewHolder, position: Int) {
        holder.nameView.text = items[position].name
        if (IdentityService.currentIdentity?.id == items[position].id) {
            holder.checked.visibility = View.INVISIBLE
        } else {
            holder.checked.visibility = View.VISIBLE
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): IdentityViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.identity_list_item_view, parent, false)
        return IdentityViewHolder(view)
    }

    inner class IdentityViewHolder(view:View): RecyclerView.ViewHolder(view) {
        val checked: ImageView = view.findViewById(R.id.checked_view)
        val unchecked: ImageView = view.findViewById(R.id.unchecked_view)
        val nameView: TextView = view.findViewById(R.id.name_view)

    }
}