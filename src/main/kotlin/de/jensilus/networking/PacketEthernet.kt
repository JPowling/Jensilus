package de.jensilus.networking

import de.jensilus.addresses.MacAddress

class PacketEthernet(val senderMacAddress: MacAddress, val destMacAddress: MacAddress, body: Any?) : Packet(body) {
    override fun toString(): String {
        return "senderMacAdd=$senderMacAddress, destMacAdd=$destMacAddress, body=$body"
    }
}