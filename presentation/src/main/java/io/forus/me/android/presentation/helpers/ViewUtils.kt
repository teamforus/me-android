package io.forus.me.android.presentation.helpers

import android.os.Build
import androidx.annotation.LayoutRes
import androidx.annotation.StyleRes
import androidx.core.widget.TextViewCompat
import android.text.Html
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import android.widget.ViewAnimator

fun ViewGroup.inflate(@LayoutRes layoutRes: Int, attachToRoot: Boolean = false): View =
        LayoutInflater.from(context).inflate(layoutRes, this, attachToRoot)

fun ViewAnimator.showIfNotYet(child: Int) {
    if (child != displayedChild) {
        displayedChild = child
    }
}

