package taxi.kassa.view.registration.connection

import android.os.Bundle
import android.view.KeyEvent
import android.view.LayoutInflater
import android.view.View
import android.view.View.GONE
import android.view.View.VISIBLE
import android.view.ViewGroup
import android.view.inputmethod.EditorInfo
import android.widget.EditText
import androidx.appcompat.content.res.AppCompatResources.getDrawable
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.NavHostFragment.findNavController
import kotlinx.android.synthetic.main.fragment_connection.*
import taxi.kassa.R
import taxi.kassa.util.Constants.CITYMOBIL
import taxi.kassa.util.Constants.CONNECTION
import taxi.kassa.util.Constants.GETT
import taxi.kassa.util.Constants.YANDEX

class ConnectionFragment : Fragment() {

    private val taxiType: String by lazy { arguments?.get(CONNECTION) as String }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? = inflater.inflate(R.layout.fragment_connection, container, false)

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        checkEditTextIsComplete()
        clearFocusWhenDoneClicked()

        when (taxiType) {
            YANDEX -> {
                yandex_block.visibility = VISIBLE
                yandex_submit_button.visibility = VISIBLE
                gett_block.visibility = GONE
                gett_submit_button.visibility = GONE
                city_block.visibility = GONE
                city_submit_button.visibility = GONE
                taxi_icon.background = getDrawable(requireContext(), R.drawable.ic_yandex)
                taxi_name.text = getString(R.string.yandex_title)
            }
            GETT -> {
                yandex_block.visibility = GONE
                yandex_submit_button.visibility = GONE
                gett_block.visibility = VISIBLE
                gett_submit_button.visibility = VISIBLE
                city_block.visibility = GONE
                city_submit_button.visibility = GONE
                taxi_icon.background = getDrawable(requireContext(), R.drawable.ic_gett)
                taxi_name.text = getString(R.string.gett_title)
            }
            CITYMOBIL -> {
                yandex_block.visibility = GONE
                yandex_submit_button.visibility = GONE
                gett_block.visibility = GONE
                gett_submit_button.visibility = GONE
                city_block.visibility = VISIBLE
                city_submit_button.visibility = VISIBLE
                taxi_icon.background = getDrawable(requireContext(), R.drawable.ic_citymobil)
                taxi_name.text = getString(R.string.citymobil_title)
            }
        }

        back_arrow.setOnClickListener { activity?.onBackPressed() }

        yandex_submit_button.setOnClickListener {
            findNavController(this).navigate(R.id.action_connectionFragment_to_successRequestFragment)
        }

        gett_submit_button.setOnClickListener {
            findNavController(this).navigate(R.id.action_connectionFragment_to_successRequestFragment)
        }

        city_submit_button.setOnClickListener {
            findNavController(this).navigate(R.id.action_connectionFragment_to_successRequestFragment)
        }
    }

    private fun checkEditTextIsComplete() {
        val editTexts = listOf<EditText>(
            driver_license_edit_text,
            passport_number_edit_text,
            sts_edit_text,
            license_edit_text,
            phone_number_edit_text,
            gett_driver_license_edit_text,
            gett_sts_edit_text,
            gett_license_edit_text,
            gett_phone_edit_text,
            id_edit_text,
            city_driver_license_edit_text,
            city_sts_edit_text,
            city_license_edit_text,
            city_phone_edit_text
        )

        editTexts.map {
            it.setOnFocusChangeListener { _, _ ->
                if (it.text?.isNotBlank() == true) {
                    it.setCompoundDrawablesWithIntrinsicBounds(
                        0,
                        0,
                        R.drawable.ic_check_green,
                        0
                    )
                } else {
                    it.setCompoundDrawablesWithIntrinsicBounds(
                        0,
                        0,
                        0,
                        0
                    )
                }
            }
        }
    }

    private fun clearFocusWhenDoneClicked() {
        val editTexts = listOf<EditText>(
            phone_number_edit_text,
            id_edit_text,
            city_phone_edit_text
        )

        editTexts.map {
            it.setOnEditorActionListener { _, actionId, event ->
                if (event != null && event.keyCode == KeyEvent.KEYCODE_ENTER || actionId == EditorInfo.IME_ACTION_DONE) {
                    it.clearFocus()
                }
                false
            }
        }
    }
}