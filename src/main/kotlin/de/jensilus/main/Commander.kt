package de.jensilus.main

import de.jensilus.addresses.IPv4Address
import de.jensilus.addresses.SubnetMask
import de.jensilus.components.Device
import de.jensilus.components.NetworkSwitch

fun main(args: Array<String>) {
    val switch = NetworkSwitch()

    val d1 = Device(1)
    val d2 = Device(1)
    val d3 = Device(1)
    val d4 = Device(1)
    val d5 = Device(1)



    println(d1.networkInterfaces.first())
    println(d2.networkInterfaces.first())
//    println(d3.networkInterfaces.first())


    d1.connect(switch)
    d2.connect(switch)
    d3.connect(switch)
//    d4.connect(switch)
//    d5.connect(switch)

    d1.startServer()
    d2.startServer()


    d3.startClient()
//    d5.startClient()

//    Thread.sleep(2000)
//    println()
//    println("---------------")
//    println()
//    d4.startClient()

    Thread.sleep(5000)

    println(d3.networkInterface.ipv4)
    println(d4.networkInterface.ipv4)
    println(d5.networkInterface.ipv4)

    Thread.sleep(20000)

}
