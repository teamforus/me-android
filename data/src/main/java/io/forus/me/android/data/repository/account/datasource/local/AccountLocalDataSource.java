package io.forus.me.android.data.repository.account.datasource.local;

import com.gigawatt.android.data.net.sign.models.request.SignUp;

import org.jetbrains.annotations.NotNull;

import java.util.List;

import io.forus.me.android.data.entity.database.DaoSession;
import io.forus.me.android.data.entity.database.Database;
import io.forus.me.android.data.entity.database.DatabaseManager;
import io.forus.me.android.data.entity.database.Token;
import io.forus.me.android.data.entity.database.TokenDao;
import io.forus.me.android.data.entity.sign.response.AccessToken;
import io.forus.me.android.data.entity.sign.response.IdentityPinResult;
import io.forus.me.android.data.entity.sign.response.IdentityTokenResult;
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

    @NotNull
    @Override
    public Observable<SignUpResult> createUser(@NotNull SignUp signUp) {
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

    @NotNull
    @Override
    public Observable<Boolean> restoreByEmail(@NotNull String email) {
        return null;
    }

    @NotNull
    @Override
    public Observable<AccessToken> restoreExchangeToken(@NotNull String token) {
        return null;
    }

    @NotNull
    @Override
    public Observable<IdentityPinResult> restoreByPinCode() {
        return null;
    }

    @NotNull
    @Override
    public Observable<IdentityTokenResult> restoreByQrToken() {
        return null;
    }

    @NotNull
    @Override
    public Observable<Boolean> authorizeCode(@NotNull String code) {
        return null;
    }

    @NotNull
    @Override
    public Observable<Boolean> authorizeToken(@NotNull String token) {
        return null;
    }

    @Override
    public void saveIdentity(@NotNull String token, @NotNull String pin) {
        Database db = DatabaseManager.Companion.getInstance();
        db.delete();
        boolean opened = db.open(pin);
        if (!opened || tokenDao == null) throw new IllegalStateException("DB is not opened");

        Token dbToken = new Token();
        dbToken.setToken(token);
        tokenDao.save(dbToken);
        db.refresh();
    }

    public boolean unlockIdentity(@NotNull String pin){
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
    public boolean checkPin(@NotNull String pin) {
        return DatabaseManager.Companion.getInstance().checkPin(pin);
    }

    @Override
    public boolean changePin(@NotNull String oldPin, @NotNull String newPin) {
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

    @NotNull
    @Override
    public String getCurrentToken() {
        return getTokenString();
    }
}
