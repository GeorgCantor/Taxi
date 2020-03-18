package taxi.kassa.util

import android.annotation.SuppressLint
import android.content.Context
import android.os.Handler
import android.view.View
import android.view.animation.AnimationUtils
import android.view.inputmethod.InputMethodManager
import android.widget.TextView
import taxi.kassa.R
import java.text.DateFormatSymbols
import java.text.SimpleDateFormat
import java.util.*

@SuppressLint("ResourceType")
fun showError(
    context: Context?,
    textView: TextView,
    message: String?,
    hide: Int
) {
    textView.text = message

    val animation = AnimationUtils.loadAnimation(context, R.animator.fade_in)
    animation.reset()

    textView.clearAnimation()
    textView.startAnimation(animation)

    if (hide > 0) {
        Handler().postDelayed(
            {
                val anim = AnimationUtils.loadAnimation(context, R.animator.fade_out)
                anim.reset()
                textView.clearAnimation()
                textView.startAnimation(anim)
            },
            3000
        )

        Handler().postDelayed({ textView.text = "" }, 3350)
    }
}

fun hideKeyboard(view: View) {
    val inputManager =
        view.context.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
    inputManager.hideSoftInputFromWindow(view.windowToken, InputMethodManager.HIDE_NOT_ALWAYS)
}

fun convertLongToTime(time: Long, pattern: String): String {
    val date = Date(time)
    val dateFormat = SimpleDateFormat(pattern, myDateFormatSymbols)

    return dateFormat.format(date)
}

val myDateFormatSymbols: DateFormatSymbols = object : DateFormatSymbols() {
    override fun getMonths(): Array<String> {
        return arrayOf(
            "янв", "фев", "мар", "апр", "мая", "июн",
            "июл", "авг", "сен", "окт", "нояб", "дек"
        )
    }
}