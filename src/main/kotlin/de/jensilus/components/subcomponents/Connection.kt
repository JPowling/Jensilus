package de.jensilus.components.subcomponents

import de.jensilus.components.Device

data class Connection(val c1: NetworkInterface, val c2: NetworkInterface) {

    var active = false

    fun getOtherComponent(c: NetworkInterface) = if (c == c1) c2 else c1
    fun isPartOf(d: Device) = c1.owner == d || c2.owner == d

}