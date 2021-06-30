package de.jensilus.components.subcomponents

import de.jensilus.addresses.MacAddress
import de.jensilus.components.Device
import de.jensilus.main.Settings
import de.jensilus.networking.Packet

class NetworkInterface(val owner: Device) {

    val macAddress = MacAddress.createMac(this)

    var connection: Connection? = null
    val isConnected: Boolean
        get() = connection != null


    fun sendPacket(packet: Packet): Boolean {
        if (isConnected) {
            connection!!.run {
                active = true
                Settings.sleep()
                active = false

                getOtherComponent(this@NetworkInterface).onReceivePacket(packet)
            }
            return true
        }
        return false
    }

    fun onReceivePacket(packet: Packet) {
        owner.onPacketReceive(this, packet)
    }

    fun onConnect() {

    }

    fun onDisconnect() {

    }

    override fun equals(other: Any?): Boolean {
        if (other == null || other !is NetworkInterface) {
            return false
        }
        return macAddress == other.macAddress
    }

    override fun hashCode(): Int {
        return macAddress.hashCode()
    }
}