package taxi.kassa.util

import android.Manifest.permission.CALL_PHONE
import android.app.Activity
import android.content.ActivityNotFoundException
import android.content.Context
import android.content.Context.CONNECTIVITY_SERVICE
import android.content.Intent
import android.content.pm.PackageManager.PERMISSION_GRANTED
import android.graphics.Color.TRANSPARENT
import android.graphics.Point
import android.graphics.drawable.ColorDrawable
import android.net.ConnectivityManager
import android.net.Uri
import android.os.Build
import android.os.Handler
import android.util.DisplayMetrics
import android.util.TypedValue
import android.view.LayoutInflater
import android.view.View
import android.view.View.*
import android.view.ViewGroup
import android.view.WindowManager
import android.view.inputmethod.InputMethodManager
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast.*
import androidx.appcompat.app.AlertDialog
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.constraintlayout.widget.ConstraintSet
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat.getColor
import androidx.core.text.HtmlCompat
import androidx.fragment.app.Fragment
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.LiveData
import androidx.lifecycle.Observer
import com.google.android.material.textfield.TextInputLayout
import com.google.android.material.transition.platform.MaterialArcMotion
import com.google.android.material.transition.platform.MaterialContainerTransform
import com.redmadrobot.inputmask.MaskedTextChangedListener
import kotlinx.android.synthetic.main.dialog_one_button.*
import kotlinx.android.synthetic.main.dialog_one_button.message
import kotlinx.android.synthetic.main.dialog_one_button.title
import kotlinx.android.synthetic.main.dialog_two_buttons.*
import taxi.kassa.R
import taxi.kassa.util.Constants.CAR_BACK
import taxi.kassa.util.Constants.CAR_FRONT
import taxi.kassa.util.Constants.CAR_LEFT
import taxi.kassa.util.Constants.CAR_RIGHT
import taxi.kassa.util.Constants.CITYMOBIL
import taxi.kassa.util.Constants.CITY_REQUEST
import taxi.kassa.util.Constants.DRIVER_LICENCE_BACK
import taxi.kassa.util.Constants.DRIVER_LICENCE_FRONT
import taxi.kassa.util.Constants.GETT
import taxi.kassa.util.Constants.GETT_REQUEST
import taxi.kassa.util.Constants.LICENCE_BACK
import taxi.kassa.util.Constants.LICENCE_FRONT
import taxi.kassa.util.Constants.MASTERCARD
import taxi.kassa.util.Constants.PASSPORT_FIRST
import taxi.kassa.util.Constants.PASSPORT_REGISTRATION
import taxi.kassa.util.Constants.PHONE_MASK
import taxi.kassa.util.Constants.SELFIE
import taxi.kassa.util.Constants.STS_BACK
import taxi.kassa.util.Constants.STS_FRONT
import taxi.kassa.util.Constants.SUPPORT_PHONE_NUMBER
import taxi.kassa.util.Constants.VISA
import taxi.kassa.util.Constants.YANDEX
import taxi.kassa.util.Constants.YANDEX_REQUEST
import taxi.kassa.util.Constants.myDateFormatSymbols
import java.text.NumberFormat
import java.text.SimpleDateFormat
import java.util.*
import java.util.concurrent.TimeUnit
import kotlin.math.pow
import kotlin.math.roundToInt
import kotlin.math.sqrt

fun Context.isNetworkAvailable() = (getSystemService(CONNECTIVITY_SERVICE) as ConnectivityManager?)
    ?.activeNetworkInfo?.isConnectedOrConnecting ?: false

fun Context.shortToast(message: String) = makeText(this, message, LENGTH_SHORT).show()

fun Context.longToast(message: String) = makeText(this, message, LENGTH_LONG).show()

fun Context.showOneButtonDialog(
    title: String,
    message: String,
    isRegistrationDialog: Boolean
) {
    val dialogView = LayoutInflater.from(this).inflate(R.layout.dialog_one_button, null)
    val builder = AlertDialog.Builder(this).setView(dialogView)
    val alertDialog = builder.show()

    with(alertDialog) {
        window?.setBackgroundDrawable(ColorDrawable(TRANSPARENT))
        this.title.text = title
        this.message.text = message
        ok_button.setOnClickListener { dismiss() }

        if (isRegistrationDialog) {
            this.message.setMultiColoredText(R.string.terms_description)
            this.message.setTextSize(TypedValue.COMPLEX_UNIT_SP, 14F)
            this.line.visible()
            this.extra_message.visible()
        }
    }
}

fun Context.showTwoButtonsDialog(
    title: String,
    message: String,
    cancelText: String,
    okText: String,
    transitionFunction: (View, ConstraintLayout) -> (Unit),
    function: () -> (Unit)
) {
    val dialogView = LayoutInflater.from(this).inflate(R.layout.dialog_two_buttons, null)
    val builder = AlertDialog.Builder(this).setView(dialogView)
    val alertDialog = builder.show()

    with(alertDialog) {
        window?.setBackgroundDrawable(ColorDrawable(TRANSPARENT))
        this.title.text = title
        this.message.text = message
        cancel_btn.text = cancelText
        cancel_btn.setOnClickListener {
            transitionFunction(dialogView, two_dialog_root_layout)
            550L.runDelayed { dismiss() }
        }
        ok_btn.text = okText
        ok_btn.setOnClickListener {
            dismiss()
            function()
        }
        setOnDismissListener {
            transitionFunction(dialogView, two_dialog_root_layout)
        }
    }
}

fun Context.getScreenSize(): Double {
    val point = Point()
    (getSystemService(Context.WINDOW_SERVICE) as WindowManager).defaultDisplay.getRealSize(point)
    val displayMetrics: DisplayMetrics = resources.displayMetrics
    val width: Int = point.x
    val height: Int = point.y
    val wi = width.toDouble() / displayMetrics.xdpi.toDouble()
    val hi = height.toDouble() / displayMetrics.ydpi.toDouble()
    val x = wi.pow(2.0)
    val y = hi.pow(2.0)

    return ((sqrt(x + y) * 10.0).roundToInt() / 10.0)
}

fun Context.getScreenWidth(): Float {
    val displayMetrics: DisplayMetrics = resources.displayMetrics

    return displayMetrics.widthPixels / displayMetrics.density
}

fun ViewGroup.inflate(layoutRes: Int): View = LayoutInflater.from(context).inflate(layoutRes, this, false)

fun View.visible() { visibility = VISIBLE }

fun View.invisible() { visibility = INVISIBLE }

fun View.gone() { visibility = GONE }

fun View.hideKeyboard() {
    val manager = context.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
    manager.hideSoftInputFromWindow(windowToken, InputMethodManager.HIDE_NOT_ALWAYS)
}

fun View.getTransform(mEndView: View) = MaterialContainerTransform().apply {
    startView = this@getTransform
    endView = mEndView
    addTarget(mEndView)
    pathMotion = MaterialArcMotion()
    duration = 550
    scrimColor = TRANSPARENT
}

fun TextView.setColor(
    balance: String,
    colorPositive: Int,
    colorNegative: Int
) = setTextColor(getColor(context, if (balance.toFloat() > 0.0F) colorPositive else colorNegative))

fun TextView.setMultiColoredText(resource: Int) = setText(
    HtmlCompat.fromHtml(context.getString(resource), HtmlCompat.FROM_HTML_MODE_LEGACY),
    TextView.BufferType.SPANNABLE
)

fun TextView.setFormattedText(
    formatResource: Int,
    value: Double
) {
    val format = NumberFormat.getInstance(Locale("ru", "RU"))
    text = context.getString(
        formatResource,
        format.format(value)
    ).replace(',', '.')
}

fun String.getCardType(): String {
    val visa = Regex("^4[0-9]{12}(?:[0-9]{3})?$")
    val mastercard = Regex("^5[1-5][0-9]{14}$")

    return when {
        visa.matches(this) -> VISA
        mastercard.matches(this) -> MASTERCARD
        else -> "Unknown"
    }
}

fun String.getTaxiId() = when (this) {
    YANDEX -> YANDEX_REQUEST
    GETT -> GETT_REQUEST
    CITYMOBIL -> CITY_REQUEST
    else -> 0
}

fun Array<View>.setNormalVisibility() {
    this[0].visible()
    this[1].invisible()
    this[2].visible()
    this[3].invisible()
    this[4].invisible()
}

fun Array<View>.setLoadPhotoVisibility() {
    this[0].invisible()
    this[1].visible()
    this[2].invisible()
    this[3].visible()
    this[4].visible()
}

fun Long.convertToTime(pattern: String): String {
    val date = Date(this)
    val dateFormat = SimpleDateFormat(pattern, myDateFormatSymbols)

    return dateFormat.format(date)
}

fun Long.runDelayed(action: () -> Unit) {
    Handler().postDelayed(action, TimeUnit.MILLISECONDS.toMillis(this))
}

fun Int.getPhotoType() = when (this) {
    1, 6, 11 -> DRIVER_LICENCE_FRONT
    12 -> DRIVER_LICENCE_BACK
    2, 7, 13 -> PASSPORT_FIRST
    3, 14 -> PASSPORT_REGISTRATION
    4, 8, 15 -> STS_FRONT
    16 -> STS_BACK
    5, 9, 17 -> LICENCE_FRONT
    18 -> LICENCE_BACK
    10, 23 -> SELFIE
    19 -> CAR_FRONT
    20 -> CAR_BACK
    21 -> CAR_LEFT
    22 -> CAR_RIGHT
    else -> this
}

inline fun <T> LiveData<T>.observe(
    owner: LifecycleOwner,
    crossinline observer: (T) -> Unit
) {
    this.observe(owner, Observer { it?.apply(observer) })
}

fun EditText.setMaskListener(input: TextInputLayout?) {
    class PhoneMaskListener : MaskedTextChangedListener(PHONE_MASK, this@setMaskListener, object : ValueListener {
        override fun onTextChanged(maskFilled: Boolean, extractedValue: String, formattedValue: String) {
            input?.error = null
        }
    })
    addTextChangedListener(PhoneMaskListener())
}

fun EditText.setKeyboard(buttons: Array<View>, function: () -> Unit) {
    showSoftInputOnFocus = false

    listOf(
        Pair(buttons[0], R.string.num0),
        Pair(buttons[1], R.string.num1),
        Pair(buttons[2], R.string.num2),
        Pair(buttons[3], R.string.num3),
        Pair(buttons[4], R.string.num4),
        Pair(buttons[5], R.string.num5),
        Pair(buttons[6], R.string.num6),
        Pair(buttons[7], R.string.num7),
        Pair(buttons[8], R.string.num8),
        Pair(buttons[9], R.string.num9)
    ).map {
        setNumberClickListener(it.first, it.second)
    }

    buttons[10].setOnClickListener {
        val cursorPosition = selectionStart
        if (cursorPosition > 0) {
            text = text?.delete(cursorPosition - 1, cursorPosition)
            setSelection(cursorPosition - 1)
        }
    }

    buttons[11].setOnClickListener { function() }
}

fun EditText.setNumberClickListener(button: View, resource: Int) {
    // handle clicking on the buttons of the custom keyboard
    button.setOnClickListener {
        text?.insert(selectionStart, context.getString(resource))
    }
}

fun EditText.isEmpty(): Boolean = value.isBlank()

val EditText.value
    get() = text.toString()

fun Activity.makeCall(fragment: Fragment) {
    val callIntent = Intent(Intent.ACTION_CALL)
    callIntent.data = Uri.parse("tel:${SUPPORT_PHONE_NUMBER}")

    if (ActivityCompat.checkSelfPermission(this, CALL_PHONE) != PERMISSION_GRANTED && Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
        fragment.requestPermissions(arrayOf(CALL_PHONE), 10)
        return
    } else {
        try {
            startActivity(callIntent)
        } catch (ex: ActivityNotFoundException) {
            shortToast(getString(R.string.not_find_call_app))
        }
    }
}

fun changeConstraint(
    rootId: ConstraintLayout,
    startId: Int,
    startSide: Int,
    endId: Int,
    endSide: Int,
    margin: Int
) {
    val constraintSet = ConstraintSet()
    with(constraintSet) {
        clone(rootId)
        connect(startId, startSide, endId, endSide, margin)
        applyTo(rootId)
    }
}

fun removeConstraint(
    rootId: ConstraintLayout,
    id: Int,
    side: Int
) {
    val constraintSet = ConstraintSet()
    with(constraintSet) {
        clone(rootId)
        clear(id, side)
        applyTo(rootId)
    }
}