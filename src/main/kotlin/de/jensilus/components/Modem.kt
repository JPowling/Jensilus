package de.jensilus.components

import de.jensilus.components.subcomponents.NetworkInterface
import de.jensilus.networking.Packet

class Modem: Device(0) {

    override fun onPacketReceive(receivedOnInterface: NetworkInterface, packet: Packet) {
    }

}