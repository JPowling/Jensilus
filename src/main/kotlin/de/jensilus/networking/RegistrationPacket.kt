package de.jensilus.networking

import de.jensilus.components.subcomponents.NetworkInterface

data class RegistrationPacket(val sender: NetworkInterface) : Packet()