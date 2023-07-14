package io.forus.me.android.presentation.view.screens.records.item.validators

import androidx.recyclerview.widget.RecyclerView
import android.view.ViewGroup
import io.forus.me.android.presentation.R
import io.forus.me.android.presentation.helpers.inflate
import kotlinx.android.synthetic.main.item_validator_list.view.*

class ValidatorVH(parent: ViewGroup, private val clickListener: ((ValidatorViewModel) -> Unit)?) : RecyclerView.ViewHolder(parent.inflate(R.layout.item_validator_list)) {
    init {

    }

    fun render(item: ValidatorViewModel) = with(itemView) {
        tv_name.text = item.name
        tv_title.text = item.title

        iv_icon.setImageUrl(item.imageUrl)
        iv_verification.setImageDrawable(resources.getDrawable(
                when(item.status){
                    ValidatorViewModel.Status.none -> R.drawable.ic_baseline_add_circle_outline
                    ValidatorViewModel.Status.pending -> R.drawable.ic_watch_later
                    ValidatorViewModel.Status.approved -> R.drawable.ic_verified_user
                    ValidatorViewModel.Status.declined -> R.drawable.ic_close
                }
        ))

        root.setOnClickListener {
            clickListener?.invoke(item)
        }
    }
}