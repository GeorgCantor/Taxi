package taxi.kassa.util

import android.annotation.SuppressLint
import android.content.Context
import android.graphics.Point
import android.net.ConnectivityManager
import android.os.Handler
import android.util.DisplayMetrics
import android.view.LayoutInflater
import android.view.View
import android.view.View.*
import android.view.WindowManager
import android.view.animation.AnimationUtils
import android.view.inputmethod.InputMethodManager
import android.widget.TextView
import android.widget.Toast
import android.widget.Toast.LENGTH_LONG
import android.widget.Toast.LENGTH_SHORT
import androidx.appcompat.app.AlertDialog
import androidx.core.content.ContextCompat.getColor
import kotlinx.android.synthetic.main.dialog_one_button.*
import kotlinx.android.synthetic.main.dialog_one_button.message
import kotlinx.android.synthetic.main.dialog_one_button.title
import kotlinx.android.synthetic.main.dialog_two_buttons.*
import taxi.kassa.R
import kotlin.math.pow
import kotlin.math.roundToInt
import kotlin.math.sqrt

fun Context.isNetworkAvailable(): Boolean {
    val manager = this.getSystemService(Context.CONNECTIVITY_SERVICE) as? ConnectivityManager

    manager?.let {
        val networkInfo = it.activeNetworkInfo
        networkInfo?.let { info ->
            if (info.isConnected) return true
        }
    }

    return false
}

fun Context.shortToast(message: String) = Toast.makeText(this, message, LENGTH_SHORT).show()

fun Context.longToast(message: String) = Toast.makeText(this, message, LENGTH_LONG).show()

fun Context.showOneButtonDialog(
    title: String,
    message: String
) {
    val dialogView = LayoutInflater.from(this).inflate(R.layout.dialog_one_button, null)
    val builder = AlertDialog.Builder(this).setView(dialogView)
    val alertDialog = builder.show()

    alertDialog.title.text = title
    alertDialog.message.text = message
    alertDialog.ok_button.setOnClickListener { alertDialog.dismiss() }
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

    alertDialog.title.text = title
    alertDialog.message.text = message
    alertDialog.cancel_btn.text = cancelText
    alertDialog.cancel_btn.setOnClickListener { alertDialog.dismiss() }
    alertDialog.ok_btn.text = okText
    alertDialog.ok_btn.setOnClickListener {
        alertDialog.dismiss()
        function()
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
) = setTextColor(getColor(this.context, if (balance.toFloat() > 0.0F) colorPositive else colorNegative))

@SuppressLint("ResourceType")
fun TextView.showError(message: String) {
    text = message

    val animation = AnimationUtils.loadAnimation(context, R.animator.fade_in)
    animation.reset()

    clearAnimation()
    startAnimation(animation)

    Handler().postDelayed({
        val anim = AnimationUtils.loadAnimation(context, R.animator.fade_out)
        anim.reset()
        clearAnimation()
        startAnimation(anim)
    }, 3000)

    Handler().postDelayed({ text = "" }, 3350)
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

fun String.getStringAfterSpace(): String {
    val index = this.indexOf(' ')

    return if (index == -1) "Нет отчества" else this.substring(index + 1)
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