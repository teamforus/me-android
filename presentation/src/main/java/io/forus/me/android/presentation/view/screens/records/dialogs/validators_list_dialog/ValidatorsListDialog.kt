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
import com.afollestad.materialdialogs.callbacks.onCancel
import com.afollestad.materialdialogs.customview.customView
import io.forus.me.android.presentation.view.component.text.TextView


class ValidatorsListDialog(private val context: Context,
                           private val organizations: List<io.forus.me.android.domain.models.records.ValidatorOrganization>,
                           private val selectItemCallback: (io.forus.me.android.domain.models.records.ValidatorOrganization) -> Unit) {


    fun show() {
        val dialog = MaterialDialog(context)
                .title(R.string.dialog_no_internet_warning)
                .message(R.string.dialog_no_internet_message)
                .show {
                    val view = this.customView()

                    val recycler = view?.findViewById<RecyclerView>(R.id.recycler)

                    val mAdapter = ValidatorsAdapter(organizations, object : ValidatorsAdapter.Callback {
                        override fun onItemClicked(item: io.forus.me.android.domain.models.records.ValidatorOrganization) {
                            selectItemCallback.invoke(item)
                            dismiss()
                        }
                    })
                    recycler!!.layoutManager = LinearLayoutManager(context)


                    val dividerItemDecoration = DividerItemDecoration(context,
                            LinearLayoutManager.VERTICAL)
                    dividerItemDecoration.setDrawable(ContextCompat.getDrawable(context, R.drawable.shape_divider)!!)
                    recycler.addItemDecoration(dividerItemDecoration)
                    recycler.adapter = mAdapter
                    view?.findViewById<ImageView>(R.id.closeBt).setOnClickListener { dismiss() }


                }
        dialog.setContentView(R.layout.view_validators_list)
        dialog.window!!.setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN)
        dialog.window!!.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))

    }


}