package io.forus.me.android.data.repository.account.datasource.local;

import androidx.annotation.NonNull;

import com.gigawatt.android.data.net.sign.models.request.SignUp;


import java.util.List;

import io.forus.me.android.data.entity.account.Account;
import io.forus.me.android.data.entity.database.DaoSession;
import io.forus.me.android.data.entity.database.Database;
import io.forus.me.android.data.entity.database.DatabaseManager;
import io.forus.me.android.data.entity.database.Token;
import io.forus.me.android.data.entity.database.TokenDao;
import io.forus.me.android.data.entity.sign.response.AccessToken;
import io.forus.me.android.data.entity.sign.response.IdentityPinResult;
import io.forus.me.android.data.entity.sign.response.IdentityTokenResult;
import io.forus.me.android.data.entity.sign.response.ShortTokenResult;
import io.forus.me.android.data.entity.sign.response.SignUpResult;
import io.forus.me.android.data.repository.account.datasource.AccountDataSource;
import io.forus.me.android.data.repository.datasource.LocalDataSource;
import io.reactivex.Observable;

public class AccountLocalDataSource implements AccountDataSource, LocalDataSource{

    private TokenDao tokenDao;

    public AccountLocalDataSource(TokenDao tokenDao) {
        this.tokenDao = tokenDao;
    }

    @Override
    public void updateDao(DaoSession daoSession) {
        this.tokenDao = daoSession != null ? daoSession.getTokenDao() : null;
    }

    @NonNull
    @Override
    public Observable<Boolean> createUser(@NonNull SignUp signUp) {
        return null;
    }

    public String getTokenString()  {

        Token token = getToken();
        return  (token != null) ? token.getToken() : "";

    }

    private Token getToken()  {
        if(tokenDao == null) return null;

        List<Token> tokes = tokenDao.queryBuilder().orderDesc(TokenDao.Properties.Id).limit(1).list();
        return tokes.size() == 0 ? null : tokes.get(0);

    }

    @NonNull
    @Override
    public Observable<Boolean> restoreByEmail(@NonNull String email) {
        return null;
    }

    @NonNull
    @Override
    public Observable<AccessToken> restoreExchangeToken(@NonNull String token) {
        return null;
    }

    @NonNull
    @Override
    public Observable<IdentityPinResult> restoreByPinCode() {
        return null;
    }

    @NonNull
    @Override
    public Observable<IdentityTokenResult> restoreByQrToken() {
        return null;
    }

    @NonNull
    @Override
    public Observable<Boolean> authorizeCode(@NonNull String code) {
        return null;
    }

    @NonNull
    @Override
    public Observable<Boolean> authorizeToken(@NonNull String token) {
        return null;
    }

    @NonNull
    @Override
    public Observable<Boolean> registerPush(@NonNull String token) {
        return null;
    }

    @NonNull
    @Override
    public Observable<Account> getIdentity() {
        return null;
    }

    @Override
    public void saveIdentity(@NonNull String token, @NonNull String pin) {
        Database db = DatabaseManager.Companion.getInstance();
        db.delete();
        boolean opened = db.open(pin);
        if (!opened || tokenDao == null) throw new IllegalStateException("DB is not opened");

        Token dbToken = new Token();
        dbToken.setToken(token);
        tokenDao.save(dbToken);
        db.refresh();
    }

    public boolean unlockIdentity(@NonNull String pin){
        Database db = DatabaseManager.Companion.getInstance();
        boolean success = db.open(pin);
        db.refresh();
        return success;
    }

    @Override
    public boolean isLogin() {
        return DatabaseManager.Companion.getInstance().isOpen() && getToken() != null;
    }

    @Override
    public boolean checkPin(@NonNull String pin) {
        return DatabaseManager.Companion.getInstance().checkPin(pin);
    }

    @Override
    public boolean changePin(@NonNull String oldPin, @NonNull String newPin) {
        if(checkPin(oldPin)){
            saveIdentity(getTokenString(), newPin);
            return true;
        }
        return false;
    }

    @Override
    public void logout() {
        Database db = DatabaseManager.Companion.getInstance();
        db.delete();
        db.refresh();
    }

    @NonNull
    @Override
    public String getCurrentToken() {
        return getTokenString();
    }

    @NonNull
    @Override
    public Observable<AccessToken> registerExchangeToken(@NonNull String token) {
        return null;
    }

    @NonNull
    @Override
    public Observable<ShortTokenResult> getShortToken() {
        return null;
    }
}
