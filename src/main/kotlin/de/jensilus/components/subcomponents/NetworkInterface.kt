package de.jensilus.components.subcomponents

import de.jensilus.addresses.IPv4Address
import de.jensilus.addresses.MacAddress
import de.jensilus.addresses.SubnetMask
import de.jensilus.components.Device
import de.jensilus.components.NetworkSwitch
import de.jensilus.launch
import de.jensilus.main.Settings
import de.jensilus.networking.Packet
import de.jensilus.networking.PacketDeregistrationSwitch
import de.jensilus.networking.PacketRegistrationDevice
import de.jensilus.networking.PacketRegistrationSwitch

class NetworkInterface(
    val owner: Device,
    var ipv4: IPv4Address = IPv4Address("0.0.0.0"),
    var subnetMask: SubnetMask = SubnetMask("255.255.255.255"),
) {


    var macAddress = MacAddress.createMac(this)

    var connection: Connection? = null
    val isConnected: Boolean
        get() = connection != null

    val isConnectedToSwitch: Boolean
        get() {
            connection?.let {
                return it.getOtherComponent(this).owner is NetworkSwitch
            }
            return false
        }
    val isConnectedToDevice = isConnected && !isConnectedToSwitch


    fun sendPacket(packet: Packet): Boolean {
        var succes = false
        launch {
            connection?.run {
                active = true
                Settings.sleep()
                active = false

                getOtherComponent(this@NetworkInterface).onReceivePacket(packet)
                succes = true
            }
        }
        return succes
    }

    fun onReceivePacket(packet: Packet) {
        owner.onDataReceive(this, packet)
    }

    fun onConnect() {
        if (owner !is NetworkSwitch && isConnectedToSwitch) {
            sendPacket(PacketRegistrationDevice(this))
        } else if (owner is NetworkSwitch && isConnectedToSwitch) {
            sendPacket(PacketRegistrationSwitch(owner))
        }
    }

    fun onPreDisconnect() {
        if (owner is NetworkSwitch && isConnectedToSwitch) {
            sendPacket(PacketDeregistrationSwitch(owner))
        }
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

    override fun toString(): String {
        return macAddress.toString()
    }
}