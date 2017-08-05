package me.bramhaag.owoandroid.upload

import me.bramhaag.owoandroid.api.ProgressRequestBody
import me.bramhaag.owoandroid.api.UploadObject
import me.bramhaag.owoandroid.profiles.Profile
import me.bramhaag.owoandroid.profiles.ProfileManager


class UploadManager {

    val uploadItems = HashMap<UploadObject, ProgressRequestBody.ProgressConsumer>()

    fun upload(profile: Profile, uploadObject: UploadObject) {
        val owo = ProfileManager.getService(profile)

        //val requestFile = ProgressRequestBody(uploadObject, )
        //owo.service.upload()
    }

}