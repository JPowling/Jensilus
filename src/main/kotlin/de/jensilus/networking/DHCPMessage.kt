package de.jensilus.networking

import de.jensilus.addresses.IPv4Address
import de.jensilus.components.subcomponents.NetworkInterface

class DHCPServerMessage(
    val ownIPv4Address: IPv4Address,
    val destIPv4Address: IPv4Address,
) {

    val serverPort = 67
    val clientPort = 68

}

class DHCPClientMessage(

) {

}
