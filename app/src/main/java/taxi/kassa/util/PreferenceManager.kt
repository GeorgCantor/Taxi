package taxi.kassa.util

import android.content.Context
import android.content.SharedPreferences
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import taxi.kassa.model.Notification
import taxi.kassa.util.Constants.MAIN_STORAGE

class PreferenceManager(context: Context) {

    private val gson = Gson()

    private val prefs: SharedPreferences = context.getSharedPreferences(MAIN_STORAGE, Context.MODE_PRIVATE)

    fun saveString(key: String, value: String) = prefs.edit().putString(key, value).apply()

    fun getString(key: String): String? = prefs.getString(key, "")

    fun saveNotifications(key: String, notifications: ArrayList<Notification>) {
        val json = gson.toJson(notifications)
        prefs.edit().putString(key, json).apply()
    }

    fun getNotifications(key: String): ArrayList<Notification>? {
        val type = object : TypeToken<ArrayList<Notification>>() {}.type
        val json = prefs.getString(key, "")

        return gson.fromJson(json, type)
    }
}