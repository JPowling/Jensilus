package de.jensilus.main

import de.jensilus.components.Device
import de.jensilus.components.NetworkSwitch

fun main(args: Array<String>) {
    val switch = NetworkSwitch()

    val d1 = Device(1)
    val d2 = Device(1)
    val d3 = Device(1)

//    println(d1.networkInterfaces.first().macAddress)
//    println(d2.networkInterfaces.first().macAddress)
//    println(d3.networkInterfaces.first().macAddress)
//
//    println(switch.networkInterfaces[0].macAddress)
//    println(switch.networkInterfaces[1].macAddress)
//    println(switch.networkInterfaces[2].macAddress)

    d1.connect(switch)
    d2.connect(switch)
    d3.connect(switch)

    println(switch.networkInterfaces.first().isConnectedToSwitch)
}
