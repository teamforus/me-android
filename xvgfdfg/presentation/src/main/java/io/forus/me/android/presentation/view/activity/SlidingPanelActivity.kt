package io.forus.me.android.presentation.view.activity

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.text.Spannable
import android.view.View
import com.sothree.slidinguppanel.SlidingUpPanelLayout
import io.forus.me.android.presentation.R
import io.forus.me.android.presentation.databinding.ActivityToolbarSlidingPanelBinding
import io.forus.me.android.presentation.helpers.Converter

abstract class SlidingPanelActivity : CommonActivity() {

    open val height: Float
        get() = 500f

    override val viewID: Int
        get() = R.layout.activity_toolbar_sliding_panel

    private lateinit var binding: ActivityToolbarSlidingPanelBinding
    
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityToolbarSlidingPanelBinding.inflate(layoutInflater)
        binding.fragmentPanelContainer.minimumHeight = Converter.convertDpToPixel(height, applicationContext)
        binding.slidingLayout.addPanelSlideListener(object: SlidingUpPanelLayout.PanelSlideListener{
            override fun onPanelSlide(panel: View?, slideOffset: Float) {}

            override fun onPanelStateChanged(panel: View?, previousState: SlidingUpPanelLayout.PanelState?, newState: SlidingUpPanelLayout.PanelState?) {
                if(newState == SlidingUpPanelLayout.PanelState.COLLAPSED){
                    removePopupFragment()
                }
            }

        })
    }

    protected fun addPopupFragment(fragment: Fragment, title: String){
        replaceFragment(R.id.fragmentPanelContainer, fragment)
        binding.slidingPanelTitle.text = title
        binding.slidingLayout.panelState = SlidingUpPanelLayout.PanelState.EXPANDED
    }

    protected fun addPopupFragment(fragment: Fragment, title: Spannable){
        replaceFragment(R.id.fragmentPanelContainer, fragment)
        binding.slidingPanelTitle.text = title
        binding.slidingLayout.panelState = SlidingUpPanelLayout.PanelState.EXPANDED
    }

    protected fun removePopupFragment(){
        removeFragment(R.id.fragmentPanelContainer)
    }
}