package de.jensilus.components

import com.google.common.collect.HashBiMap
import de.jensilus.addresses.IPv4Address
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

    override fun onPacketReceive(receivedOnInterface: NetworkInterface, packet: Packet) {
        run whenCheck@{
            when (packet) {
                is PacketRegistrationDevice -> portToInterface[receivedOnInterface] = packet.toRegister

                is PacketRegistrationSwitch -> connectedSwitches[receivedOnInterface] = packet.switch
                is PacketDeregistrationSwitch -> connectedSwitches.remove(receivedOnInterface)

                is PacketIP -> {
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
            keys.filter { it.owner.ipv4 == ipv4 }
                .map { get(it) }
                .randomOrNull() // To simulate chaos when IPv4 Addresses are duplicated
        }
    }

}