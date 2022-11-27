package allianceone.prog.leanmanagementsystem.utils

import allianceone.prog.leanmanagementsystem.data.model.DriverConfigData
import android.app.Application
import android.content.Context
import com.google.gson.Gson

object SharedPreferenceHelper {
    private lateinit var application: Application
    private const val FILE_NAME = "staff_id"
    fun bindContext(app: Application) {
        application = app
    }

    fun writeStaffId(idStaff: String) {
        val pref = application.getSharedPreferences(FILE_NAME, Context.MODE_PRIVATE) ?: return
        pref.edit().putString("idStaff", idStaff).apply()
    }

    fun readStaffId(): String? {
        val pref = application.getSharedPreferences(FILE_NAME, Context.MODE_PRIVATE) ?: return null
        return pref.getString("idStaff", null)
    }

    fun writeConfigConnection(driverConfigData: DriverConfigData) {
        val pref = application.getSharedPreferences(FILE_NAME, Context.MODE_PRIVATE) ?: return
        val gson = Gson()
        val data = gson.toJson(driverConfigData)
        pref.edit().putString("dataConfig", data).apply()
    }

    fun readConfigConnection(): DriverConfigData? {
        val pref = application.getSharedPreferences(FILE_NAME, Context.MODE_PRIVATE)
        val connectionData = pref.getString("dataConfig", null)
        if (connectionData != null) {
            val gson = Gson()
            return gson.fromJson(connectionData, DriverConfigData::class.java)
        }
        return null
    }
}