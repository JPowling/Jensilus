package de.jensilus.applications

import de.jensilus.addresses.IPv4Address
import de.jensilus.components.Device
import de.jensilus.launch
import de.jensilus.networking.DHCPMessage
import de.jensilus.networking.DHCPMessageType
import de.jensilus.components.subcomponents.NetworkInterface
import de.jensilus.main.Settings

class DHCPClient(val owner: Device, val netInt: NetworkInterface) {
    private val clientPort: UShort = 68u
    private val serverPort: UShort = 67u

    var isActive = false

    var offerList = mutableListOf<DHCPMessage>()

    fun startClient() {
        launch {
            println("starting client...")
            isActive = true
            initDiscover()
        }
    }

    fun receive(message: DHCPMessage) {
        when (message.op) {
            DHCPMessageType.OFFER -> {
                offer(message)
            }
            DHCPMessageType.ACK -> ack(message)
            else -> println("unexpected Message")
        }
    }

    private fun initDiscover() {
        println("client is sending DHCPDISCOVER Message")

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
                shaddr = null,
                sname = null,
                options = null
            )
        )
        waitAndRequest()
    }

    private fun waitAndRequest() {
        Thread.sleep(1500L + 15 * Settings.sleepMillis)

        println("client received ${offerList.size} offer*s")
        if (offerList.isEmpty()) {
            println("No DHCP-Server responded...")
            println("shutting down client")
            isActive = false
            return
        }

        //TODO select best OFFER
        val offer = offerList.first()

        println("client is sending DHCPREQEUST")
        owner.sendPacketUDP(
            clientPort,
            IPv4Address.LOCALBROADCAST,
            serverPort,
            DHCPMessage(
                op = DHCPMessageType.REQUEST,
                xid = offer.xid,
                ciaddr = null,
                yiaddr = IPv4Address("0.0.0.0"),
                siaddr = IPv4Address("0.0.0.0"),
                chaddr = netInt.macAddress,
                shaddr = offer.shaddr,
                sname = null,
                options = null
            )
        )
    }

    fun offer(message: DHCPMessage) {
        println("client received DHCPOFFER")
        offerList.add(message)
    }

    private fun ack(message: DHCPMessage) {
//        println("client received DHCPACK from server")
        netInt.ipv4 = message.yiaddr
        println("new IPv4 address is ${netInt.ipv4}, DHCP-Server: ${message.sname}")

    }


    fun waitForResponse() {
        println("client is waiting")
    }

}