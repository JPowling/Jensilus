package de.jensilus.components

import com.google.common.collect.HashBiMap
import de.jensilus.components.subcomponents.NetworkInterface
import de.jensilus.networking.Packet
import de.jensilus.networking.RegistrationPacket

class NetworkSwitch : Device(20) {

    val portToInterface = HashBiMap.create<NetworkInterface, NetworkInterface>()

    init {
//        val switchMacAddress = networkInterfaces[0].macAddress
//
//        for (i in 1 until defaultNetworkInterfaces) {
//            networkInterfaces[i].apply {
//                macAddress = switchMacAddress
//            }
//        }
    }

    override fun onPacketReceive(netInterface: NetworkInterface, packet: Packet) {
        if (packet is RegistrationPacket) {
            portToInterface[netInterface] = packet.sender
        }

        networkInterfaces.filter { it != netInterface }.forEach { it.sendPacket(packet) }
    }

}