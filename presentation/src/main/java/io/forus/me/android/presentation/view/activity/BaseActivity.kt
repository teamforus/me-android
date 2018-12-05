package io.forus.me.android.presentation.view.activity

//import io.forus.me.android.presentation._AndroidApplication;
//import io.forus.me.android.presentation.internal.di.components.ApplicationComponent;
//import io.forus.me.android.presentation.internal.di.modules.ActivityModule;

import android.content.Context
import android.support.transition.Explode
import android.support.transition.Fade
import android.support.v4.app.Fragment
import android.support.v4.view.ViewCompat
import android.support.v7.app.AppCompatActivity
import android.view.View
import android.view.inputmethod.InputMethodManager
import io.forus.me.android.presentation.helpers.SystemServices
import io.forus.me.android.presentation.navigation.Navigator


/**
 * Base [android.app.Activity] class for every Activity in this application.
 */
abstract class BaseActivity : AppCompatActivity() {

    val systemServices by lazy(LazyThreadSafetyMode.NONE) { SystemServices(this) }
    val navigator by lazy{ Navigator() }

    protected fun addFragment(containerViewId: Int, fragment: Fragment) {

        supportFragmentManager
                .beginTransaction()
                .add(containerViewId, fragment)
                .commit()
    }

    protected fun addFragment(containerViewId: Int, fragment: Fragment,
                              sharedViews: List<Pair<String, View>>) {
        val transaction = supportFragmentManager
                .beginTransaction()

        fragment.sharedElementEnterTransition = Explode()
        fragment.enterTransition = Fade()
        fragment.exitTransition = Fade()
        fragment.sharedElementReturnTransition = Explode()


        sharedViews.forEach {
            transaction.addSharedElement(it.second, it.first)
        }

        transaction
                .add(containerViewId, fragment)
                .commit()
    }

    protected fun replaceFragment(containerViewId: Int, fragment: Fragment) {

        supportFragmentManager
                .beginTransaction()
                .replace(containerViewId, fragment)
                .commit()
    }

    open fun replaceFragment(fragment: Fragment,
                             sharedViews: List<View>) {}


    fun replaceFragment(containerViewId: Int, fragment: Fragment,
                                  sharedViews: List<View>, addToBackStack: Boolean) {
        val transaction = supportFragmentManager
                .beginTransaction()

        fragment.sharedElementEnterTransition = Explode()
        fragment.enterTransition = Fade()
        fragment.exitTransition = Fade()
        fragment.sharedElementReturnTransition = Explode()


        sharedViews.forEach {
            transaction.addSharedElement(it, ViewCompat.getTransitionName(it) ?: "")
        }

        if(addToBackStack) {
            transaction.addToBackStack(null)
        }

        transaction
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

    fun hideSoftKeyboard() {
        try {
            val view = currentFocus
            if (view != null) {
                val inputManager = getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
                inputManager.hideSoftInputFromWindow(view.windowToken, 0)
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }

    }
}
