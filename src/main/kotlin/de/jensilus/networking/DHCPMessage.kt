package de.jensilus.networking

import de.jensilus.addresses.IPv4Address
import de.jensilus.addresses.MacAddress
import javax.crypto.Mac

data class DHCPMessage(
    val op: DHCPMessageType,
    val xid: UInt,
    val ciaddr: IPv4Address?,
    val yiaddr: IPv4Address,
    val siaddr: IPv4Address,
    val chaddr: MacAddress,
    val shaddr: MacAddress?,
    val sname: String?,
    val options: Any?
)


enum class DHCPMessageType{
    DISCOVER,
    OFFER,
    REQUEST,
    ACK,
    NAK,
    INFORM
}