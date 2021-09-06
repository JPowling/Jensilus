package de.jensilus.networking

import de.jensilus.addresses.IPv4Address
import de.jensilus.components.subcomponents.NetworkInterface

open class PacketICMP(
    val sender: NetworkInterface,
    val destinationAddress: IPv4Address,
    body: Any?,
) : Packet(body)


class PacketUDP(
    sender: NetworkInterface,
    val senderPort: UShort,
    destinationAddress: IPv4Address,
    val destinationPort: UShort,
    body: Any?,
) : PacketICMP(sender, destinationAddress, body)