package de.jensilus.networking

import de.jensilus.addresses.IPv4Address
import de.jensilus.addresses.Port
import de.jensilus.components.subcomponents.NetworkInterface

open class PacketICMP(
    val sender: NetworkInterface,
    val destinationAddress: IPv4Address,
    val body: Any?,
) : Packet()


class PacketUDP(
    sender: NetworkInterface,
    val senderPort: Port,
    destinationAddress: IPv4Address,
    val destinationPort: Port,
    body: Any?,
) : PacketICMP(sender, destinationAddress, body)