package io.forus.me.android.presentation.services;

import android.content.Context;

import io.forus.me.android.data.net.OnAccessListener;
import io.forus.me.android.presentation.R;
import io.forus.me.android.presentation.navigation.Navigator;

public class AccessMiddleware implements OnAccessListener {

    Context context;

    public AccessMiddleware(Context context) {
        this.context = context;
    }

    @Override
    public void on401() {
        new Navigator().navigateToWelcomeScreen(context, context.getString(R.string.session_expired_description));
    }
}
