package de.jensilus.addresses.applications

import de.jensilus.addresses.IPv4Address
import de.jensilus.components.Device
import de.jensilus.launch
import de.jensilus.networking.DHCPMessage
import de.jensilus.networking.DHCPMessageType
import de.jensilus.components.subcomponents.NetworkInterface

class DHCPClient (val owner: Device, val netInt: NetworkInterface){
    private val clientPort: UShort = 68u
    private val serverPort: UShort = 67u

    private var isActive = false

    fun startClient(){
        launch {
            println("starting client...")
            isActive = true
            initDiscover()
            waitForResponse()
        }
        Thread.sleep(1000)
    }

    fun initDiscover() {
        println("client is sending DISCOVER Message")

        val xid: UInt = (0U..UInt.MAX_VALUE).random()
        owner.sendPacketUDP(
            clientPort,
            IPv4Address.LOCALBROADCAST,
            serverPort,
            DHCPMessage(
                op = DHCPMessageType.DISCOVER,
                xid = xid,
                ciaddr = null,
                yiaddr = IPv4Address("0.0.0.0"),
                siaddr = IPv4Address("0.0.0.0"),
                chaddr = netInt.macAddress,
                sname = null,
                options = null
            )
        )
    }

    fun waitForResponse() {
        println("client is waiting")
    }

}