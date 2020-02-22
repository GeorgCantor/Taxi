package taxi.kassa.util

import android.content.Context
import android.net.ConnectivityManager
import android.view.LayoutInflater
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import kotlinx.android.synthetic.main.dialog_accounts.*
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

fun Context.showDialog(
    title: String,
    message: String
) {
    val dialogView = LayoutInflater.from(this).inflate(R.layout.dialog_accounts, null)
    val builder = AlertDialog.Builder(this).setView(dialogView)
    val alertDialog = builder.show()

    alertDialog.title.text = title
    alertDialog.message.text = message
    alertDialog.ok_button.setOnClickListener { alertDialog.dismiss() }
}