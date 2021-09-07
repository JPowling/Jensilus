package de.jensilus.main

import de.jensilus.components.Device

object World {

    val devices = mutableListOf<Device>()

    fun createDevice(netI: Int = 1): Device {
        val device = Device(netI)
        devices += device
        return device
    }

}