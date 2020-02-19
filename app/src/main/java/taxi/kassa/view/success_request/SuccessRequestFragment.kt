package taxi.kassa.view.success_request

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.NavOptions
import androidx.navigation.Navigation
import kotlinx.android.synthetic.main.fragment_success_request.*
import taxi.kassa.R

class SuccessRequestFragment : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? = inflater.inflate(R.layout.fragment_success_request, container, false)

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        back_arrow.setOnClickListener { closeFragment() }
        ok_button.setOnClickListener { closeFragment() }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        closeFragment()
    }

    private fun closeFragment() {
        val navOption = NavOptions.Builder().setPopUpTo(R.id.introFragment, true).build()
        Navigation.findNavController(requireView()).navigate(R.id.introFragment, null, navOption)
    }
}