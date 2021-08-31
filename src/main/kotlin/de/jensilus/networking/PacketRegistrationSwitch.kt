package de.jensilus.networking

import de.jensilus.components.NetworkSwitch

open class PacketRegistrationSwitch(val switch: NetworkSwitch) : Packet()
open class PacketDeregistrationSwitch(val switch: NetworkSwitch) : Packet()