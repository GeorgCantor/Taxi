package taxi.kassa.util

import android.content.Context
import android.content.Context.CONNECTIVITY_SERVICE
import android.graphics.Color.TRANSPARENT
import android.graphics.Point
import android.graphics.drawable.ColorDrawable
import android.net.ConnectivityManager
import android.util.DisplayMetrics
import android.util.TypedValue
import android.view.LayoutInflater
import android.view.View
import android.view.View.*
import android.view.WindowManager
import android.view.inputmethod.InputMethodManager
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import android.widget.Toast.LENGTH_LONG
import android.widget.Toast.LENGTH_SHORT
import androidx.appcompat.app.AlertDialog
import androidx.core.content.ContextCompat.getColor
import androidx.core.text.HtmlCompat
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.LiveData
import androidx.lifecycle.Observer
import kotlinx.android.synthetic.main.dialog_one_button.*
import kotlinx.android.synthetic.main.dialog_one_button.message
import kotlinx.android.synthetic.main.dialog_one_button.title
import kotlinx.android.synthetic.main.dialog_two_buttons.*
import taxi.kassa.R
import taxi.kassa.util.Constants.MASTERCARD
import taxi.kassa.util.Constants.VISA
import taxi.kassa.util.Constants.myDateFormatSymbols
import java.text.NumberFormat
import java.text.SimpleDateFormat
import java.util.*
import kotlin.math.pow
import kotlin.math.roundToInt
import kotlin.math.sqrt

fun Context.isNetworkAvailable(): Boolean {
    val manager = getSystemService(CONNECTIVITY_SERVICE) as ConnectivityManager?

    return manager?.activeNetworkInfo?.isConnectedOrConnecting ?: false
}

fun Context.shortToast(message: String) = Toast.makeText(this, message, LENGTH_SHORT).show()

fun Context.longToast(message: String) = Toast.makeText(this, message, LENGTH_LONG).show()

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
            this.line.visibility = VISIBLE
            this.extra_message.visibility = VISIBLE
        }
    }
}

fun Context.showTwoButtonsDialog(
    title: String,
    message: String,
    cancelText: String,
    okText: String,
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
        cancel_btn.setOnClickListener { dismiss() }
        ok_btn.text = okText
        ok_btn.setOnClickListener {
            dismiss()
            function()
        }
    }
}

fun View.visible() { visibility = VISIBLE }

fun View.invisible() { visibility = INVISIBLE }

fun View.gone() { visibility = GONE }

fun View.hideKeyboard() {
    val manager = context.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
    manager.hideSoftInputFromWindow(windowToken, InputMethodManager.HIDE_NOT_ALWAYS)
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

inline fun <T> LiveData<T>.observe(
    owner: LifecycleOwner,
    crossinline observer: (T) -> Unit
) {
    this.observe(owner, Observer { it?.apply(observer) })
}

fun EditText.setNumberClickListener(button: Button, resource: Int) {
    // handle clicking on the buttons of the custom keyboard
    button.setOnClickListener {
        text?.insert(selectionStart, context.getString(resource))
    }
}

fun EditText.isEmpty(): Boolean = text.toString().isBlank()