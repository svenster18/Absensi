package com.mohamadrizki.absensi.ui.profil

import android.content.SharedPreferences
import android.os.Bundle
import androidx.preference.EditTextPreference
import androidx.preference.PreferenceFragmentCompat
import com.mohamadrizki.absensi.App
import com.mohamadrizki.absensi.R
import com.mohamadrizki.absensi.UserPreference
import com.mohamadrizki.absensi.data.model.LoggedInUser

class PreferenceFragment : PreferenceFragmentCompat(),
    SharedPreferences.OnSharedPreferenceChangeListener {
    private lateinit var NAME: String

    private lateinit var namePreference: EditTextPreference

    val userPreference = UserPreference(App.applicationContext())

    companion object {
        private const val DEFAULT_VALUE = "Tidak Ada"
    }

    override fun onCreatePreferences(savedInstanceState: Bundle?, rootKey: String?) {
        addPreferencesFromResource(R.xml.preferences)
        init()
        setSummaries()
    }

    private fun init() {
        NAME = resources.getString(R.string.key_name)

        namePreference = findPreference<EditTextPreference> (NAME) as EditTextPreference
    }

    private fun setSummaries() {
        namePreference.summary = userPreference.getUser().displayName
    }

    override fun onResume() {
        super.onResume()
        preferenceScreen.sharedPreferences.registerOnSharedPreferenceChangeListener(this)
    }

    override fun onPause() {
        super.onPause()
        preferenceScreen.sharedPreferences.unregisterOnSharedPreferenceChangeListener(this)
    }

    override fun onSharedPreferenceChanged(sharedPreferences: SharedPreferences, key: String) {
        if (key == NAME) {
            namePreference.summary = sharedPreferences.getString(NAME, DEFAULT_VALUE)
            val user = userPreference.getUser()
            val name = sharedPreferences.getString(NAME, DEFAULT_VALUE)
            userPreference.setUser(LoggedInUser(user.userId, name, user.isLoggedIn))
        }
    }
}