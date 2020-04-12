package taxi.kassa.view.registration.connection

import android.content.Intent
import android.os.Bundle
import android.view.KeyEvent
import android.view.LayoutInflater
import android.view.View
import android.view.View.VISIBLE
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
import taxi.kassa.util.gone
import taxi.kassa.util.setLoadPhotoVisibility
import taxi.kassa.util.setNormalVisibility
import taxi.kassa.util.visible

class ConnectionFragment : Fragment() {

    private val taxiType: String by lazy { arguments?.get(CONNECTION) as String }
    private val images = mutableListOf<com.esafirm.imagepicker.model.Image>()
    private val docs = mutableListOf<ImageDocument>()
    private val imageTypes = mutableListOf<ImageType>()

    private var yandexDLicenseViews = arrayOf<View>()
    private var yandexPasportFirstViews = arrayOf<View>()
    private var yandexPasportRegViews = arrayOf<View>()
    private var yandexStsViews = arrayOf<View>()
    private var yandexLicenseViews = arrayOf<View>()

    private var gettDLicenseViews = arrayOf<View>()
    private var gettPassportViews = arrayOf<View>()
    private var gettStsViews = arrayOf<View>()
    private var gettLicenseViews = arrayOf<View>()
    private var gettSelfieViews = arrayOf<View>()

    private var cityDLicenseFrontViews = arrayOf<View>()
    private var cityDLicenseBackViews = arrayOf<View>()
    private var cityPassportFirstViews = arrayOf<View>()
    private var cityPassportRegViews = arrayOf<View>()
    private var cityStsViews = arrayOf<View>()
    private var cityLicenseFrontViews = arrayOf<View>()
    private var cityLicenseBackViews = arrayOf<View>()
    private var cityFrontSideViews = arrayOf<View>()
    private var cityBackSideViews = arrayOf<View>()
    private var cityLeftSideViews = arrayOf<View>()
    private var cityRightSideViews = arrayOf<View>()
    private var citySelfieViews = arrayOf<View>()

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
        activity?.onBackPressedDispatcher?.addCallback(viewLifecycleOwner, callback)

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
            Pair(driver_license_edit_text, ImageType.YANDEX_D_LICENSE),
            Pair(passport_first_edit_text, ImageType.YANDEX_PASSPORT_FIRST),
            Pair(passport_reg_edit_text, ImageType.YANDEX_PASSPORT_REG),
            Pair(sts_edit_text, ImageType.YANDEX_STS),
            Pair(license_edit_text, ImageType.YANDEX_LICENSE),

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

        driver_license_cancel.setOnClickListener { cancelLoadPhoto(it as ImageView) }
        passport_first_cancel.setOnClickListener { cancelLoadPhoto(it as ImageView) }
        passport_reg_cancel.setOnClickListener { cancelLoadPhoto(it as ImageView) }
        sts_cancel.setOnClickListener { cancelLoadPhoto(it as ImageView) }
        license_cancel.setOnClickListener { cancelLoadPhoto(it as ImageView) }

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

        yandexDLicenseViews = arrayOf(
            driver_license_input_view,
            driver_license_input_view2,
            driver_license_edit_text,
            driver_license_edit_text2,
            driver_license_cancel
        )
        yandexPasportFirstViews = arrayOf(
            passport_first_input_view,
            passport_first_input_view2,
            passport_first_edit_text,
            passport_first_edit_text2,
            passport_first_cancel
        )
        yandexPasportRegViews = arrayOf(
            passport_reg_input_view,
            passport_reg_input_view2,
            passport_reg_edit_text,
            passport_reg_edit_text2,
            passport_reg_cancel
        )
        yandexStsViews = arrayOf(
            sts_input_view,
            sts_input_view2,
            sts_edit_text,
            sts_edit_text2,
            sts_cancel
        )
        yandexLicenseViews = arrayOf(
            license_input_view,
            license_input_view2,
            license_edit_text,
            license_edit_text2,
            license_cancel
        )

        gettDLicenseViews = arrayOf(
            gett_driver_license_input_view,
            gett_driver_license_input_view2,
            gett_driver_license_edit_text,
            gett_driver_license_edit_text2,
            gett_driver_license_cancel
        )
        gettPassportViews = arrayOf(
            passport_first_number_input_view,
            passport_first_number_input_view2,
            passport_first_number_edit_text,
            passport_first_number_edit_text2,
            passport_first_number_cancel
        )
        gettStsViews = arrayOf(
            gett_sts_input_view,
            gett_sts_input_view2,
            gett_sts_edit_text,
            gett_sts_edit_text2,
            gett_sts_cancel
        )
        gettLicenseViews = arrayOf(
            gett_license_input_view,
            gett_license_input_view2,
            gett_license_edit_text,
            gett_license_edit_text2,
            gett_license_cancel
        )
        gettSelfieViews = arrayOf(
            make_selfie_input_view,
            make_selfie_input_view2,
            make_selfie_edit_text,
            make_selfie_edit_text2,
            make_selfie_cancel
        )

        cityDLicenseFrontViews = arrayOf(
            city_driver_license_front_input_view,
            city_driver_license_front_input_view2,
            city_driver_license_front_edit_text,
            city_driver_license_front_edit_text2,
            city_driver_license_front_cancel
        )
        cityDLicenseBackViews = arrayOf(
            city_driver_license_back_input_view,
            city_driver_license_back_input_view2,
            city_driver_license_back_edit_text,
            city_driver_license_back_edit_text2,
            city_driver_license_back_cancel
        )
        cityPassportFirstViews = arrayOf(
            city_passport_first_input_view,
            city_passport_first_input_view2,
            city_passport_first_edit_text,
            city_passport_first_edit_text2,
            city_passport_first_cancel
        )
        cityPassportRegViews = arrayOf(
            city_passport_registration_input_view,
            city_passport_registration_input_view2,
            city_passport_registration_edit_text,
            city_passport_registration_edit_text2,
            city_passport_registration_cancel
        )
        cityStsViews = arrayOf(
            city_sts_input_view,
            city_sts_input_view2,
            city_sts_edit_text,
            city_sts_edit_text2,
            city_sts_cancel
        )
        cityLicenseFrontViews = arrayOf(
            city_license_front_input_view,
            city_license_front_input_view2,
            city_license_front_edit_text,
            city_license_front_edit_text2,
            city_license_front_cancel
        )
        cityLicenseBackViews = arrayOf(
            city_license_back_input_view,
            city_license_back_input_view2,
            city_license_back_edit_text,
            city_license_back_edit_text2,
            city_license_back_cancel
        )
        cityFrontSideViews = arrayOf(
            front_side_input_view,
            front_side_input_view2,
            front_side_edit_text,
            front_side_edit_text2,
            front_side_cancel
        )
        cityBackSideViews = arrayOf(
            back_side_input_view,
            back_side_input_view2,
            back_side_edit_text,
            back_side_edit_text2,
            back_side_cancel
        )
        cityLeftSideViews = arrayOf(
            left_side_input_view,
            left_side_input_view2,
            left_side_edit_text,
            left_side_edit_text2,
            left_side_cancel
        )
        cityRightSideViews = arrayOf(
            right_side_input_view,
            right_side_input_view2,
            right_side_edit_text,
            right_side_edit_text2,
            right_side_cancel
        )
        citySelfieViews = arrayOf(
            city_selfie_input_view,
            city_selfie_input_view2,
            city_selfie_edit_text,
            city_selfie_edit_text2,
            city_selfie_cancel
        )
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        imageTypes.map {
            when (it) {
                ImageType.YANDEX_D_LICENSE -> yandexDLicenseViews.setLoadPhotoVisibility()
                ImageType.YANDEX_PASSPORT_FIRST -> yandexPasportFirstViews.setLoadPhotoVisibility()
                ImageType.YANDEX_PASSPORT_REG -> yandexPasportRegViews.setLoadPhotoVisibility()
                ImageType.YANDEX_STS -> yandexStsViews.setLoadPhotoVisibility()
                ImageType.YANDEX_LICENSE -> yandexLicenseViews.setLoadPhotoVisibility()

                ImageType.GETT_D_LICENSE -> gettDLicenseViews.setLoadPhotoVisibility()
                ImageType.GETT_PASSPORT -> gettPassportViews.setLoadPhotoVisibility()
                ImageType.GETT_STS -> gettStsViews.setLoadPhotoVisibility()
                ImageType.GETT_LICENSE -> gettLicenseViews.setLoadPhotoVisibility()
                ImageType.GETT_SELFIE -> gettSelfieViews.setLoadPhotoVisibility()

                ImageType.D_LICENSE_FRONT -> cityDLicenseFrontViews.setLoadPhotoVisibility()
                ImageType.D_LICENSE_BACK -> cityDLicenseBackViews.setLoadPhotoVisibility()
                ImageType.PASSPORT_FIRST -> cityPassportFirstViews.setLoadPhotoVisibility()
                ImageType.PASSPORT_REG -> cityPassportRegViews.setLoadPhotoVisibility()
                ImageType.STS -> cityStsViews.setLoadPhotoVisibility()
                ImageType.LICENSE_FRONT -> cityLicenseFrontViews.setLoadPhotoVisibility()
                ImageType.LICENSE_BACK -> cityLicenseBackViews.setLoadPhotoVisibility()
                ImageType.AUTO_FRONT -> cityFrontSideViews.setLoadPhotoVisibility()
                ImageType.AUTO_BACK -> cityBackSideViews.setLoadPhotoVisibility()
                ImageType.AUTO_LEFT -> cityLeftSideViews.setLoadPhotoVisibility()
                ImageType.AUTO_RIGHT -> cityRightSideViews.setLoadPhotoVisibility()
                ImageType.SELFIE -> citySelfieViews.setLoadPhotoVisibility()
            }
        }

        try {
            val image: com.esafirm.imagepicker.model.Image = ImagePicker.getFirstImageOrNull(data)
            images.add(image)
            docs.add(ImageDocument(image.id, image.name, image.path, selectedType))
        } catch (e: IllegalStateException) {
            // if the user returned without selecting a photo
            when (selectedType) {
                ImageType.YANDEX_D_LICENSE -> {
                    imageTypes.remove(ImageType.YANDEX_D_LICENSE)
                    yandexDLicenseViews.setNormalVisibility()
                }
                ImageType.YANDEX_PASSPORT_FIRST -> {
                    imageTypes.remove(ImageType.YANDEX_PASSPORT_FIRST)
                    yandexPasportFirstViews.setNormalVisibility()
                }
                ImageType.YANDEX_PASSPORT_REG -> {
                    imageTypes.remove(ImageType.YANDEX_PASSPORT_REG)
                    yandexPasportRegViews.setNormalVisibility()
                }
                ImageType.YANDEX_STS -> {
                    imageTypes.remove(ImageType.YANDEX_STS)
                    yandexStsViews.setNormalVisibility()
                }
                ImageType.YANDEX_LICENSE -> {
                    imageTypes.remove(ImageType.YANDEX_LICENSE)
                    yandexLicenseViews.setNormalVisibility()
                }

                ImageType.GETT_D_LICENSE -> {
                    imageTypes.remove(ImageType.GETT_D_LICENSE)
                    gettDLicenseViews.setNormalVisibility()
                }
                ImageType.GETT_PASSPORT -> {
                    imageTypes.remove(ImageType.GETT_PASSPORT)
                    gettPassportViews.setNormalVisibility()
                }
                ImageType.GETT_STS -> {
                    imageTypes.remove(ImageType.GETT_STS)
                    gettStsViews.setNormalVisibility()
                }
                ImageType.GETT_LICENSE -> {
                    imageTypes.remove(ImageType.GETT_LICENSE)
                    gettLicenseViews.setNormalVisibility()
                }
                ImageType.GETT_SELFIE -> {
                    imageTypes.remove(ImageType.GETT_SELFIE)
                    gettSelfieViews.setNormalVisibility()
                }

                ImageType.D_LICENSE_FRONT -> {
                    imageTypes.remove(ImageType.D_LICENSE_FRONT)
                    cityDLicenseFrontViews.setNormalVisibility()
                }
                ImageType.D_LICENSE_BACK -> {
                    imageTypes.remove(ImageType.D_LICENSE_BACK)
                    cityDLicenseBackViews.setNormalVisibility()
                }
                ImageType.PASSPORT_FIRST -> {
                    imageTypes.remove(ImageType.PASSPORT_FIRST)
                    cityPassportFirstViews.setNormalVisibility()
                }
                ImageType.PASSPORT_REG -> {
                    imageTypes.remove(ImageType.PASSPORT_REG)
                    cityPassportRegViews.setNormalVisibility()
                }
                ImageType.STS -> {
                    imageTypes.remove(ImageType.STS)
                    cityStsViews.setNormalVisibility()
                }
                ImageType.LICENSE_FRONT -> {
                    imageTypes.remove(ImageType.LICENSE_FRONT)
                    cityLicenseFrontViews.setNormalVisibility()
                }
                ImageType.LICENSE_BACK -> {
                    imageTypes.remove(ImageType.LICENSE_BACK)
                    cityLicenseBackViews.setNormalVisibility()
                }
                ImageType.AUTO_FRONT -> {
                    imageTypes.remove(ImageType.AUTO_FRONT)
                    cityFrontSideViews.setNormalVisibility()
                }
                ImageType.AUTO_BACK -> {
                    imageTypes.remove(ImageType.AUTO_BACK)
                    cityBackSideViews.setNormalVisibility()
                }
                ImageType.AUTO_LEFT -> {
                    imageTypes.remove(ImageType.AUTO_LEFT)
                    cityLeftSideViews.setNormalVisibility()
                }
                ImageType.AUTO_RIGHT -> {
                    imageTypes.remove(ImageType.AUTO_RIGHT)
                    cityRightSideViews.setNormalVisibility()
                }
                ImageType.SELFIE -> {
                    imageTypes.remove(ImageType.SELFIE)
                    citySelfieViews.setNormalVisibility()
                }
            }
        }
        super.onActivityResult(requestCode, resultCode, data)
    }

    private fun checkEditTextIsComplete() {
        val editTexts = listOf<EditText>(
            phone_number_edit_text,
            gett_phone_edit_text,
            id_edit_text,
            city_phone_edit_text
        )

        editTexts.map {
            it.setOnFocusChangeListener { _, _ ->
                it.setCompoundDrawablesWithIntrinsicBounds(
                    0,
                    0,
                    if (it.text?.isNotBlank() == true) R.drawable.ic_check_green else 0,
                    0
                )
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
            R.id.driver_license_cancel -> {
                imageType = ImageType.YANDEX_D_LICENSE
                yandexDLicenseViews.setNormalVisibility()
            }
            R.id.passport_first_cancel -> {
                imageType = ImageType.YANDEX_PASSPORT_FIRST
                yandexPasportFirstViews.setNormalVisibility()
            }
            R.id.passport_reg_cancel -> {
                imageType = ImageType.PASSPORT_REG
                yandexPasportRegViews.setNormalVisibility()
            }
            R.id.sts_cancel -> {
                imageType = ImageType.YANDEX_STS
                yandexStsViews.setNormalVisibility()
            }
            R.id.license_cancel -> {
                imageType = ImageType.YANDEX_LICENSE
                yandexLicenseViews.setNormalVisibility()
            }

            R.id.gett_driver_license_cancel -> {
                imageType = ImageType.GETT_D_LICENSE
                gettDLicenseViews.setNormalVisibility()
            }
            R.id.passport_first_number_cancel -> {
                imageType = ImageType.GETT_PASSPORT
                gettPassportViews.setNormalVisibility()
            }
            R.id.gett_sts_cancel -> {
                imageType = ImageType.GETT_STS
                gettStsViews.setNormalVisibility()
            }
            R.id.gett_license_cancel -> {
                imageType = ImageType.GETT_LICENSE
                gettLicenseViews.setNormalVisibility()
            }
            R.id.make_selfie_cancel -> {
                imageType = ImageType.GETT_SELFIE
                gettSelfieViews.setNormalVisibility()
            }

            R.id.city_driver_license_front_cancel -> {
                imageType = ImageType.D_LICENSE_FRONT
                cityDLicenseFrontViews.setNormalVisibility()
            }
            R.id.city_driver_license_back_cancel -> {
                imageType = ImageType.D_LICENSE_BACK
                cityDLicenseBackViews.setNormalVisibility()
            }
            R.id.city_passport_first_cancel -> {
                imageType = ImageType.PASSPORT_FIRST
                cityPassportFirstViews.setNormalVisibility()
            }
            R.id.city_passport_registration_cancel -> {
                imageType = ImageType.PASSPORT_REG
                cityPassportRegViews.setNormalVisibility()
            }
            R.id.city_sts_cancel -> {
                imageType = ImageType.STS
                cityStsViews.setNormalVisibility()
            }
            R.id.city_license_front_cancel -> {
                imageType = ImageType.LICENSE_FRONT
                cityLicenseFrontViews.setNormalVisibility()
            }
            R.id.city_license_back_cancel -> {
                imageType = ImageType.LICENSE_BACK
                cityLicenseBackViews.setNormalVisibility()
            }
            R.id.front_side_cancel -> {
                imageType = ImageType.AUTO_FRONT
                cityFrontSideViews.setNormalVisibility()
            }
            R.id.back_side_cancel -> {
                imageType = ImageType.AUTO_BACK
                cityBackSideViews.setNormalVisibility()
            }
            R.id.left_side_cancel -> {
                imageType = ImageType.AUTO_LEFT
                cityLeftSideViews.setNormalVisibility()
            }
            R.id.right_side_cancel -> {
                imageType = ImageType.AUTO_RIGHT
                cityRightSideViews.setNormalVisibility()
            }
            R.id.city_selfie_cancel -> {
                imageType = ImageType.SELFIE
                citySelfieViews.setNormalVisibility()
            }
        }

        val doc = docs.find { it.type == imageType }
        val photoForDelete = images.find { it.id == doc?.id }
        images.remove(photoForDelete)
        docs.remove(doc)

        imageTypes.remove(imageType)

        val cancels = mutableListOf<View>(
            driver_license_cancel,
            passport_first_cancel,
            passport_reg_cancel,
            sts_cancel,
            license_cancel
        )
        var counter = 0
        cancels.map {
            if (it.visibility == VISIBLE) counter++
        }
        if (counter == 0) imageTypes.clear()
    }

    private fun back() {
        findNavController(this).navigate(R.id.action_connectionFragment_to_registrationSelectionFragment)
    }
}