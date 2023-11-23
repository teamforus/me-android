package io.forus.me.android.presentation.view.screens.records.create_record

import androidx.navigation.fragment.NavHostFragment

class FNavHostFragment : NavHostFragment() {
    override fun createFragmentNavigator() =
        FFragmentNavigator(requireContext(), childFragmentManager, id)
}