package io.forus.me.android.presentation.helpers

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ViewAnimator
import androidx.annotation.LayoutRes

fun ViewGroup.inflate(@LayoutRes layoutRes: Int, attachToRoot: Boolean = false): View =
    LayoutInflater.from(context).inflate(layoutRes, this, attachToRoot)

fun ViewAnimator.showIfNotYet(child: Int) {
    if (child != displayedChild) {
        displayedChild = child
    }
}

