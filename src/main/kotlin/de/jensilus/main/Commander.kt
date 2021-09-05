package de.jensilus.main

import com.google.common.primitives.UnsignedInteger
import de.jensilus.addresses.IPv4Address
import de.jensilus.addresses.Port
import de.jensilus.addresses.SubnetMask
import de.jensilus.components.Device
import de.jensilus.components.NetworkSwitch
import org.checkerframework.checker.signedness.qual.Unsigned

fun main(args: Array<String>) {
    val switch = NetworkSwitch()

    val d1 = Device(1)
    val d2 = Device(1)
    val d3 = Device(1)

    println(d1.networkInterfaces.first())
    println(d2.networkInterfaces.first())
    println(d3.networkInterfaces.first())

    d1.networkInterface.ipv4 = IPv4Address("192.168.0.1")
    d2.networkInterface.ipv4 = IPv4Address("192.168.0.2")
    d3.networkInterface.ipv4 = IPv4Address("192.168.0.3")

    d1.connect(switch)
    d2.connect(switch)
    d3.connect(switch)

    d1.sendPacketICMP(
        IPv4Address("192.168.0.2").getNetworkBroadcastAddress(SubnetMask("255.255.255.0")), null)
    println(d1.networkInterface.ipv4.getNetworkBroadcastAddress(SubnetMask("255.255.240.0")))

    d2.sendPacketUDP(Port(1234), IPv4Address("192.168.0.2"), Port(69), "hallo")

}
