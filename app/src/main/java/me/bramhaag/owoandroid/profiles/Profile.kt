package me.bramhaag.owoandroid.profiles


data class Profile(val id: Int, val lastEdited: Long, val name: String, val domain: String, val key: String, val type: Profile.Type, val endpoint: String) {

    enum class Type {
        UPLOAD,
        SHORTEN
    }
}