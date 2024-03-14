package io.forus.me.android.presentation.view.screens.records.dialogs.validators_list_dialog

import android.content.Context
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import androidx.recyclerview.widget.RecyclerView
import com.afollestad.materialdialogs.MaterialDialog
import io.forus.me.android.presentation.R
import androidx.recyclerview.widget.LinearLayoutManager
import android.view.WindowManager
import android.widget.ImageView
import androidx.recyclerview.widget.DividerItemDecoration

import androidx.core.content.ContextCompat


class ValidatorsListDialog(private val context: Context,
                           private val organizations: List<io.forus.me.android.domain.models.records.ValidatorOrganization>,
                           private val selectItemCallback: (io.forus.me.android.domain.models.records.ValidatorOrganization) -> Unit) {

    private val dialog: MaterialDialog = MaterialDialog.Builder(context)
            .customView(R.layout.view_validators_list, true)
            .build()

    init {


        val view = dialog.customView
        val recycler = view?.findViewById<RecyclerView>(R.id.recycler)

        val mAdapter = ValidatorsAdapter(organizations, object : ValidatorsAdapter.Callback {
            override fun onItemClicked(item: io.forus.me.android.domain.models.records.ValidatorOrganization) {
                selectItemCallback.invoke(item)
                dialog.dismiss()
            }
        })
        recycler!!.layoutManager =
            LinearLayoutManager(context)


        val dividerItemDecoration =
            DividerItemDecoration(
                context,
                LinearLayoutManager.VERTICAL
            )
        dividerItemDecoration.setDrawable(ContextCompat.getDrawable(context,R.drawable.shape_divider)!!)
        recycler.addItemDecoration(dividerItemDecoration)

        recycler.adapter = mAdapter

        view.findViewById<ImageView>(R.id.closeBt)?.setOnClickListener { dialog.dismiss() }


        dialog.window!!.setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN)
        dialog.window!!.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))

    }

    fun show() {
        dialog.show()

    }


}