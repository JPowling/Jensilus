package de.jensilus.main

object Settings {

    private var sleepMillis = 0L

    val version = "v0.1"

    fun sleep() {
        if (sleepMillis == 0L) {
            return
        }
        Thread.sleep(sleepMillis)
    }

}