package io.forus.me.android.presentation.view.screens.records.item.validations

import androidx.recyclerview.widget.RecyclerView
import android.view.ViewGroup
import io.forus.me.android.presentation.R
import io.forus.me.android.presentation.helpers.inflate
import kotlinx.android.synthetic.main.item_validator_list.view.*

class ValidationVH(parent: ViewGroup, private val clickListener: ((ValidationViewModel) -> Unit)?) : RecyclerView.ViewHolder(parent.inflate(R.layout.item_validation_list)) {
    init {

    }

    fun render(item: ValidationViewModel) = with(itemView) {
        tv_name.text = item.name
        tv_title.text = item.title

        /*iv_icon.setImageUrl(item.imageUrl)
        iv_verification.setImageDrawable(resources.getDrawable(
                when(item.status){
                    ValidationViewModel.Status.none -> R.drawable.ic_baseline_add_circle_outline
                    ValidationViewModel.Status.pending -> R.drawable.ic_watch_later
                    ValidationViewModel.Status.approved -> R.drawable.ic_verified_user
                    ValidationViewModel.Status.declined -> R.drawable.ic_close
                }
        ))*/

        root.setOnClickListener {
            clickListener?.invoke(item)
        }
    }
}