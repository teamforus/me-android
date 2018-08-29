package io.forus.me.android.presentation.view.fragment;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;

import io.forus.me.android.presentation.R;
import io.forus.me.android.presentation.navigation.Navigator;
import io.forus.me.android.presentation.view.component.images.AutoLoadImageView;
import io.forus.me.android.presentation.view.screens.account.newaccount.AccountActivity;

public class FragmentHelper {

    public static void setHomeIconToolbar(final AppCompatActivity _activity, Toolbar toolbar, View profile, boolean allowBack){
       if (allowBack) {
            _activity.getSupportActionBar().setHomeButtonEnabled(true);
            _activity.getSupportActionBar().setDisplayHomeAsUpEnabled(true);
           profile.setVisibility(View.GONE);
        } else {
            _activity.getSupportActionBar().setHomeButtonEnabled(false);
            _activity.getSupportActionBar().setDisplayHomeAsUpEnabled(false);

           profile.setVisibility(View.VISIBLE);

           profile.setOnClickListener(new View.OnClickListener() {
               @Override
               public void onClick(View view) {
                   (new Navigator()).navigateToAccount(_activity);
               }
           });
        }
    }
}
