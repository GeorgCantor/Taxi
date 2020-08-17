package taxi.kassa.util

import android.content.Context
import android.content.Context.MODE_PRIVATE
import android.content.SharedPreferences
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import taxi.kassa.model.Notification
import taxi.kassa.util.Constants.MAIN_STORAGE

class PreferenceManager(context: Context) {

    private val gson = Gson()

    private val prefs: SharedPreferences = context.getSharedPreferences(MAIN_STORAGE, MODE_PRIVATE)

    fun saveString(key: String, value: String) = prefs.edit().putString(key, value).apply()

    fun getString(key: String): String? = prefs.getString(key, "")

    fun saveInt(key: String, value: Int) = prefs.edit().putInt(key, value).apply()

    fun getInt(key: String): Int? = prefs.getInt(key, 0)

    fun saveBoolean(key: String, value: Boolean) = prefs.edit().putBoolean(key, value).apply()

    fun getBoolean(key: String): Boolean = prefs.getBoolean(key, false)

    fun saveNotifications(key: String, notifications: MutableList<Notification>) {
        val json = gson.toJson(notifications)
        prefs.edit().putString(key, json).apply()
    }

    fun getNotifications(key: String): MutableList<Notification>? {
        val type = object : TypeToken<MutableList<Notification>>() {}.type
        val json = prefs.getString(key, "")

        return gson.fromJson(json, type)
    }
}