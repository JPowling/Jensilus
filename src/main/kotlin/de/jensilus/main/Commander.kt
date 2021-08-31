package de.jensilus.main

import de.jensilus.components.NetworkSwitch

fun main(args: Array<String>) {
    val switch = NetworkSwitch()

    for (`interface` in switch.networkInterfaces) {
        println(`interface`.macAddress)
    }

//    val d1 = Device(1)
//    val d2 = Device(1)
//    val d3 = Device(1)
//
//    println(d1.networkInterfaces.first().macAddress)
//    println(d2.networkInterfaces.first().macAddress)
//    println(d3.networkInterfaces.first().macAddress)
//
//    d1.connect(switch)
//    d2.connect(switch)
//    d3.connect(switch)
//
//    d1.sendPacket(Packet(IPv4Address("127.0.0.1"), IPv4Address("127.0.0.1")))

}
