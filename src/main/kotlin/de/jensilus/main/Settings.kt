package de.jensilus.main

object Settings {

    private var sleepMillis = 0L

    fun sleep() {
        if (sleepMillis == 0L) {
            return
        }
        Thread.sleep(sleepMillis)
    }

}