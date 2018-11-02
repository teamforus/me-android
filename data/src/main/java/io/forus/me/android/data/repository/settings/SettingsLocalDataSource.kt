package io.forus.me.android.data.repository.settings

class SettingsLocalDataSource(private val settings: SettingsDataSource): SettingsDataSource{

    override fun clear() = settings.clear()

    override fun isFingerprintEnabled() = settings.isFingerprintEnabled()

    override fun setFingerprintEnabled(isFingerprintEnabled: Boolean) = settings.setFingerprintEnabled(isFingerprintEnabled)

    override fun isPinEnabled(): Boolean = settings.isPinEnabled()

    override fun getPin(): String = settings.getPin()

    override fun setPin(pin: String): Boolean = settings.setPin(pin)

    override fun setFCMToken(token: String): Boolean = settings.setFCMToken(token)

    override fun getFCMToken(): String = settings.getFCMToken()

    override fun updateFCMToken(): Boolean = settings.updateFCMToken()
}