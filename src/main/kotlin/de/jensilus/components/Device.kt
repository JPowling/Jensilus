package de.jensilus.components

import de.jensilus.addresses.IPv4Address
import de.jensilus.addresses.MacAddress
import de.jensilus.addresses.applications.DHCPClient
import de.jensilus.addresses.applications.DHCPServer
import de.jensilus.components.subcomponents.Connection
import de.jensilus.components.subcomponents.NetworkInterface
import de.jensilus.exceptions.NoConnectionException
import de.jensilus.launch
import de.jensilus.networking.*

open class Device(defaultNetworkInterfaces: Int) {

    val networkInterfaces = mutableListOf<NetworkInterface>()
    val networkInterface: NetworkInterface
        get() = networkInterfaces[0]


    private var dhcpServer: DHCPServer
    private var dhcpClient: DHCPClient

    init {
        for (i in 0 until defaultNetworkInterfaces) {
            networkInterfaces.add(NetworkInterface(this))
        }

        dhcpServer = DHCPServer(this, networkInterface)
        dhcpClient = DHCPClient(this, networkInterface)

    }

    fun startClient() {
        dhcpClient.startClient()
    }

    fun startServer() {
        dhcpServer.startServer()
    }

    fun connect(other: Device) {
        if (other == this) {
            throw NoConnectionException("Device can't be connected to itself!")
        }
        if (isConnected(other)) {
            throw NoConnectionException("Device is already connected to other Device!")
        }

        val thisNetI: NetworkInterface
        val otherNetI: NetworkInterface

        try {
            thisNetI = networkInterfaces.first { !it.isConnected }
            otherNetI = other.networkInterfaces.first { !it.isConnected }
        } catch (e: NoSuchElementException) {
            throw NoConnectionException("At least one device has no free network interfaces!")
        }

        val connection = Connection(thisNetI, otherNetI)
        thisNetI.connection = connection
        otherNetI.connection = connection

        thisNetI.onConnect()
        otherNetI.onConnect()
    }

    fun disconnect(other: Device) {
        val thisNetIs = networkInterfaces.filter { it.isConnected }
        val otherNetIs = other.networkInterfaces.filter { it.isConnected }

        val thisNetI = thisNetIs.first { it.connection!!.isPartOf(other) }
        val otherNetI = otherNetIs.first { it.connection!!.isPartOf(this) }

        thisNetI.onPreDisconnect()
        otherNetI.onPreDisconnect()

        thisNetI.connection = null
        otherNetI.connection = null

        thisNetI.onDisconnect()
        otherNetI.onDisconnect()
    }

    private fun isConnected(other: Device): Boolean {
        return networkInterfaces.firstOrNull { it.isConnected && it.connection!!.isPartOf(other) } != null
    }

    fun sendPacket(packet: Packet) {
        for (netI in networkInterfaces.filter { it.isConnected }) {
            netI.sendPacket(packet)
        }
    }

    fun sendPacketEthernet(senderMacAddress: MacAddress, destinationMacAddress: MacAddress, body: Any?) {
        for (netI in networkInterfaces.filter { it.isConnected }) {
            netI.sendPacket(PacketEthernet(senderMacAddress, destinationMacAddress, body))
        }
    }

    fun sendPacketICMP(destinationIPv4Address: IPv4Address, body: Any?) {
        for (netI in networkInterfaces.filter { it.isConnected }) {
            netI.sendPacket(PacketICMP(netI, destinationIPv4Address, body))
        }
    }

    fun sendPacketUDP(thisPort: UShort, destinationIPv4Address: IPv4Address, destinationPort: UShort, body: Any?) {
        for (netI in networkInterfaces.filter { it.isConnected }) {
            netI.sendPacket(PacketUDP(netI, thisPort, destinationIPv4Address, destinationPort, body))
        }
    }

    open fun onDataReceive(receivedOnInterface: NetworkInterface, packet: Packet) {
        println("incoming packet at ${receivedOnInterface.macAddress} $packet")

        when (packet) {

            is PacketEthernet -> {
                onPacketEthernetReceive(receivedOnInterface, packet)
            }

            //PacketUDP has to be in front of PacketICMP since every PacketUDP is a PacketICMP
            //and should still be interpreted as a PacketUDP
            is PacketUDP -> {
                onPacketUDPReceive(receivedOnInterface, packet)
            }

            is PacketICMP -> {
                onPacketICMPReceive(receivedOnInterface, packet)
            }


//            is Packet -> { onPacketReceive(receivedOnInterface, packet) }
        }
    }

    fun onPacketReceive(receivedOnInterface: NetworkInterface, packet: Packet) {

    }

    fun onPacketEthernetReceive(receivedOnInterface: NetworkInterface, packet: Packet) {

    }

    fun onPacketICMPReceive(receivedOnInterface: NetworkInterface, packet: Packet) {

    }

    fun onPacketUDPReceive(receivedOnInterface: NetworkInterface, packet: Packet) {
        when (packet.body) {
            is DHCPMessage -> {
                when (packet.body.op) {
                    DHCPMessageType.DISCOVER, DHCPMessageType.REQUEST -> {
                        if (dhcpServer.isActive) dhcpServer.receive(packet.body)
                        else println("${receivedOnInterface.macAddress}: silently discarded packet since server is offline")
                    }

                    DHCPMessageType.OFFER, DHCPMessageType.ACK, DHCPMessageType.NAK, DHCPMessageType.INFORM -> {

                    }
                }
            }
        }
    }

}
