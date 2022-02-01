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
    private lateinit var USERNAME: String
    private lateinit var NIP: String
    private lateinit var NO_TELP: String
    private lateinit var JABATAN: String

    private lateinit var namePreference: EditTextPreference
    private lateinit var usernamePreference: EditTextPreference
    private lateinit var nipPreference: EditTextPreference
    private lateinit var noTelpPreference: EditTextPreference
    private lateinit var jabatanPreference: EditTextPreference

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
        NIP = resources.getString(R.string.key_nip)
        NAME = resources.getString(R.string.key_name)
        JABATAN = resources.getString(R.string.key_jabatan)
        NO_TELP = resources.getString(R.string.key_no_telp)
        USERNAME = resources.getString(R.string.key_username)

        nipPreference = findPreference<EditTextPreference> (NIP) as EditTextPreference
        namePreference = findPreference<EditTextPreference> (NAME) as EditTextPreference
        jabatanPreference = findPreference<EditTextPreference> (JABATAN) as EditTextPreference
        noTelpPreference = findPreference<EditTextPreference> (NO_TELP) as EditTextPreference
        usernamePreference = findPreference<EditTextPreference> (USERNAME) as EditTextPreference
    }

    private fun setSummaries() {
        nipPreference.summary = userPreference.getUser().nip
        namePreference.summary = userPreference.getUser().displayName
        jabatanPreference.summary = userPreference.getUser().jabatan
        noTelpPreference.summary = userPreference.getUser().no_telp
        usernamePreference.summary = userPreference.getUser().username

    }

    override fun onResume() {
        super.onResume()
        preferenceScreen.sharedPreferences?.registerOnSharedPreferenceChangeListener(this)
    }

    override fun onPause() {
        super.onPause()
        preferenceScreen.sharedPreferences?.unregisterOnSharedPreferenceChangeListener(this)
    }

    override fun onSharedPreferenceChanged(sharedPreferences: SharedPreferences, key: String) {
        if (key == NO_TELP) {
            noTelpPreference.summary = sharedPreferences.getString(NO_TELP, DEFAULT_VALUE)
            val user = userPreference.getUser()
            val noTelp = sharedPreferences.getString(NO_TELP, DEFAULT_VALUE)
            userPreference.setUser(LoggedInUser(user.nip, user.username, user.displayName, noTelp, user.jabatan, user.isLoggedIn))
        }
    }
}