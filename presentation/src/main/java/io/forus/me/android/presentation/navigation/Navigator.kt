package io.forus.me.android.presentation.navigation

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.content.Intent.FLAG_ACTIVITY_CLEAR_TASK
import androidx.fragment.app.Fragment
import androidx.appcompat.app.AppCompatActivity
import io.forus.me.android.domain.models.records.Record
import io.forus.me.android.domain.models.records.RecordCategory
import io.forus.me.android.domain.models.wallets.Wallet
import io.forus.me.android.presentation.models.ChangePinMode
import io.forus.me.android.presentation.models.vouchers.Voucher
import io.forus.me.android.presentation.view.screens.account.account.AccountActivity
import io.forus.me.android.presentation.view.screens.account.account.check_email.CheckEmailActivity
import io.forus.me.android.presentation.view.screens.account.login_signup_account.LogInSignUpActivity
import io.forus.me.android.presentation.view.screens.account.account.pin.ChangePinActivity
import io.forus.me.android.presentation.view.screens.account.assigndelegates.AssignDelegatesAccountActivity
import io.forus.me.android.presentation.view.screens.account.assigndelegates.email.RestoreByEmailActivity
import io.forus.me.android.presentation.view.screens.account.newaccount.NewAccountActivity
import io.forus.me.android.presentation.view.screens.account.newaccount.confirmRegistration.ConfirmRegistrationActivity
import io.forus.me.android.presentation.view.screens.account.newaccount.pin.NewPinActivity
import io.forus.me.android.presentation.view.screens.account.pair_device.PairDeviceActivity
import io.forus.me.android.presentation.view.screens.account.restore_account_success.RestoreAccountSuccessActivity
import io.forus.me.android.presentation.view.screens.account.send_crash_reports.SendReportsActivity
import io.forus.me.android.presentation.view.screens.dashboard.DashboardActivity
import io.forus.me.android.presentation.view.screens.lock.PinLockActivity
import io.forus.me.android.presentation.view.screens.qr.QrScannerActivity
import io.forus.me.android.presentation.view.screens.records.item.RecordDetailsActivity
import io.forus.me.android.presentation.view.screens.records.list.RecordsActivity
import io.forus.me.android.presentation.view.screens.records.newrecord.NewRecordActivity
import io.forus.me.android.presentation.view.screens.vouchers.item.VoucherActivity
import io.forus.me.android.presentation.view.screens.vouchers.product_reservation.ProductReservationActivity
import io.forus.me.android.presentation.view.screens.vouchers.provider.ProviderActivity
import io.forus.me.android.presentation.view.screens.wallets.item.WalletDetailsActivity
import io.forus.me.android.presentation.view.screens.welcome.WelcomeActivity
import javax.inject.Inject
import javax.inject.Singleton

/**
 * Class used to navigate through the application.
 */
@Singleton
class Navigator @Inject
constructor()//empty
{
    fun navigateToDashboardPinlocked(context: Context?, useFingerprint: Boolean) {
        if (context != null) {
            val lockIntent = PinLockActivity.getCallingIntent(context, DashboardActivity.getCallingIntent(context), useFingerprint)
            context.startActivity(lockIntent)
        }
    }

    fun navigateToCheckTransactionPin(context: Context?,intent: Intent, useFingerprint: Boolean) {
        if (context != null) {
            val lockIntent = PinLockActivity.getCallingIntent(context, intent, useFingerprint)
            context.startActivity(lockIntent)
        }
    }

    fun navigateToDashboard(context: Context?) {
        if (context != null) {
            val intentToLaunch = DashboardActivity.getCallingIntent(context)
            intentToLaunch.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK or FLAG_ACTIVITY_CLEAR_TASK)
            context.startActivity(intentToLaunch)
        }
    }


    fun navigateToWelcomeScreen(context: Context?) {
        if (context != null) {
            val intentToLaunch = WelcomeActivity.getCallingIntent(context)
            intentToLaunch.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK or FLAG_ACTIVITY_CLEAR_TASK)
            context.startActivity(intentToLaunch)
        }
    }


    fun navigateToAccountRestore(context: Context?) {
        if (context != null) {
            val intentToLaunch = AssignDelegatesAccountActivity.getCallingIntent(context)
            context.startActivity(intentToLaunch)
        }
    }

    fun navigateToPairDevice(context: Context?) {
        if (context != null) {
            val intentToLaunch = PairDeviceActivity.getCallingIntent(context)
            context.startActivity(intentToLaunch)
        }
    }

    fun navigateToCheckEmail(context: Context?) {
        if (context != null) {
            val intentToLaunch = CheckEmailActivity.getCallingIntent(context)
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

    fun navigateToPinNew(context: Context?, accessToken: String){
        if (context != null) {
            val intentToLaunch = NewPinActivity.getCallingIntent(context, accessToken)
            context.startActivity(intentToLaunch)
        }
    }

    fun navigateToAccountRestoreByEmail(context: Context?) {
        if (context != null) {
            val intentToLaunch = RestoreByEmailActivity.getCallingIntent(context)
            context.startActivity(intentToLaunch)
        }
    }


    fun navigateToConfirmRegistration(context: Context?, accessToken: String) {
        if (context != null) {
            val intentToLaunch = ConfirmRegistrationActivity.getCallingIntent(context,accessToken)
            context.startActivity(intentToLaunch)
        }
    }




    fun navigateToAccountRestoreByEmailExchangeToken(context: Context?, token: String) {
        if (context != null) {
            val intentToLaunch = RestoreByEmailActivity.getCallingIntent(context, token)
            intentToLaunch.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)
            context.startActivity(intentToLaunch)
        }
    }


    fun navigateToLoginSignUp(context: Context?) {
        if (context != null) {
            val intentToLaunch = LogInSignUpActivity.getCallingIntent(context)
            //intentToLaunch.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)
            context.startActivity(intentToLaunch)
        }
    }




    fun navigateToLoginSignUp(context: Context?, token: String) {
        if (context != null) {
            val intentToLaunch = LogInSignUpActivity.getCallingIntent(context, token)
            intentToLaunch.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)
            context.startActivity(intentToLaunch)
        }
    }

    fun navigateToResoreAccountSuccess(context: Context?, token: String, isExchangeToken: Boolean) {
        if (context != null) {
            val intentToLaunch = RestoreAccountSuccessActivity.getCallingIntent(context, token, isExchangeToken)
            intentToLaunch.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)
            context.startActivity(intentToLaunch)
        }
    }



    fun navigateToWallet(context: Context?, wallet: Wallet) {
        if (context != null) {
            val intentToLaunch = WalletDetailsActivity.getCallingIntent(context, wallet)
            context.startActivity(intentToLaunch)
        }
    }

    fun navigateToVoucher(context: Context?, id: String) {
        if (context != null) {
            val intentToLaunch = VoucherActivity.getCallingIntent(context, id)
            context.startActivity(intentToLaunch)
        }
    }

    fun navigateToVoucher(context: Context?, voucher: Voucher) {
        if (context != null) {
            val intentToLaunch = VoucherActivity.getCallingIntent(context, voucher)
            context.startActivity(intentToLaunch)
        }
    }

    fun navigateToSendReports(context: Context?, exchangeToken: String) {
        if (context != null) {
            val intentToLaunch = SendReportsActivity.getCallingIntent(context, exchangeToken)
            context.startActivity(intentToLaunch)
        }
    }

    fun navigateToAccount(context: Context?) {
        if (context != null) {
            val intentToLaunch = AccountActivity.getCallingIntent(context)
            context.startActivity(intentToLaunch)
        }
    }

    fun navigateToNewRecord(context: Context?) {
        if (context != null) {
            val intentToLaunch = NewRecordActivity.getCallingIntent(context)
            context.startActivity(intentToLaunch)
        }
    }

    fun navigateToRecordsList(context: Context?, recordCategory: RecordCategory?) {
        if (context != null) {
            val intentToLaunch = RecordsActivity.getCallingIntent(context, recordCategory)
            context.startActivity(intentToLaunch)
        }
    }

    fun navigateToRecordDetailsForResult(context: AppCompatActivity, record: Record, REQUEST_CODE: Int) {
        if (context != null) {
            val intentToLaunch = RecordDetailsActivity.getCallingIntent(context, record)
            context.startActivityForResult(intentToLaunch,REQUEST_CODE)
        }
    }



    fun navigateToVoucherProvider(context: Context?, voucherAddress: String,  isDemoVoucher: Boolean? = false) {
        if (context != null) {
            val intentToLaunch = ProviderActivity.getCallingIntent(context, voucherAddress,isDemoVoucher)
            context.startActivity(intentToLaunch)
        }
    }

    fun navigateToProductReservation(context: Context?, voucherAddress: String, showParentVoucher: Boolean) {
        if (context != null) {
            val intentToLaunch = ProductReservationActivity.getCallingIntent(context, voucherAddress, showParentVoucher)
            context.startActivity(intentToLaunch)
        }
    }


    fun navigateToChangePin(caller: Fragment, mode: ChangePinMode, requestCode: Int){
        val context = caller.context
        if(context != null){
            val intentToLaunch = ChangePinActivity.getCallingIntent(context, mode)
            caller.startActivityForResult(intentToLaunch, requestCode)
        }
    }
}
