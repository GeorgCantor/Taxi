package taxi.kassa.view.registration.connection

import android.content.Intent
import android.os.Bundle
import android.view.KeyEvent
import android.view.LayoutInflater
import android.view.View
import android.view.View.*
import android.view.ViewGroup
import android.view.inputmethod.EditorInfo
import android.widget.EditText
import androidx.activity.OnBackPressedCallback
import androidx.appcompat.content.res.AppCompatResources.getDrawable
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.NavHostFragment.findNavController
import com.esafirm.imagepicker.features.ImagePicker
import com.esafirm.imagepicker.features.ReturnMode
import kotlinx.android.synthetic.main.fragment_connection.*
import taxi.kassa.R
import taxi.kassa.model.ImageDocument
import taxi.kassa.util.Constants.CITYMOBIL
import taxi.kassa.util.Constants.CONNECTION
import taxi.kassa.util.Constants.GETT
import taxi.kassa.util.Constants.YANDEX

class ConnectionFragment : Fragment() {

    private val taxiType: String by lazy { arguments?.get(CONNECTION) as String }
    private val images = mutableListOf<com.esafirm.imagepicker.model.Image>()
    private val docs = mutableListOf<ImageDocument>()
    private val imageTypes = mutableListOf<ImageType>()

    private lateinit var selectedType: ImageType

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? = inflater.inflate(R.layout.fragment_connection, container, false)

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        checkEditTextIsComplete()
        clearFocusWhenDoneClicked()

        val callback = object : OnBackPressedCallback(true) {
            override fun handleOnBackPressed() {
                back()
            }
        }
        requireActivity().onBackPressedDispatcher.addCallback(viewLifecycleOwner, callback)

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

        val imageTypePairs = mutableListOf<Pair<EditText, ImageType>>(
            Pair(city_driver_license_front_edit_text, ImageType.D_LICENSE_FRONT),
            Pair(city_driver_license_back_edit_text, ImageType.D_LICENSE_BACK),
            Pair(city_passport_first_edit_text, ImageType.PASSPORT_FIRST),
            Pair(city_passport_registration_edit_text, ImageType.PASSPORT_REG),
            Pair(front_side_edit_text, ImageType.AUTO_FRONT)
        )

        imageTypePairs.map {
            setImagePickerClickListener(it.first, it.second)
        }

        city_driver_license_front_edit_text.setHint(R.string.driver_license_front)
        city_driver_license_back_edit_text.setHint(R.string.driver_license_back)

        city_driver_license_front_cancel.setOnClickListener {
            val doc = docs.find { it.type == ImageType.D_LICENSE_FRONT }
            val photoForDelete = images.find { it.id == doc?.id }
            images.remove(photoForDelete)

            city_driver_license_front_input_view.visibility = VISIBLE
            city_driver_license_front_input_view2.visibility = INVISIBLE
            city_driver_license_front_edit_text.visibility = VISIBLE
            city_driver_license_front_edit_text2.visibility = INVISIBLE
            city_driver_license_front_cancel.visibility = INVISIBLE
        }

        city_driver_license_back_cancel.setOnClickListener {
            val doc = docs.find { it.type == ImageType.D_LICENSE_FRONT }
            val photoForDelete = images.find { it.id == doc?.id }
            images.remove(photoForDelete)

            city_driver_license_back_input_view.visibility = VISIBLE
            city_driver_license_back_input_view2.visibility = INVISIBLE
            city_driver_license_back_edit_text.visibility = VISIBLE
            city_driver_license_back_edit_text2.visibility = INVISIBLE
            city_driver_license_back_cancel.visibility = INVISIBLE
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        imageTypes.map {
            when (it) {
                ImageType.D_LICENSE_FRONT -> {
                    city_driver_license_front_input_view.visibility = INVISIBLE
                    city_driver_license_front_input_view2.visibility = VISIBLE
                    city_driver_license_front_edit_text.visibility = INVISIBLE
                    city_driver_license_front_edit_text2.visibility = VISIBLE
                    city_driver_license_front_cancel.visibility = VISIBLE
                }
                ImageType.D_LICENSE_BACK -> {
                    city_driver_license_back_input_view.visibility = INVISIBLE
                    city_driver_license_back_input_view2.visibility = VISIBLE
                    city_driver_license_back_edit_text.visibility = INVISIBLE
                    city_driver_license_back_edit_text2.visibility = VISIBLE
                    city_driver_license_back_cancel.visibility = VISIBLE
                }
                ImageType.PASSPORT_FIRST -> {
                    city_passport_first_input_view.visibility = INVISIBLE
                    city_passport_first_input_view2.visibility = VISIBLE
                    city_passport_first_edit_text.visibility = INVISIBLE
                    city_passport_first_edit_text2.visibility = VISIBLE
                }
                ImageType.PASSPORT_REG -> {
                    city_passport_registration_input_view.visibility = INVISIBLE
                    city_passport_registration_input_view2.visibility = VISIBLE
                    city_passport_registration_edit_text.visibility = INVISIBLE
                    city_passport_registration_edit_text2.visibility = VISIBLE
                }
                ImageType.STS -> {
                }
                ImageType.LICENSE_FRONT -> {
                }
                ImageType.LICENSE_BACK -> {
                }
                ImageType.AUTO_FRONT -> {

                }
                ImageType.AUTO_BACK -> {
                }
                ImageType.AUTO_LEFT -> {
                }
                ImageType.AUTO_RIGHT -> {
                }
                ImageType.SELFIE -> {
                }
            }
        }

        try {
            val image: com.esafirm.imagepicker.model.Image = ImagePicker.getFirstImageOrNull(data)
            images.add(image)
            docs.add(ImageDocument(image.id, image.name, image.path, selectedType))
        } catch (e: IllegalStateException) {
            when (selectedType) {
                ImageType.D_LICENSE_FRONT -> {
                    city_driver_license_front_input_view.visibility = VISIBLE
                    city_driver_license_front_input_view2.visibility = INVISIBLE
                    city_driver_license_front_edit_text.visibility = VISIBLE
                    city_driver_license_front_edit_text2.visibility = INVISIBLE
                    city_driver_license_front_cancel.visibility = INVISIBLE
                }
                ImageType.D_LICENSE_BACK -> {
                    city_driver_license_back_input_view.visibility = VISIBLE
                    city_driver_license_back_input_view2.visibility = INVISIBLE
                    city_driver_license_back_edit_text.visibility = VISIBLE
                    city_driver_license_back_edit_text2.visibility = INVISIBLE
                    city_driver_license_back_cancel.visibility = INVISIBLE
                }
            }
        }

        super.onActivityResult(requestCode, resultCode, data)
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
            city_sts_edit_text,
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

    private fun setImagePickerClickListener(editText: EditText, imageType: ImageType) {
        editText.setOnClickListener {
            imageTypes.add(imageType)
            selectedType = imageType

            ImagePicker.create(this)
                .returnMode(ReturnMode.GALLERY_ONLY)
                .folderMode(true)
                .single()
                .toolbarFolderTitle(getString(R.string.photo_loading))
                .toolbarImageTitle(getString(R.string.photo_loading))
                .theme(R.style.AppTheme)
                .start(0)
        }
    }

    private fun back() {
        findNavController(this).navigate(R.id.action_connectionFragment_to_registrationSelectionFragment)
    }
}