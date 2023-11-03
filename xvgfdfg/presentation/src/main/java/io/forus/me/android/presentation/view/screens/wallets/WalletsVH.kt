package io.forus.me.android.presentation.view.screens.wallets

import androidx.recyclerview.widget.RecyclerView
import io.forus.me.android.domain.models.wallets.Wallet
import io.forus.me.android.presentation.databinding.ItemWalletsBinding
import io.forus.me.android.presentation.helpers.format


class WalletsVH(private val binding: ItemWalletsBinding) : RecyclerView.ViewHolder(binding.root) {

    fun bind(wallet: Wallet, clickListener: ((Wallet) -> Unit)?) {
        binding.apply {
            tvName.text = wallet.name
            tvCurrencyName.text = wallet.currency?.name
            tvValue.text = wallet.balance.format()
            ivLogo.setImageUrl(wallet.logoUrl)

            root.setOnClickListener {
                clickListener?.invoke(wallet)
            }
        }
    }
}