package me.bramhaag.owoandroid.components

import java.io.Serializable
import java.net.URL
import java.util.*

data class ShortenHistoryItem(val originalUrl: URL, val shortenedUrl: URL,  val date: Date): Serializable