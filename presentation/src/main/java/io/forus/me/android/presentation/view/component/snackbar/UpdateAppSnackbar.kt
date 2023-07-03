package com.example.snackbarexample.customsnackbar.chef

import com.google.android.material.snackbar.BaseTransientBottomBar
import com.google.android.material.snackbar.Snackbar
import androidx.core.content.ContextCompat
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup


import com.example.customsnackbar.extension.findSuitableParent
import io.forus.me.android.presentation.R


class UpdateAppSnackbar(
        parent: ViewGroup,
        content: UpdateAppSnackbarView
) : BaseTransientBottomBar<UpdateAppSnackbar>(parent, content, content) {

    init {
        getView().setBackgroundColor(ContextCompat.getColor(view.context, android.R.color.transparent))
        getView().setPadding(0, 0, 0, 0)
    }

    companion object {

        fun make(view: View, updateClickListener: View.OnClickListener?): UpdateAppSnackbar {

            val parent = view.findSuitableParent() ?: throw IllegalArgumentException(
                    "No suitable parent found from the given view. Please provide a valid view."
            )

            val customView = LayoutInflater.from(view.context).inflate(
                    R.layout.layout_snackbar_update_app,
                    parent,
                    false
            ) as UpdateAppSnackbarView

            if (updateClickListener != null) {
                customView.setUpdateClickListener(updateClickListener)
            }

            val updateAppSnackbar = UpdateAppSnackbar(
                    parent,
                    customView
            )
            updateAppSnackbar.setDuration(Snackbar.LENGTH_INDEFINITE)

            return updateAppSnackbar
        }

    }

}