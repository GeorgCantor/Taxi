package taxi.kassa.util

import android.app.Activity
import android.content.Context
import android.content.SharedPreferences
import taxi.kassa.util.Constants.MAIN_STORAGE

class PreferenceManager(activity: Activity) {

    private val prefs: SharedPreferences = activity.getSharedPreferences(MAIN_STORAGE, Context.MODE_PRIVATE)

    fun saveString(key: String, value: String) = prefs.edit().putString(key, value).apply()

    fun getString(key: String): String? = prefs.getString(key, "")
}