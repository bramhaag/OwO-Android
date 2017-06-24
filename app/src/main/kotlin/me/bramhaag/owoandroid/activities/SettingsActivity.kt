package me.bramhaag.owoandroid.activities

import android.content.Intent
import android.preference.PreferenceFragment
import android.preference.PreferenceManager
import android.support.v7.app.AppCompatActivity
import android.support.v7.app.AppCompatDelegate

class SettingsActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: android.os.Bundle?) {
        super.onCreate(savedInstanceState)

        fragmentManager.beginTransaction()
                .replace(android.R.id.content, SettingsFragment())
                .commit()

        PreferenceManager.getDefaultSharedPreferences(this).registerOnSharedPreferenceChangeListener({ prefs, key ->
            if(key == "pref_dark_theme") {
                AppCompatDelegate.setDefaultNightMode(if (prefs.getBoolean("pref_dark_theme", false)) AppCompatDelegate.MODE_NIGHT_YES else AppCompatDelegate.MODE_NIGHT_NO)
                startActivity(Intent(this, SettingsActivity::class.java))
                finish()
            }
        })
    }

    class SettingsFragment : PreferenceFragment() {

        override fun onCreate(savedInstanceState: android.os.Bundle?) {
            super.onCreate(savedInstanceState)
            addPreferencesFromResource(me.bramhaag.owoandroid.R.xml.settings)
        }
    }
}