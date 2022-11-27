package allianceone.prog.leanmanagementsystem

import allianceone.prog.leanmanagementsystem.utils.SharedPreferenceHelper
import android.app.Application

class App : Application() {

    override fun onCreate() {
        super.onCreate()
        SharedPreferenceHelper.bindContext(this)
    }
}