package me.bramhaag.owoandroid.notifications

import java.util.concurrent.atomic.AtomicInteger

object NotificationId {
    @JvmStatic
    private var counter = AtomicInteger(0)

    fun getId() = counter.incrementAndGet()
}