package io.forus.me.android.presentation.view.activity

import android.app.Activity
import android.app.Fragment
import android.app.FragmentTransaction
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.view.Window
import io.forus.me.android.presentation.helpers.SystemServices

//import io.forus.me.android.presentation._AndroidApplication;
//import io.forus.me.android.presentation.internal.di.components.ApplicationComponent;
//import io.forus.me.android.presentation.internal.di.modules.ActivityModule;
import java.lang.reflect.Field
import java.lang.reflect.Method

import io.forus.me.android.presentation.navigation.Navigator
import javax.inject.Inject


/**
 * Base [android.app.Activity] class for every Activity in this application.
 */
abstract class BaseActivity : AppCompatActivity() {

    val systemServices by lazy(LazyThreadSafetyMode.NONE) { SystemServices(this) }


    //@Inject
    protected lateinit var navigator: Navigator

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        navigator = Navigator()

    }

    protected fun addFragment(containerViewId: Int, fragment: android.support.v4.app.Fragment) {

        supportFragmentManager
                .beginTransaction()
                .add(containerViewId, fragment)
                .commit()
    }

    protected fun replaceFragment(containerViewId: Int, fragment: android.support.v4.app.Fragment) {

        supportFragmentManager
                .beginTransaction()
                .replace(containerViewId, fragment)
                .commit()
    }

    protected fun removeFragment(containerViewId: Int) {

        val fragment = supportFragmentManager.findFragmentById(containerViewId)
        if(fragment != null){
            supportFragmentManager
                    .beginTransaction()
                    .remove(fragment)
                    .commit()
        }
    }


}
