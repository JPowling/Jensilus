package de.jensilus.components

import de.jensilus.components.subcomponents.NetworkInterface
import de.jensilus.networking.Packet

class NetworkSwitch : Device(20) {

    override fun onPacketReceive(netInterface: NetworkInterface, packet: Packet) {
        networkInterfaces.filter { it != netInterface }.forEach { it.sendPacket(packet) }
    }

}