package io.forus.me.android.presentation.view.screens.dashboard

interface DashboardContract {

    interface View {

        fun logout()

        fun addUserId(id: String)
    }

    interface Presenter {
        fun onCreate()
    }
}