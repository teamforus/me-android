package io.forus.me.android.presentation.view.screens.records.item

import android.view.View
import android.view.ViewGroup
import io.forus.me.android.domain.models.validators.SimpleValidator
import io.forus.me.android.presentation.R
import io.forus.me.android.presentation.helpers.inflate
import io.forus.me.android.presentation.view.adapters.RVViewHolder
import kotlinx.android.synthetic.main.validator_list_item.view.*

class ValidatorVH(itemView: View) : RVViewHolder<SimpleValidator>(itemView) {

    companion object {
        val create: (ViewGroup) -> ValidatorVH = {
            ValidatorVH(it.inflate(R.layout.validator_list_item))
        }
    }

    override fun render(item: SimpleValidator) = with(itemView) {
        tv_name.text = item.name
        iv_icon.setImageUrl(item.imageUrl)
        iv_verification.setImageDrawable(resources.getDrawable(
                when(item.status){
                    SimpleValidator.Status.none -> R.drawable.ic_add_circle_outline
                    SimpleValidator.Status.pending -> R.drawable.ic_watch_later
                    SimpleValidator.Status.approved -> R.drawable.ic_verified_user
                    SimpleValidator.Status.declined -> R.drawable.ic_close
                }
        ))

        root.setOnClickListener {
            clickListener?.invoke(item)
        }
    }
}