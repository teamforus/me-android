package io.forus.me.android.presentation.helpers

import android.os.Build
import android.support.annotation.LayoutRes
import android.support.annotation.StyleRes
import android.support.v4.widget.TextViewCompat
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



fun ImageView.loadUrl(url: String) {
    //Picasso.with(context).initData(url).into(this)
}

fun TextView.setTextAppearanceC(@StyleRes textAppearance: Int)
        = TextViewCompat.setTextAppearance(this, textAppearance)

@Suppress("DEPRECATION")
fun String.fromHtml() = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
    Html.fromHtml(this, Html.FROM_HTML_MODE_COMPACT).toString();
} else {
    Html.fromHtml(this).toString()
}