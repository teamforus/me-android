package io.forus.me.android.presentation.view.screens.vouchers.provider.dialogs.organizations_dialog

import android.content.Context
import androidx.recyclerview.widget.RecyclerView
import com.afollestad.materialdialogs.MaterialDialog
import io.forus.me.android.presentation.R
import io.forus.me.android.presentation.models.vouchers.Organization
import androidx.recyclerview.widget.LinearLayoutManager




class OrganizationsListDialog(private val context: Context,
                              private val organizations: List<Organization>,
                              private val selectItemCallback: (Organization) -> Unit) {

    private val dialog: MaterialDialog = MaterialDialog.Builder(context)
            .title(context.resources.getString(R.string.voucher_dialog_choose_organization))
            .customView(R.layout.view_organizations_list, true)
            .negativeText(context.resources.getString(R.string.me_cancel))
            .build()

    init {


        val view = dialog.customView
        val recycler = view?.findViewById<RecyclerView>(R.id.recycler)

        val mAdapter = OrganizationsAdapter(organizations, object : OrganizationsAdapter.Callback {
            override fun onItemClicked(item: Organization) {
                selectItemCallback.invoke(item)
                dialog.dismiss()
            }
        })
        recycler!!.layoutManager = LinearLayoutManager(context)
        recycler.adapter = mAdapter

    }

    fun show() {
        dialog.show()

    }


}