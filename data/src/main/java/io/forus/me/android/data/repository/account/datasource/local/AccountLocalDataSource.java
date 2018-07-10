package io.forus.me.android.data.repository.account.datasource.local;

import org.jetbrains.annotations.NotNull;

import java.util.List;

import io.forus.me.android.data.entity.database.Token;
import io.forus.me.android.data.entity.database.TokenDao;
import io.forus.me.android.data.entity.sign.response.AccessToken;
import io.forus.me.android.data.entity.sign.response.IdentityPinResult;
import io.forus.me.android.data.entity.sign.response.IdentityTokenResult;
import io.forus.me.android.data.entity.sign.response.SignUpResult;
import io.forus.me.android.data.repository.account.datasource.AccountDataSource;
import io.forus.me.android.domain.models.account.NewAccountRequest;
import io.reactivex.Observable;

public class AccountLocalDataSource implements AccountDataSource {

    final TokenDao tokenDao;

    public AccountLocalDataSource(TokenDao tokenDao) {
        this.tokenDao = tokenDao;
    }


    @NotNull
    @Override
    public Observable<SignUpResult> createUser(@NotNull NewAccountRequest model) {
        return null;
    }

    public String getTokenString()  {

        Token token = getToken();
        return  (token != null) ? token.getToken() : "";

    }

    private Token getToken()  {
        List<Token> tokes = tokenDao.queryBuilder().orderDesc(TokenDao.Properties.Id).limit(1).list();
        return tokes.size() == 0 ? null : tokes.get(0);

    }

    @NotNull
    @Override
    public Observable<AccessToken> requestNewUserByEmail(@NotNull String email) {
        return null;
    }

    @NotNull
    @Override
    public Observable<IdentityPinResult> getAuthCode() {
        return null;
    }

    @NotNull
    @Override
    public Observable<IdentityTokenResult> requestDelegatesQRAddress() {
        return null;
    }

    @Override
    public void saveToken(@NotNull String token) {

    }

    @Override
    public boolean isLogin() {
        return getToken() != null;
    }

    @Override
    public void logout() {
        tokenDao.deleteAll();
    }
}
