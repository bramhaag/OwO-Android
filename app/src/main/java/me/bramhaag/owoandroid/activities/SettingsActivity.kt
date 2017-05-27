package me.bramhaag.owoandroid.activities

import android.os.Bundle
import android.preference.PreferenceFragment
import android.support.v7.app.AppCompatActivity

class SettingsActivity : android.support.v7.app.AppCompatActivity() {
    override fun onCreate(savedInstanceState: android.os.Bundle?) {
        super.onCreate(savedInstanceState)

        fragmentManager.beginTransaction()
                .replace(android.R.id.content, me.bramhaag.owoandroid.activities.SettingsActivity.SettingsFragment())
                .commit()

        /*SharedPreferences.OnSharedPreferenceChangeListener({ preferences, key ->
            when(key) {
                "pref_key" -> {
                    (findViewById(android.R.id.content) as MainActivity).owo = OwO(preferences.getString(key, null))
                }
                "pref_theme" -> {
                    if(preferences.getBoolean("pref_theme", false)) {
                        activity.setTheme(android.R.style.Theme_Material)
                    } else {
                        activity.setTheme(android.R.style.Theme_Material_Light)
                    }
                }
            }
        })*/
    }

    class SettingsFragment : android.preference.PreferenceFragment() {

        override fun onCreate(savedInstanceState: android.os.Bundle?) {
            super.onCreate(savedInstanceState)
            addPreferencesFromResource(me.bramhaag.owoandroid.R.xml.settings)
        }
    }
}