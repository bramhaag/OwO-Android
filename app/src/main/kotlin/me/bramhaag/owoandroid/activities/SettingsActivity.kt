package me.bramhaag.owoandroid.activities

import android.preference.PreferenceFragment
import android.support.v7.app.AppCompatActivity

class SettingsActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: android.os.Bundle?) {
        super.onCreate(savedInstanceState)

        println(applicationInfo.theme)
        println(resources.getResourceName(applicationInfo.theme))

        fragmentManager.beginTransaction()
                .replace(android.R.id.content, SettingsFragment())
                .commit()
    }

    class SettingsFragment : PreferenceFragment() {

        override fun onCreate(savedInstanceState: android.os.Bundle?) {
            super.onCreate(savedInstanceState)
            addPreferencesFromResource(me.bramhaag.owoandroid.R.xml.settings)
        }
    }
}