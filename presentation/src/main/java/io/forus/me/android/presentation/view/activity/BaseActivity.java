package io.forus.me.android.presentation.view.activity;

import android.app.Activity;
import android.app.Fragment;
import android.app.FragmentTransaction;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.Window;

//import io.forus.me.android.presentation._AndroidApplication;
//import io.forus.me.android.presentation.internal.di.components.ApplicationComponent;
//import io.forus.me.android.presentation.internal.di.modules.ActivityModule;
import java.lang.reflect.Field;
import java.lang.reflect.Method;

import io.forus.me.android.presentation.navigation.Navigator;
import javax.inject.Inject;


/**
 * Base {@link android.app.Activity} class for every Activity in this application.
 */
public abstract class BaseActivity extends AppCompatActivity {

  //@Inject
  protected Navigator navigator;

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    navigator = new Navigator();

  }

  protected void addFragment(int containerViewId, android.support.v4.app.Fragment fragment) {

    getSupportFragmentManager()
            .beginTransaction()
            .add(containerViewId, fragment)
            .commit();
//    final FragmentTransaction fragmentTransaction = this.getFragmentManager().beginTransaction();
//    fragmentTransaction.add(containerViewId, fragment);
//    fragmentTransaction.commit();
  }
}
