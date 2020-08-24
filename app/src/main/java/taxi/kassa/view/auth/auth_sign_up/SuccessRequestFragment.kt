package taxi.kassa.view.auth.auth_sign_up

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import kotlinx.android.synthetic.main.fragment_success_request.*
import taxi.kassa.R

class SuccessRequestFragment : Fragment(R.layout.fragment_success_request) {

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        back_arrow.setOnClickListener { activity?.onBackPressed() }
        ok_button.setOnClickListener { activity?.onBackPressed() }
    }
}