package taxi.kassa.util

import android.content.Context
import android.net.ConnectivityManager
import android.view.LayoutInflater
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import kotlinx.android.synthetic.main.dialog_one_button.*
import kotlinx.android.synthetic.main.dialog_one_button.message
import kotlinx.android.synthetic.main.dialog_one_button.title
import kotlinx.android.synthetic.main.dialog_two_buttons.*
import taxi.kassa.R

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

fun Context.shortToast(message: CharSequence) = Toast.makeText(this, message, Toast.LENGTH_SHORT).show()

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

fun String.getStringAfterSpace(): String {
    val index = this.indexOf(' ')

    return if (index == -1) "Нет отчества" else this.substring(index + 1)
}