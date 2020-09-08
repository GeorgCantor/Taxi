package taxi.kassa.view.support

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import kotlinx.android.synthetic.main.fragment_success.*
import taxi.kassa.R
import taxi.kassa.util.restart

class SuccessFragment : Fragment(R.layout.fragment_success) {

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        back_arrow_success.setOnClickListener { activity?.onBackPressed() }

        back_to_main_button.setOnClickListener { activity?.restart() }

        back_button.setOnClickListener { activity?.onBackPressed() }
    }
}