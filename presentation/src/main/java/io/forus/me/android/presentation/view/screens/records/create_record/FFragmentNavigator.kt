package io.forus.me.android.presentation.view.screens.records.create_record

import android.content.Context
import android.os.Bundle
import androidx.fragment.app.FragmentManager

import androidx.navigation.NavDestination
import androidx.navigation.NavOptions
import androidx.navigation.Navigator
import androidx.navigation.fragment.FragmentNavigator

@Navigator.Name("fragment")
class FFragmentNavigator(
        context: Context,
        fm: FragmentManager,
        containerId: Int
) : FragmentNavigator(context, fm, containerId) {

    /*override fun navigate(...): NavDestination? {
        val shouldSkip = navOptions?.run {
            popUpTo == destination.id && !isPopUpToInclusive
        }  ?: false

        return if (shouldSkip) null
        else super.navigate(destination, args, navOptions, navigatorExtras)
    }*/

    override fun navigate(
        destination: Destination,
        args: Bundle?,
        navOptions: NavOptions?,
        navigatorExtras: Navigator.Extras?
    ): NavDestination? {

        val shouldSkip = navOptions?.run {
            popUpTo == destination.id && !isPopUpToInclusive
        }  ?: false

        return if (shouldSkip) null
        else super.navigate(destination, args, navOptions, navigatorExtras)
    }
}