package br.com.alissontfb.multifilepicker.preferences

import android.app.Activity
import android.content.Context
import br.com.alissontfb.multifilepicker.utils.SHARED_PREF_KEY
import br.com.alissontfb.multifilepicker.utils.SP_PROFILE

class Preferences(context: Context) {

    private val sp = context.getSharedPreferences(SHARED_PREF_KEY, Activity.MODE_PRIVATE)

    fun getProfile() = sp.getString(SP_PROFILE,"")
    fun setProvider(provider: String){
        val edt = sp.edit()
        edt.putString(SP_PROFILE,provider)
        edt.apply()
    }

}