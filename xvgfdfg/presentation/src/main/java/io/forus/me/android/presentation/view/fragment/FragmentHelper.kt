package io.forus.me.android.presentation.view.fragment

import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import io.forus.me.android.presentation.navigation.Navigator

object FragmentHelper {
    fun setHomeIconToolbar(
        _activity: AppCompatActivity,
        toolbar: Toolbar?,
        profile: View?,
        allowBack: Boolean
    ) {
        if (allowBack) {
            _activity.supportActionBar?.setHomeButtonEnabled(true)
            _activity.supportActionBar?.setDisplayHomeAsUpEnabled(true)
            profile?.visibility = View.GONE
        } else {
            _activity.supportActionBar?.setHomeButtonEnabled(false)
            _activity.supportActionBar?.setDisplayHomeAsUpEnabled(false)
            profile?.visibility = View.VISIBLE
            profile?.setOnClickListener { Navigator().navigateToAccount(_activity) }
        }
    }
}