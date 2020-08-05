package taxi.kassa.view.support

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import kotlinx.android.synthetic.main.fragment_success.*
import taxi.kassa.R
import taxi.kassa.util.inflate
import taxi.kassa.view.MainActivity

class SuccessFragment : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? = container?.inflate(R.layout.fragment_success)

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        back_arrow_success.setOnClickListener { activity?.onBackPressed() }

        back_to_main_button.setOnClickListener {
            activity?.finish()
            startActivity(Intent(requireActivity(), MainActivity::class.java))
        }

        back_button.setOnClickListener { activity?.onBackPressed() }
    }
}