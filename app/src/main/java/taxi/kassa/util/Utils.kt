package taxi.kassa.util

import android.content.Context
import android.os.Handler
import android.view.animation.AnimationUtils
import android.widget.ProgressBar
import android.widget.TextView
import taxi.kassa.R

fun showError(
    context: Context?,
    textView: TextView,
    message: String?,
    hide: Int,
    gone: Int
) {
    if (gone == 1) textView.visibility = ProgressBar.VISIBLE
    textView.text = message
    val animation =
        AnimationUtils.loadAnimation(context, R.animator.fade_in)
    animation.reset()
    textView.clearAnimation()
    textView.startAnimation(animation)
    if (hide > 0) {
        Handler().postDelayed(
            {
                val a = AnimationUtils.loadAnimation(
                    context,
                    R.animator.fade_out
                )
                a.reset()
                textView.clearAnimation()
                textView.startAnimation(a)
            },
            3000
        )
        Handler().postDelayed(
            {
                textView.text = ""
                if (gone == 1) textView.visibility = ProgressBar.GONE
            },
            3350
        )
    }
}