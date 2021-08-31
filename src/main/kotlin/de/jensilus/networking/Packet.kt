package de.jensilus.networking

import de.jensilus.addresses.IPv4Address

data class Packet(val receiver: IPv4Address, val sender: IPv4Address)