package me.bramhaag.owoandroid.profiles

import me.bramhaag.owoandroid.api.OwO


object ProfileManager {
    private val profiles = ArrayList<Profile>()
    private var currentProfile: Profile? = null

    private val services = HashMap<Profile, OwO>()

    fun addProfile(profile: Profile) {
        profiles.add(profile.id, profile)
    }

    fun updateProfile(profile: Profile) {

    }

    fun getService(profile: Profile): OwO {
        val foundProfile = services.keys.filter { it.id == profile.id }.firstOrNull()
        if(foundProfile != null && foundProfile.lastEdited == profile.lastEdited) {
            return services[foundProfile]!!
        } else if(foundProfile != null && foundProfile.lastEdited != profile.lastEdited) {
            services[foundProfile] = OwO(profile)
            return services[foundProfile]!!
        } else {
            services.put(profile, OwO(profile))
            return services[profile]!!
        }
    }
}