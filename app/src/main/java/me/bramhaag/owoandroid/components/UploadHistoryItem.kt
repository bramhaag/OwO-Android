package me.bramhaag.owoandroid.components

import java.io.Serializable
import java.net.URL
import java.util.*

data class UploadHistoryItem(val name: String, val url: URL?, val date: Date) : Serializable