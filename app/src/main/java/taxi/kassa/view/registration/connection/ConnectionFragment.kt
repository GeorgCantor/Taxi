package taxi.kassa.view.registration.connection

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.View.GONE
import android.view.View.VISIBLE
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import android.widget.ImageView
import androidx.activity.OnBackPressedCallback
import androidx.appcompat.content.res.AppCompatResources.getDrawable
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.navigation.fragment.NavHostFragment.findNavController
import com.google.android.material.textfield.TextInputEditText
import com.redmadrobot.inputmask.MaskedTextChangedListener
import kotlinx.android.synthetic.main.connection_city.*
import kotlinx.android.synthetic.main.connection_gett.*
import kotlinx.android.synthetic.main.connection_yandex.*
import kotlinx.android.synthetic.main.fragment_connection.*
import kotlinx.android.synthetic.main.fragment_connection.city_block
import kotlinx.android.synthetic.main.fragment_connection.gett_block
import kotlinx.android.synthetic.main.fragment_connection.yandex_block
import kotlinx.android.synthetic.main.keyboard.*
import org.koin.androidx.viewmodel.ext.android.getSharedViewModel
import org.koin.core.parameter.parametersOf
import taxi.kassa.R
import taxi.kassa.model.LoadImage
import taxi.kassa.util.*
import taxi.kassa.util.Constants.CITYMOBIL
import taxi.kassa.util.Constants.CONNECTION
import taxi.kassa.util.Constants.GETT
import taxi.kassa.util.Constants.PHONE_MASK
import taxi.kassa.util.Constants.YANDEX

class ConnectionFragment : Fragment() {

    private val taxiType: String by lazy { arguments?.get(CONNECTION) as String }

    private lateinit var viewModel: ConnectionViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        viewModel = getSharedViewModel { parametersOf() }
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? = inflater.inflate(R.layout.fragment_connection, container, false)

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val callback = object : OnBackPressedCallback(true) {
            override fun handleOnBackPressed() {
                back()
            }
        }
        activity?.onBackPressedDispatcher?.addCallback(viewLifecycleOwner, callback)

        val editTexts = listOf<TextInputEditText>(
            phone_number_edit_text,
            gett_phone_edit_text,
            id_edit_text,
            city_phone_edit_text
        )

        editTexts.map {
            it.showSoftInputOnFocus = false

            it.setOnFocusChangeListener { _, hasFocus ->
                when (hasFocus) {
                    true -> keyboard.visibility = VISIBLE
                    false -> keyboard.visibility = GONE
                }
            }

            it.setOnClickListener { if (keyboard.visibility == GONE) keyboard.visibility = VISIBLE }
        }

        val keyboardPairs = mutableListOf<Pair<Button, Int>>(
            Pair(num_0, R.string.num0),
            Pair(num_1, R.string.num1),
            Pair(num_2, R.string.num2),
            Pair(num_3, R.string.num3),
            Pair(num_4, R.string.num4),
            Pair(num_5, R.string.num5),
            Pair(num_6, R.string.num6),
            Pair(num_7, R.string.num7),
            Pair(num_8, R.string.num8),
            Pair(num_9, R.string.num9)
        )

        keyboardPairs.map {
            setNumberClickListener(it.first, it.second)
        }

        erase_btn.setOnClickListener {
            var focusedInput = phone_number_edit_text
            editTexts.map {
                if (it.isFocused) focusedInput = it
            }

            val cursorPosition = focusedInput.selectionStart
            if (cursorPosition > 0) {
                focusedInput.text = focusedInput.text?.delete(cursorPosition - 1, cursorPosition)
                focusedInput.setSelection(cursorPosition - 1)
            }
        }

        apply_btn.setOnClickListener {
            var focusedInput = phone_number_edit_text
            editTexts.map {
                if (it.isFocused) focusedInput = it
            }

            when (focusedInput.id) {
                R.id.phone_number_edit_text, R.id.id_edit_text, R.id.city_phone_edit_text -> keyboard.visibility = GONE
                R.id.gett_phone_edit_text -> id_edit_text.requestFocus()
            }
        }

        val phoneEditTexts = listOf<EditText>(
            phone_number_edit_text,
            gett_phone_edit_text,
            city_phone_edit_text
        )
        phoneEditTexts.map {
            it.addTextChangedListener(PhoneMaskListener(it))
        }

        when (taxiType) {
            YANDEX -> {
                yandex_block.visible()
                yandex_submit_button.visible()
                gett_block.gone()
                gett_submit_button.gone()
                city_block.gone()
                city_submit_button.gone()
                taxi_icon.background = getDrawable(requireContext(), R.drawable.ic_yandex)
                taxi_name.text = getString(R.string.yandex_title)
            }
            GETT -> {
                yandex_block.gone()
                yandex_submit_button.gone()
                gett_block.visible()
                gett_submit_button.visible()
                city_block.gone()
                city_submit_button.gone()
                taxi_icon.background = getDrawable(requireContext(), R.drawable.ic_gett)
                taxi_name.text = getString(R.string.gett_title)
            }
            CITYMOBIL -> {
                yandex_block.gone()
                yandex_submit_button.gone()
                gett_block.gone()
                gett_submit_button.gone()
                city_block.visible()
                city_submit_button.visible()
                taxi_icon.background = getDrawable(requireContext(), R.drawable.ic_citymobil)
                taxi_name.text = getString(R.string.citymobil_title)
            }
        }

        back_arrow.setOnClickListener { backToRegScreen() }

        val yandexCancelButtons = mutableListOf<ImageView>(
            driver_license_cancel,
            passport_first_cancel,
            passport_reg_cancel,
            sts_cancel,
            license_cancel
        )

        val gettCancelButtons = mutableListOf<ImageView>(
            gett_driver_license_cancel,
            passport_first_number_cancel,
            gett_sts_cancel,
            gett_license_cancel,
            make_selfie_cancel
        )

        val cityCancelButtons = mutableListOf<ImageView>(
            city_driver_license_front_cancel,
            city_driver_license_back_cancel,
            city_passport_first_cancel,
            city_passport_registration_cancel,
            city_sts_cancel,
            city_sts_back_cancel,
            city_license_front_cancel,
            city_license_back_cancel,
            front_side_cancel,
            back_side_cancel,
            left_side_cancel,
            right_side_cancel,
            city_selfie_cancel
        )

        yandex_submit_button.setOnClickListener {
            checkFieldsAndSubmit(yandexCancelButtons, mutableListOf(phone_number_edit_text))
        }

        gett_submit_button.setOnClickListener {
            checkFieldsAndSubmit(gettCancelButtons, mutableListOf(gett_phone_edit_text, id_edit_text))
        }

        city_submit_button.setOnClickListener {
            checkFieldsAndSubmit(cityCancelButtons, mutableListOf(city_phone_edit_text))
        }

        city_driver_license_front_edit_text.setHint(R.string.driver_license_front)
        city_driver_license_back_edit_text.setHint(R.string.driver_license_back)

       val yandexDLicenseViews = arrayOf(
            driver_license_input_view,
            driver_license_input_view2,
            driver_license_edit_text,
            driver_license_edit_text2,
            driver_license_cancel
        )
        val yandexPasportFirstViews = arrayOf(
            passport_first_input_view,
            passport_first_input_view2,
            passport_first_edit_text,
            passport_first_edit_text2,
            passport_first_cancel
        )
        val yandexPasportRegViews = arrayOf(
            passport_reg_input_view,
            passport_reg_input_view2,
            passport_reg_edit_text,
            passport_reg_edit_text2,
            passport_reg_cancel
        )
        val yandexStsViews = arrayOf(
            sts_input_view,
            sts_input_view2,
            sts_edit_text,
            sts_edit_text2,
            sts_cancel
        )
        val yandexLicenseViews = arrayOf(
            license_input_view,
            license_input_view2,
            license_edit_text,
            license_edit_text2,
            license_cancel
        )

        val gettDLicenseViews = arrayOf(
            gett_driver_license_input_view,
            gett_driver_license_input_view2,
            gett_driver_license_edit_text,
            gett_driver_license_edit_text2,
            gett_driver_license_cancel
        )
        val gettPassportViews = arrayOf(
            passport_first_number_input_view,
            passport_first_number_input_view2,
            passport_first_number_edit_text,
            passport_first_number_edit_text2,
            passport_first_number_cancel
        )
        val gettStsViews = arrayOf(
            gett_sts_input_view,
            gett_sts_input_view2,
            gett_sts_edit_text,
            gett_sts_edit_text2,
            gett_sts_cancel
        )
        val gettLicenseViews = arrayOf(
            gett_license_input_view,
            gett_license_input_view2,
            gett_license_edit_text,
            gett_license_edit_text2,
            gett_license_cancel
        )
        val gettSelfieViews = arrayOf(
            make_selfie_input_view,
            make_selfie_input_view2,
            make_selfie_edit_text,
            make_selfie_edit_text2,
            make_selfie_cancel
        )

        val cityDLicenseFrontViews = arrayOf(
            city_driver_license_front_input_view,
            city_driver_license_front_input_view2,
            city_driver_license_front_edit_text,
            city_driver_license_front_edit_text2,
            city_driver_license_front_cancel
        )
        val cityDLicenseBackViews = arrayOf(
            city_driver_license_back_input_view,
            city_driver_license_back_input_view2,
            city_driver_license_back_edit_text,
            city_driver_license_back_edit_text2,
            city_driver_license_back_cancel
        )
        val cityPassportFirstViews = arrayOf(
            city_passport_first_input_view,
            city_passport_first_input_view2,
            city_passport_first_edit_text,
            city_passport_first_edit_text2,
            city_passport_first_cancel
        )
        val cityPassportRegViews = arrayOf(
            city_passport_registration_input_view,
            city_passport_registration_input_view2,
            city_passport_registration_edit_text,
            city_passport_registration_edit_text2,
            city_passport_registration_cancel
        )
        val cityStsViews = arrayOf(
            city_sts_input_view,
            city_sts_input_view2,
            city_sts_edit_text,
            city_sts_edit_text2,
            city_sts_cancel
        )
        val cityStsBackViews = arrayOf(
            city_sts_back_input_view,
            city_sts_back_input_view2,
            city_sts_back_edit_text,
            city_sts_back_edit_text2,
            city_sts_back_cancel
        )
        val cityLicenseFrontViews = arrayOf(
            city_license_front_input_view,
            city_license_front_input_view2,
            city_license_front_edit_text,
            city_license_front_edit_text2,
            city_license_front_cancel
        )
        val cityLicenseBackViews = arrayOf(
            city_license_back_input_view,
            city_license_back_input_view2,
            city_license_back_edit_text,
            city_license_back_edit_text2,
            city_license_back_cancel
        )
        val cityFrontSideViews = arrayOf(
            front_side_input_view,
            front_side_input_view2,
            front_side_edit_text,
            front_side_edit_text2,
            front_side_cancel
        )
        val cityBackSideViews = arrayOf(
            back_side_input_view,
            back_side_input_view2,
            back_side_edit_text,
            back_side_edit_text2,
            back_side_cancel
        )
        val cityLeftSideViews = arrayOf(
            left_side_input_view,
            left_side_input_view2,
            left_side_edit_text,
            left_side_edit_text2,
            left_side_cancel
        )
        val cityRightSideViews = arrayOf(
            right_side_input_view,
            right_side_input_view2,
            right_side_edit_text,
            right_side_edit_text2,
            right_side_cancel
        )
        val citySelfieViews = arrayOf(
            city_selfie_input_view,
            city_selfie_input_view2,
            city_selfie_edit_text,
            city_selfie_edit_text2,
            city_selfie_cancel
        )

        val inputs = listOf<EditText>(
            driver_license_edit_text,
            passport_first_edit_text,
            passport_reg_edit_text,
            sts_edit_text,
            license_edit_text,
            gett_driver_license_edit_text,
            passport_first_number_edit_text,
            gett_sts_edit_text,
            gett_license_edit_text,
            make_selfie_edit_text,
            city_driver_license_front_edit_text,
            city_driver_license_back_edit_text,
            city_passport_first_edit_text,
            city_passport_registration_edit_text,
            city_sts_edit_text,
            city_sts_back_edit_text,
            city_license_front_edit_text,
            city_license_back_edit_text,
            front_side_edit_text,
            back_side_edit_text,
            left_side_edit_text,
            right_side_edit_text,
            city_selfie_edit_text
        )

        inputs.map { editText ->
            editText.setOnClickListener {
                when (editText.id) {
                    R.id.driver_license_edit_text -> viewModel.setSelected(1)
                    R.id.passport_first_edit_text -> viewModel.setSelected(2)
                    R.id.passport_reg_edit_text -> viewModel.setSelected(3)
                    R.id.sts_edit_text -> viewModel.setSelected(4)
                    R.id.license_edit_text -> viewModel.setSelected(5)
                    R.id.gett_driver_license_edit_text -> viewModel.setSelected(6)
                    R.id.passport_first_number_edit_text -> viewModel.setSelected(7)
                    R.id.gett_sts_edit_text -> viewModel.setSelected(8)
                    R.id.gett_license_edit_text -> viewModel.setSelected(9)
                    R.id.make_selfie_edit_text -> viewModel.setSelected(10)
                    R.id.city_driver_license_front_edit_text -> viewModel.setSelected(11)
                    R.id.city_driver_license_back_edit_text -> viewModel.setSelected(12)
                    R.id.city_passport_first_edit_text -> viewModel.setSelected(13)
                    R.id.city_passport_registration_edit_text -> viewModel.setSelected(14)
                    R.id.city_sts_edit_text -> viewModel.setSelected(15)
                    R.id.city_sts_back_edit_text -> viewModel.setSelected(16)
                    R.id.city_license_front_edit_text -> viewModel.setSelected(17)
                    R.id.city_license_back_edit_text -> viewModel.setSelected(18)
                    R.id.front_side_edit_text -> viewModel.setSelected(19)
                    R.id.back_side_edit_text -> viewModel.setSelected(20)
                    R.id.left_side_edit_text -> viewModel.setSelected(21)
                    R.id.right_side_edit_text -> viewModel.setSelected(22)
                    R.id.city_selfie_edit_text -> viewModel.setSelected(23)
                }

                findNavController(this).navigate(
                    R.id.action_connectionFragment_to_photoFragment,
                    Bundle().apply { putString(CONNECTION, taxiType) }
                )
            }
        }

        driver_license_edit_text.setOnClickListener {
            viewModel.setSelected(1)
            findNavController(this).navigate(R.id.action_connectionFragment_to_photoFragment)
        }

        val allCancelButtons = yandexCancelButtons + gettCancelButtons + cityCancelButtons

        allCancelButtons.map { imageView ->
            imageView.setOnClickListener {
                when (imageView.id) {
                    R.id.driver_license_cancel -> viewModel.removeLoadImage(1)
                    R.id.passport_first_cancel -> viewModel.removeLoadImage(2)
                    R.id.passport_reg_cancel -> viewModel.removeLoadImage(3)
                    R.id.sts_cancel -> viewModel.removeLoadImage(4)
                    R.id.license_cancel -> viewModel.removeLoadImage(5)
                    R.id.gett_driver_license_cancel -> viewModel.removeLoadImage(6)
                    R.id.passport_first_number_cancel -> viewModel.removeLoadImage(7)
                    R.id.gett_sts_cancel -> viewModel.removeLoadImage(8)
                    R.id.gett_license_cancel -> viewModel.removeLoadImage(9)
                    R.id.make_selfie_cancel -> viewModel.removeLoadImage(10)
                    R.id.city_driver_license_front_cancel -> viewModel.removeLoadImage(11)
                    R.id.city_driver_license_back_cancel -> viewModel.removeLoadImage(12)
                    R.id.city_passport_first_cancel -> viewModel.removeLoadImage(13)
                    R.id.city_passport_registration_cancel -> viewModel.removeLoadImage(14)
                    R.id.city_sts_cancel -> viewModel.removeLoadImage(15)
                    R.id.city_sts_back_cancel -> viewModel.removeLoadImage(16)
                    R.id.city_license_front_cancel -> viewModel.removeLoadImage(17)
                    R.id.city_license_back_cancel -> viewModel.removeLoadImage(18)
                    R.id.front_side_cancel -> viewModel.removeLoadImage(19)
                    R.id.back_side_cancel -> viewModel.removeLoadImage(20)
                    R.id.left_side_cancel -> viewModel.removeLoadImage(21)
                    R.id.right_side_cancel -> viewModel.removeLoadImage(22)
                    R.id.city_selfie_cancel -> viewModel.removeLoadImage(23)
                }
            }
        }

        val inputViews = mutableListOf<Array<View>>(
            yandexDLicenseViews,
            yandexPasportFirstViews,
            yandexPasportRegViews,
            yandexStsViews,
            yandexLicenseViews,
            gettDLicenseViews,
            gettPassportViews,
            gettStsViews,
            gettLicenseViews,
            gettSelfieViews,
            cityDLicenseFrontViews,
            cityDLicenseBackViews,
            cityPassportFirstViews,
            cityPassportRegViews,
            cityStsViews,
            cityStsBackViews,
            cityLicenseFrontViews,
            cityLicenseBackViews,
            cityFrontSideViews,
            cityBackSideViews,
            cityLeftSideViews,
            cityRightSideViews,
            citySelfieViews
        )

        viewModel.loadedImages.observe(viewLifecycleOwner, Observer { loadImages ->
            var id = 1
            inputViews.map {
                setInputViewsState(loadImages, id, it)
                id++
            }
        })
    }

    // go through the downloaded images and set the editTexts visibility
    private fun setInputViewsState(images: MutableList<LoadImage>, id: Int, inputs: Array<View>) {
        images.find { it.id == id }
            .let { if (it != null) inputs.setLoadPhotoVisibility() else inputs.setNormalVisibility() }
    }

    private fun setNumberClickListener(button: Button, resource: Int) {
        val editTexts = listOf<TextInputEditText>(
            phone_number_edit_text,
            gett_phone_edit_text,
            id_edit_text,
            city_phone_edit_text
        )

        button.setOnClickListener {
            var focusedInput = phone_number_edit_text
            editTexts.map {
                if (it.isFocused) focusedInput = it
            }

            focusedInput.text?.insert(focusedInput.selectionStart, getString(resource))
        }
    }

    private fun checkFieldsAndSubmit(
        cancelButtons: MutableList<ImageView>,
        editTexts: MutableList<EditText>
    ) {
        cancelButtons.map {
            if (!it.isVisible) {
                context?.longToast(getString(R.string.load_all_photos))
                return
            }
        }

        editTexts.map {
            if (it.text.isNullOrBlank()) {
                context?.longToast(getString(R.string.fill_all_fields))
                return
            }
        }

        findNavController(this).navigate(R.id.action_connectionFragment_to_successRequestFragment)
    }

    private fun back() {
        when (keyboard.visibility) {
            VISIBLE -> keyboard.visibility = GONE
            GONE -> backToRegScreen()
        }
    }

    private fun backToRegScreen() = findNavController(this).popBackStack()

    inner class PhoneMaskListener(editText: EditText) : MaskedTextChangedListener(PHONE_MASK, editText, object : ValueListener {
        override fun onTextChanged(maskFilled: Boolean, extractedValue: String, formattedValue: String) {
        }
    })
}