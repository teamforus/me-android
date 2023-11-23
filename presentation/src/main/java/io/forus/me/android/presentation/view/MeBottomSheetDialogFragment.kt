package io.forus.me.android.presentation.view
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import io.forus.me.android.presentation.R
import io.forus.me.android.presentation.databinding.FragmentBottomSheetBinding

class MeBottomSheetDialogFragment(private val fragment: Fragment, private  val title: String) : BottomSheetDialogFragment() {

    private lateinit var binding: FragmentBottomSheetBinding

    override fun getTheme(): Int {
        return R.style.AppBottomSheetDialogTheme
    }


    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        binding = FragmentBottomSheetBinding.inflate(inflater)

        return binding.root
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        childFragmentManager
            .beginTransaction()
            .replace(R.id.fragmentContainer, fragment)
            .commit()

        binding.slidingPanelTitle.text = title

        binding.btClose.setOnClickListener {
            dismiss()
        }
    }

    companion object {
        fun newInstance(fragment: Fragment, title: String): MeBottomSheetDialogFragment {
            return MeBottomSheetDialogFragment(fragment, title)
        }
    }


}