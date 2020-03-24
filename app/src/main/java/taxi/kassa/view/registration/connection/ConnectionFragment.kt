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
import android.widget.ImageView
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
            Pair(gett_driver_license_edit_text, ImageType.GETT_D_LICENSE),
            Pair(passport_first_number_edit_text, ImageType.GETT_PASSPORT),
            Pair(gett_sts_edit_text, ImageType.GETT_STS),
            Pair(gett_license_edit_text, ImageType.GETT_LICENSE),
            Pair(make_selfie_edit_text, ImageType.GETT_SELFIE),

            Pair(city_driver_license_front_edit_text, ImageType.D_LICENSE_FRONT),
            Pair(city_driver_license_back_edit_text, ImageType.D_LICENSE_BACK),
            Pair(city_passport_first_edit_text, ImageType.PASSPORT_FIRST),
            Pair(city_passport_registration_edit_text, ImageType.PASSPORT_REG),
            Pair(city_sts_edit_text, ImageType.STS),
            Pair(city_license_front_edit_text, ImageType.LICENSE_FRONT),
            Pair(city_license_back_edit_text, ImageType.LICENSE_BACK),
            Pair(front_side_edit_text, ImageType.AUTO_FRONT),
            Pair(back_side_edit_text, ImageType.AUTO_BACK),
            Pair(left_side_edit_text, ImageType.AUTO_LEFT),
            Pair(right_side_edit_text, ImageType.AUTO_RIGHT),
            Pair(city_selfie_edit_text, ImageType.SELFIE)
        )

        imageTypePairs.map {
            setImagePickerClickListener(it.first, it.second)
        }

        city_driver_license_front_edit_text.setHint(R.string.driver_license_front)
        city_driver_license_back_edit_text.setHint(R.string.driver_license_back)

        gett_driver_license_cancel.setOnClickListener { cancelLoadPhoto(it as ImageView) }
        passport_first_number_cancel.setOnClickListener { cancelLoadPhoto(it as ImageView) }
        gett_sts_cancel.setOnClickListener { cancelLoadPhoto(it as ImageView) }
        gett_license_cancel.setOnClickListener { cancelLoadPhoto(it as ImageView) }
        make_selfie_cancel.setOnClickListener { cancelLoadPhoto(it as ImageView) }

        city_driver_license_front_cancel.setOnClickListener { cancelLoadPhoto(it as ImageView) }
        city_driver_license_back_cancel.setOnClickListener { cancelLoadPhoto(it as ImageView) }
        city_passport_first_cancel.setOnClickListener { cancelLoadPhoto(it as ImageView) }
        city_passport_registration_cancel.setOnClickListener { cancelLoadPhoto(it as ImageView) }
        city_sts_cancel.setOnClickListener { cancelLoadPhoto(it as ImageView) }
        city_license_front_cancel.setOnClickListener { cancelLoadPhoto(it as ImageView) }
        city_license_back_cancel.setOnClickListener { cancelLoadPhoto(it as ImageView) }
        front_side_cancel.setOnClickListener { cancelLoadPhoto(it as ImageView) }
        back_side_cancel.setOnClickListener { cancelLoadPhoto(it as ImageView) }
        left_side_cancel.setOnClickListener { cancelLoadPhoto(it as ImageView) }
        right_side_cancel.setOnClickListener { cancelLoadPhoto(it as ImageView) }
        city_selfie_cancel.setOnClickListener { cancelLoadPhoto(it as ImageView) }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        imageTypes.map {
            when (it) {
                ImageType.GETT_D_LICENSE -> {
                    gett_driver_license_input_view.visibility = INVISIBLE
                    gett_driver_license_input_view2.visibility = VISIBLE
                    gett_driver_license_edit_text.visibility = INVISIBLE
                    gett_driver_license_edit_text2.visibility = VISIBLE
                    gett_driver_license_cancel.visibility = VISIBLE
                }
                ImageType.GETT_PASSPORT -> {
                    passport_first_number_input_view.visibility = INVISIBLE
                    passport_first_number_input_view2.visibility = VISIBLE
                    passport_first_number_edit_text.visibility = INVISIBLE
                    passport_first_number_edit_text2.visibility = VISIBLE
                    passport_first_number_cancel.visibility = VISIBLE
                }
                ImageType.GETT_STS -> {
                    gett_sts_input_view.visibility = INVISIBLE
                    gett_sts_input_view2.visibility = VISIBLE
                    gett_sts_edit_text.visibility = INVISIBLE
                    gett_sts_edit_text2.visibility = VISIBLE
                    gett_sts_cancel.visibility = VISIBLE
                }
                ImageType.GETT_LICENSE -> {
                    gett_license_input_view.visibility = INVISIBLE
                    gett_license_input_view2.visibility = VISIBLE
                    gett_license_edit_text.visibility = INVISIBLE
                    gett_license_edit_text2.visibility = VISIBLE
                    gett_license_cancel.visibility = VISIBLE
                }
                ImageType.GETT_SELFIE -> {
                    make_selfie_input_view.visibility = INVISIBLE
                    make_selfie_input_view2.visibility = VISIBLE
                    make_selfie_edit_text.visibility = INVISIBLE
                    make_selfie_edit_text2.visibility = VISIBLE
                    make_selfie_cancel.visibility = VISIBLE
                }

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
                    city_passport_first_cancel.visibility = VISIBLE
                }
                ImageType.PASSPORT_REG -> {
                    city_passport_registration_input_view.visibility = INVISIBLE
                    city_passport_registration_input_view2.visibility = VISIBLE
                    city_passport_registration_edit_text.visibility = INVISIBLE
                    city_passport_registration_edit_text2.visibility = VISIBLE
                    city_passport_registration_cancel.visibility = VISIBLE
                }
                ImageType.STS -> {
                    city_sts_input_view.visibility = INVISIBLE
                    city_sts_input_view2.visibility = VISIBLE
                    city_sts_edit_text.visibility = INVISIBLE
                    city_sts_edit_text2.visibility = VISIBLE
                    city_sts_cancel.visibility = VISIBLE
                }
                ImageType.LICENSE_FRONT -> {
                    city_license_front_input_view.visibility = INVISIBLE
                    city_license_front_input_view2.visibility = VISIBLE
                    city_license_front_edit_text.visibility = INVISIBLE
                    city_license_front_edit_text2.visibility = VISIBLE
                    city_license_front_cancel.visibility = VISIBLE
                }
                ImageType.LICENSE_BACK -> {
                    city_license_back_input_view.visibility = INVISIBLE
                    city_license_back_input_view2.visibility = VISIBLE
                    city_license_back_edit_text.visibility = INVISIBLE
                    city_license_back_edit_text2.visibility = VISIBLE
                    city_license_back_cancel.visibility = VISIBLE
                }
                ImageType.AUTO_FRONT -> {
                    front_side_input_view.visibility = INVISIBLE
                    front_side_input_view2.visibility = VISIBLE
                    front_side_edit_text.visibility = INVISIBLE
                    front_side_edit_text2.visibility = VISIBLE
                    front_side_cancel.visibility = VISIBLE
                }
                ImageType.AUTO_BACK -> {
                    back_side_input_view.visibility = INVISIBLE
                    back_side_input_view2.visibility = VISIBLE
                    back_side_edit_text.visibility = INVISIBLE
                    back_side_edit_text2.visibility = VISIBLE
                    back_side_cancel.visibility = VISIBLE
                }
                ImageType.AUTO_LEFT -> {
                    left_side_input_view.visibility = INVISIBLE
                    left_side_input_view2.visibility = VISIBLE
                    left_side_edit_text.visibility = INVISIBLE
                    left_side_edit_text2.visibility = VISIBLE
                    left_side_cancel.visibility = VISIBLE
                }
                ImageType.AUTO_RIGHT -> {
                    right_side_input_view.visibility = INVISIBLE
                    right_side_input_view2.visibility = VISIBLE
                    right_side_edit_text.visibility = INVISIBLE
                    right_side_edit_text2.visibility = VISIBLE
                    right_side_cancel.visibility = VISIBLE
                }
                ImageType.SELFIE -> {
                    city_selfie_input_view.visibility = INVISIBLE
                    city_selfie_input_view2.visibility = VISIBLE
                    city_selfie_edit_text.visibility = INVISIBLE
                    city_selfie_edit_text2.visibility = VISIBLE
                    city_selfie_cancel.visibility = VISIBLE
                }
            }
        }

        try {
            val image: com.esafirm.imagepicker.model.Image = ImagePicker.getFirstImageOrNull(data)
            images.add(image)
            docs.add(ImageDocument(image.id, image.name, image.path, selectedType))
        } catch (e: IllegalStateException) {
            when (selectedType) {
                ImageType.GETT_D_LICENSE -> {
                    gett_driver_license_input_view.visibility = VISIBLE
                    gett_driver_license_input_view2.visibility = INVISIBLE
                    gett_driver_license_edit_text.visibility = VISIBLE
                    gett_driver_license_edit_text2.visibility = INVISIBLE
                    gett_driver_license_cancel.visibility = INVISIBLE
                }
                ImageType.GETT_PASSPORT -> {
                    passport_first_number_input_view.visibility = VISIBLE
                    passport_first_number_input_view2.visibility = INVISIBLE
                    passport_first_number_edit_text.visibility = VISIBLE
                    passport_first_number_edit_text2.visibility = INVISIBLE
                    passport_first_number_cancel.visibility = INVISIBLE
                }
                ImageType.GETT_STS -> {
                    gett_sts_input_view.visibility = VISIBLE
                    gett_sts_input_view2.visibility = INVISIBLE
                    gett_sts_edit_text.visibility = VISIBLE
                    gett_sts_edit_text2.visibility = INVISIBLE
                    gett_sts_cancel.visibility = INVISIBLE
                }
                ImageType.GETT_LICENSE -> {
                    gett_license_input_view.visibility = VISIBLE
                    gett_license_input_view2.visibility = INVISIBLE
                    gett_license_edit_text.visibility = VISIBLE
                    gett_license_edit_text2.visibility = INVISIBLE
                    gett_license_cancel.visibility = INVISIBLE
                }
                ImageType.GETT_SELFIE -> {
                    make_selfie_input_view.visibility = VISIBLE
                    make_selfie_input_view2.visibility = INVISIBLE
                    make_selfie_edit_text.visibility = VISIBLE
                    make_selfie_edit_text2.visibility = INVISIBLE
                    make_selfie_cancel.visibility = INVISIBLE
                }

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
                ImageType.PASSPORT_FIRST -> {
                    city_passport_first_input_view.visibility = VISIBLE
                    city_passport_first_input_view2.visibility = INVISIBLE
                    city_passport_first_edit_text.visibility = VISIBLE
                    city_passport_first_edit_text2.visibility = INVISIBLE
                    city_passport_first_cancel.visibility = INVISIBLE
                }
                ImageType.PASSPORT_REG -> {
                    city_passport_registration_input_view.visibility = VISIBLE
                    city_passport_registration_input_view2.visibility = INVISIBLE
                    city_passport_registration_edit_text.visibility = VISIBLE
                    city_passport_registration_edit_text2.visibility = INVISIBLE
                    city_passport_registration_cancel.visibility = INVISIBLE
                }
                ImageType.STS -> {
                    city_sts_input_view.visibility = VISIBLE
                    city_sts_input_view2.visibility = INVISIBLE
                    city_sts_edit_text.visibility = VISIBLE
                    city_sts_edit_text2.visibility = INVISIBLE
                    city_sts_cancel.visibility = INVISIBLE
                }
                ImageType.LICENSE_FRONT -> {
                    city_license_front_input_view.visibility = VISIBLE
                    city_license_front_input_view2.visibility = INVISIBLE
                    city_license_front_edit_text.visibility = VISIBLE
                    city_license_front_edit_text2.visibility = INVISIBLE
                    city_license_front_cancel.visibility = INVISIBLE
                }
                ImageType.LICENSE_BACK -> {
                    city_license_back_input_view.visibility = VISIBLE
                    city_license_back_input_view2.visibility = INVISIBLE
                    city_license_back_edit_text.visibility = VISIBLE
                    city_license_back_edit_text2.visibility = INVISIBLE
                    city_license_back_cancel.visibility = INVISIBLE
                }
                ImageType.AUTO_FRONT -> {
                    front_side_input_view.visibility = VISIBLE
                    front_side_input_view2.visibility = INVISIBLE
                    front_side_edit_text.visibility = VISIBLE
                    front_side_edit_text2.visibility = INVISIBLE
                    front_side_cancel.visibility = INVISIBLE
                }
                ImageType.AUTO_BACK -> {
                    back_side_input_view.visibility = VISIBLE
                    back_side_input_view2.visibility = INVISIBLE
                    back_side_edit_text.visibility = VISIBLE
                    back_side_edit_text2.visibility = INVISIBLE
                    back_side_cancel.visibility = INVISIBLE
                }
                ImageType.AUTO_LEFT -> {
                    left_side_input_view.visibility = VISIBLE
                    left_side_input_view2.visibility = INVISIBLE
                    left_side_edit_text.visibility = VISIBLE
                    left_side_edit_text2.visibility = INVISIBLE
                    left_side_cancel.visibility = INVISIBLE
                }
                ImageType.AUTO_RIGHT -> {
                    right_side_input_view.visibility = VISIBLE
                    right_side_input_view2.visibility = INVISIBLE
                    right_side_edit_text.visibility = VISIBLE
                    right_side_edit_text2.visibility = INVISIBLE
                    right_side_cancel.visibility = INVISIBLE
                }
                ImageType.SELFIE -> {
                    city_selfie_input_view.visibility = VISIBLE
                    city_selfie_input_view2.visibility = INVISIBLE
                    city_selfie_edit_text.visibility = VISIBLE
                    city_selfie_edit_text2.visibility = INVISIBLE
                    city_selfie_cancel.visibility = INVISIBLE
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

    private fun cancelLoadPhoto(imageView: ImageView) {
        var imageType = ImageType.D_LICENSE_FRONT

        when (imageView.id) {
            R.id.gett_driver_license_cancel -> {
                imageType = ImageType.GETT_D_LICENSE
                gett_driver_license_input_view.visibility = VISIBLE
                gett_driver_license_input_view2.visibility = INVISIBLE
                gett_driver_license_edit_text.visibility = VISIBLE
                gett_driver_license_edit_text2.visibility = INVISIBLE
                gett_driver_license_cancel.visibility = INVISIBLE
            }
            R.id.passport_first_number_cancel -> {
                passport_first_number_input_view.visibility = VISIBLE
                passport_first_number_input_view2.visibility = INVISIBLE
                passport_first_number_edit_text.visibility = VISIBLE
                passport_first_number_edit_text2.visibility = INVISIBLE
                passport_first_number_cancel.visibility = INVISIBLE
            }
            R.id.gett_sts_cancel -> {
                gett_sts_input_view.visibility = VISIBLE
                gett_sts_input_view2.visibility = INVISIBLE
                gett_sts_edit_text.visibility = VISIBLE
                gett_sts_edit_text2.visibility = INVISIBLE
                gett_sts_cancel.visibility = INVISIBLE
            }
            R.id.gett_license_cancel -> {
                gett_license_input_view.visibility = VISIBLE
                gett_license_input_view2.visibility = INVISIBLE
                gett_license_edit_text.visibility = VISIBLE
                gett_license_edit_text2.visibility = INVISIBLE
                gett_license_cancel.visibility = INVISIBLE
            }
            R.id.make_selfie_cancel -> {
                make_selfie_input_view.visibility = VISIBLE
                make_selfie_input_view2.visibility = INVISIBLE
                make_selfie_edit_text.visibility = VISIBLE
                make_selfie_edit_text2.visibility = INVISIBLE
                make_selfie_cancel.visibility = INVISIBLE
            }

            R.id.city_driver_license_front_cancel -> {
                imageType = ImageType.D_LICENSE_FRONT
                city_driver_license_front_input_view.visibility = VISIBLE
                city_driver_license_front_input_view2.visibility = INVISIBLE
                city_driver_license_front_edit_text.visibility = VISIBLE
                city_driver_license_front_edit_text2.visibility = INVISIBLE
                city_driver_license_front_cancel.visibility = INVISIBLE
            }
            R.id.city_driver_license_back_cancel -> {
                imageType = ImageType.D_LICENSE_BACK
                city_driver_license_back_input_view.visibility = VISIBLE
                city_driver_license_back_input_view2.visibility = INVISIBLE
                city_driver_license_back_edit_text.visibility = VISIBLE
                city_driver_license_back_edit_text2.visibility = INVISIBLE
                city_driver_license_back_cancel.visibility = INVISIBLE
            }
            R.id.city_passport_first_cancel -> {
                imageType = ImageType.PASSPORT_FIRST
                city_passport_first_input_view.visibility = VISIBLE
                city_passport_first_input_view2.visibility = INVISIBLE
                city_passport_first_edit_text.visibility = VISIBLE
                city_passport_first_edit_text2.visibility = INVISIBLE
                city_passport_first_cancel.visibility = INVISIBLE
            }
            R.id.city_passport_registration_cancel -> {
                imageType = ImageType.PASSPORT_REG
                city_passport_registration_input_view.visibility = VISIBLE
                city_passport_registration_input_view2.visibility = INVISIBLE
                city_passport_registration_edit_text.visibility = VISIBLE
                city_passport_registration_edit_text2.visibility = INVISIBLE
                city_passport_registration_cancel.visibility = INVISIBLE
            }
            R.id.city_sts_cancel -> {
                city_sts_input_view.visibility = VISIBLE
                city_sts_input_view2.visibility = INVISIBLE
                city_sts_edit_text.visibility = VISIBLE
                city_sts_edit_text2.visibility = INVISIBLE
                city_sts_cancel.visibility = INVISIBLE
            }
            R.id.city_license_front_cancel -> {
                city_license_front_input_view.visibility = VISIBLE
                city_license_front_input_view2.visibility = INVISIBLE
                city_license_front_edit_text.visibility = VISIBLE
                city_license_front_edit_text2.visibility = INVISIBLE
                city_license_front_cancel.visibility = INVISIBLE
            }
            R.id.city_license_back_cancel -> {
                city_license_back_input_view.visibility = VISIBLE
                city_license_back_input_view2.visibility = INVISIBLE
                city_license_back_edit_text.visibility = VISIBLE
                city_license_back_edit_text2.visibility = INVISIBLE
                city_license_back_cancel.visibility = INVISIBLE
            }
            R.id.front_side_cancel -> {
                front_side_input_view.visibility = VISIBLE
                front_side_input_view2.visibility = INVISIBLE
                front_side_edit_text.visibility = VISIBLE
                front_side_edit_text2.visibility = INVISIBLE
                front_side_cancel.visibility = INVISIBLE
            }
            R.id.back_side_cancel -> {
                back_side_input_view.visibility = VISIBLE
                back_side_input_view2.visibility = INVISIBLE
                back_side_edit_text.visibility = VISIBLE
                back_side_edit_text2.visibility = INVISIBLE
                back_side_cancel.visibility = INVISIBLE
            }
            R.id.left_side_cancel -> {
                left_side_input_view.visibility = VISIBLE
                left_side_input_view2.visibility = INVISIBLE
                left_side_edit_text.visibility = VISIBLE
                left_side_edit_text2.visibility = INVISIBLE
                left_side_cancel.visibility = INVISIBLE
            }
            R.id.right_side_cancel -> {
                right_side_input_view.visibility = VISIBLE
                right_side_input_view2.visibility = INVISIBLE
                right_side_edit_text.visibility = VISIBLE
                right_side_edit_text2.visibility = INVISIBLE
                right_side_cancel.visibility = INVISIBLE
            }
            R.id.city_selfie_cancel -> {
                city_selfie_input_view.visibility = VISIBLE
                city_selfie_input_view2.visibility = INVISIBLE
                city_selfie_edit_text.visibility = VISIBLE
                city_selfie_edit_text2.visibility = INVISIBLE
                city_selfie_cancel.visibility = INVISIBLE
            }
        }

        val doc = docs.find { it.type == imageType }
        val photoForDelete = images.find { it.id == doc?.id }
        images.remove(photoForDelete)

        imageTypes.remove(imageType)
    }

    private fun back() {
        findNavController(this).navigate(R.id.action_connectionFragment_to_registrationSelectionFragment)
    }
}