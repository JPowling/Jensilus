package de.jensilus.networking

import de.jensilus.addresses.IPv4Address
import de.jensilus.components.subcomponents.NetworkInterface

data class PacketIP(val sender: NetworkInterface, var destinationAddress: IPv4Address) : Packet()