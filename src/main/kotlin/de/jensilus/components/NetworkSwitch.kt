package de.jensilus.components

import com.google.common.collect.HashBiMap
import de.jensilus.addresses.IPv4Address
import de.jensilus.addresses.MacAddress
import de.jensilus.components.subcomponents.NetworkInterface
import de.jensilus.networking.*

class NetworkSwitch : Device(20) {

    private val portToInterface: HashBiMap<NetworkInterface, NetworkInterface> = HashBiMap.create()
    private val connectedSwitches: HashBiMap<NetworkInterface, NetworkSwitch> = HashBiMap.create()

    init {
//        val switchMacAddress = networkInterfaces[0].macAddress
//
//        for (i in 1 until defaultNetworkInterfaces) {
//            networkInterfaces[i].apply {
//                macAddress = switchMacAddress
//            }
//        }
    }

    override fun onDataReceive(receivedOnInterface: NetworkInterface, packet: Packet) {
        run whenCheck@{
            when (packet) {
                is PacketRegistrationDevice -> portToInterface[receivedOnInterface] = packet.toRegister

                is PacketRegistrationSwitch -> connectedSwitches[receivedOnInterface] = packet.switch
                is PacketDeregistrationSwitch -> connectedSwitches.remove(receivedOnInterface)

                is PacketEthernet -> {
                    getInterfaceToSendFor(packet.destMacAddress)?.run {
                        sendPacket(packet);return@whenCheck
                    }
                }

                is PacketICMP -> {

                    if (packet.destinationAddress == packet.sender.ipv4.getNetworkBroadcastAddress(packet.sender.subnetMask)) {
                        networkInterfaces.forEach { it.sendPacket(packet) }
                    }

                    if (packet.destinationAddress == IPv4Address.LOCALBROADCAST) {
                        networkInterfaces.forEach { it.sendPacket(packet) }
                    }

                    // Send over matching NetworkInterface OR to all other connected switches
                    getInterfaceToSendOnFor(packet.destinationAddress)?.run {
                        sendPacket(packet); return@whenCheck
                    }
                    connectedSwitches.keys.forEach { it.sendPacket(packet) }
                }
            }
        }

//        networkInterfaces.filter { it != receivedOnInterface }.forEach { it.sendPacket(packet) }
    }

    private fun getInterfaceToSendOnFor(ipv4: IPv4Address): NetworkInterface? {
        return portToInterface.inverse().run {
            keys.filter { it.ipv4 == ipv4 }
                .map { get(it) }
                .randomOrNull() // To simulate chaos when IPv4 Addresses are duplicated
        }
    }

    private fun getInterfaceToSendFor(mac: MacAddress): NetworkInterface? {
        return portToInterface.inverse().run {
            keys.filter { it.macAddress == mac }
                .map { get(it) }
                .randomOrNull() // To simulate chaos when IPv4 Addresses are duplicated
        }
    }

}