package io.forus.me.android.presentation.navigation

import android.content.Context
import io.forus.me.android.domain.models.records.RecordCategory
import io.forus.me.android.presentation.view.screens.account.assigndelegates.AssignDelegatesAccountActivity
import io.forus.me.android.presentation.view.screens.account.newaccount.NewAccountActivity
import io.forus.me.android.presentation.view.screens.account.pin.RestoreByPinActivity
import io.forus.me.android.presentation.view.screens.account.restoreByEmail.RestoreByEmailActivity
import io.forus.me.android.presentation.view.screens.dashboard.DashboardActivity
import io.forus.me.android.presentation.view.screens.qr.QrScannerActivity
import io.forus.me.android.presentation.view.screens.records.list.RecordsActivity
import io.forus.me.android.presentation.view.screens.records.newrecord.NewRecordActivity
import io.forus.me.android.presentation.view.screens.vouchers.item.VoucherActivity
import javax.inject.Inject
import javax.inject.Singleton

import io.forus.me.android.presentation.view.screens.welcome.WelcomeActivity

/**
 * Class used to navigate through the application.
 */
@Singleton
class Navigator @Inject
constructor()//empty
{


    fun navigateToDashboard(context: Context?) {
        if (context != null) {
            val intentToLaunch = DashboardActivity.getCallingIntent(context)
            context.startActivity(intentToLaunch)
        }
    }


    fun navigateToWelcomeScreen(context: Context?) {
        if (context != null) {
            val intentToLaunch = WelcomeActivity.getCallingIntent(context)
            context.startActivity(intentToLaunch)
        }
    }


    fun navigateToAccountRestore(context: Context?) {
        if (context != null) {
            val intentToLaunch = AssignDelegatesAccountActivity.getCallingIntent(context)
            context.startActivity(intentToLaunch)
        }
    }


    fun navigateToQrScanner(context: Context?) {
        if (context != null) {
            val intentToLaunch = QrScannerActivity.getCallingIntent(context)
            context.startActivity(intentToLaunch)
        }
    }

    fun navigateToAccountNew(context: Context?) {
        if (context != null) {
            val intentToLaunch = NewAccountActivity.getCallingIntent(context)
            context.startActivity(intentToLaunch)
        }
    }

    fun navigateToAccountRestoreByEmail(context: Context?) {
        if (context != null) {
            val intentToLaunch = RestoreByEmailActivity.getCallingIntent(context)
            context.startActivity(intentToLaunch)
        }
    }

    fun navigateToAccountRestoreByPin(context: Context?) {
        if (context != null) {
            val intentToLaunch = RestoreByPinActivity.getCallingIntent(context)
            context.startActivity(intentToLaunch)
        }
    }


    fun navigateToVoucher(context: Context?, id: String) {
        if (context != null) {
            val intentToLaunch = VoucherActivity.getCallingIntent(context, id)
            context.startActivity(intentToLaunch)
        }
    }

    fun navigateToNewRecord(context: Context?) {
        if (context != null) {
            val intentToLaunch = NewRecordActivity.getCallingIntent(context)
            context.startActivity(intentToLaunch)
        }
    }

    fun navigateToRecordsList(context: Context?, recordCategory: RecordCategory) {
        if (context != null) {
            val intentToLaunch = RecordsActivity.getCallingIntent(context, recordCategory)
            context.startActivity(intentToLaunch)
        }
    }
}
