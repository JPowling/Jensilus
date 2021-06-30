package de.jensilus.main

import de.jensilus.addresses.IPv4Address
import de.jensilus.addresses.SubnetMask

fun main(args: Array<String>) {
    val addr = IPv4Address("192.168.52.100")
    val subn = SubnetMask("255.255.254.0")

    println(addr.getNetworkAddressBytes(subn))

}
