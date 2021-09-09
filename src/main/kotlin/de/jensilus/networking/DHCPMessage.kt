package de.jensilus.networking

import de.jensilus.addresses.IPv4Address
import de.jensilus.addresses.MacAddress
import de.jensilus.components.subcomponents.NetworkInterface

data class DHCPMessage(
    val op: DHCPMessageType,
    val xid: UInt,
    val ciaddr: IPv4Address?,
    val yiaddr: IPv4Address,
    val siaddr: IPv4Address,
    val chaddr: MacAddress,
    val sname: String?,
    val options: Any?
) {

    val serverPort = 67
    val clientPort = 68



}


enum class DHCPMessageType{
    DISCOVER,
    OFFER,
    REQUEST,
    ACK,
    NAK,
    INFORM
}