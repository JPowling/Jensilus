package de.jensilus.main

object Settings {

    var sleepMillis = 0L
        private set

    fun sleep() {
        if (sleepMillis == 0L) {
            return
        }
        Thread.sleep(sleepMillis)
    }

}