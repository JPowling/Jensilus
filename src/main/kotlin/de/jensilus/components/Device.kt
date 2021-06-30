package de.jensilus.components

import de.jensilus.components.subcomponents.Connection
import de.jensilus.components.subcomponents.NetworkInterface
import de.jensilus.exceptions.NoConnectionException
import de.jensilus.networking.Packet

open class Device(defaultNetworkInterfaces: Int) {

    val networkInterfaces = mutableListOf<NetworkInterface>()

    init {
        for (i in 0 until defaultNetworkInterfaces) {
            networkInterfaces.add(NetworkInterface(this))
        }
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

        thisNetI.connection = null
        otherNetI.connection = null

        thisNetI.onDisconnect()
        otherNetI.onDisconnect()
    }

    private fun isConnected(other: Device): Boolean {
        return networkInterfaces.firstOrNull { it.isConnected && it.connection!!.isPartOf(other) } != null
    }

    fun sendPacket(packet: Packet) {
        for (netI in networkInterfaces) {
            netI.sendPacket(packet)
        }
    }

    open fun onPacketReceive(netInterface: NetworkInterface, packet: Packet) {
        println("Packet from " + netInterface.macAddress.toString())
    }

}
