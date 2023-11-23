package io.forus.me.android.domain.models.account

class SecurityOptions(
        var sendCrashReportsEnabled: Boolean,
        var pinEnabled: Boolean,
        var fingerprintEnabled: Boolean,
        var startFromScanner: Boolean)