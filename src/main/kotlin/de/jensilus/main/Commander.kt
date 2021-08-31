package de.jensilus.main

import de.jensilus.addresses.IPv4Address
import de.jensilus.components.Device
import de.jensilus.components.NetworkSwitch

fun main(args: Array<String>) {
    val switch = NetworkSwitch()

    val d1 = Device(1)
    val d2 = Device(1)
    val d3 = Device(1)

    println(d1.networkInterfaces.first())
    println(d2.networkInterfaces.first())
    println(d3.networkInterfaces.first())

    d1.ipv4 = IPv4Address("192.168.0.1")
    d2.ipv4 = IPv4Address("192.168.0.2")
    d3.ipv4 = IPv4Address("192.168.0.3")

    d1.connect(switch)
    d2.connect(switch)
    d3.connect(switch)

    d1.sendPacketIP(IPv4Address("192.168.0.2"))

}
