package io.forus.me.android.presentation.view.screens.account.restoreByEmail;

import io.forus.me.android.domain.models.account.RestoreAccountByEmailRequest


data class RestoreByEmailModel(
        val item: RestoreAccountByEmailRequest = RestoreAccountByEmailRequest(),
        val sendingRegistration: Boolean = false
        ) {
}