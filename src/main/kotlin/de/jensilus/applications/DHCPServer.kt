package de.jensilus.applications

import de.jensilus.addresses.IPv4Address
import de.jensilus.addresses.MacAddress
import de.jensilus.components.Device
import de.jensilus.components.subcomponents.NetworkInterface
import de.jensilus.launch
import de.jensilus.networking.DHCPMessage
import de.jensilus.networking.DHCPMessageType

class DHCPServer(val owner: Device, val networkInterface: NetworkInterface) {
    private val clientPort: UShort = 68u
    private val serverPort: UShort = 67u

    var counter = 1 //belongs to TODO@chooseIpAddr

    var isActive = false
    var firstIPv4Address = IPv4Address("10.1.0.10")
    var lastIPv4Address = IPv4Address("10.1.0.20")

    var serverName: String? = "server with mac: ${networkInterface.macAddress}"

    var tasks = mutableListOf<Task>()

    fun startServer() {
        isActive = true
    }


    fun receive(message: DHCPMessage) {
        when (message.op) {
            DHCPMessageType.DISCOVER -> {
                discover(message)
            }
            DHCPMessageType.REQUEST -> {
                request(message)
            }
            else -> println("unexpected Message")
        }
    }


    fun chooseIpAddr(): IPv4Address {
        //TODO

        return IPv4Address("10.1.0.${(10..20).random()}")
    }

    private fun discover(message: DHCPMessage) {
        println("server received DHCPDISCOVER message")
        val task = Task(message.xid, chooseIpAddr(), message.chaddr, networkInterface.macAddress, message.ciaddr)
        tasks.add(task)

        Thread.sleep(500)

        println("server is sending DHCPOFFER")
        owner.sendPacketUDP(
            serverPort,
            IPv4Address.LOCALBROADCAST,
            clientPort,
            DHCPMessage(
                op = DHCPMessageType.OFFER,
                xid = task.xid,
                ciaddr = task.ciaddr,
                yiaddr = task.reservedIPv4Address,
                siaddr = networkInterface.ipv4,
                chaddr = task.chaddr,
                shaddr = networkInterface.macAddress,
                sname = serverName,
                options = null
            )
        )
    }

    private fun request(message: DHCPMessage) {
        println("server received DHCPREQUEST ")

        if (tasks.find { it.shaddr == message.shaddr && it.xid == message.xid }?.run {
                println("server is sending DHCPACK")
                owner.sendPacketUDP(
                    serverPort,
                    IPv4Address.LOCALBROADCAST,
                    clientPort,
                    DHCPMessage(
                        op = DHCPMessageType.ACK,
                        xid = message.xid,
                        ciaddr = message.ciaddr,
                        yiaddr = reservedIPv4Address,
                        siaddr = networkInterface.ipv4,
                        chaddr = message.chaddr,
                        shaddr = shaddr,
                        sname = serverName,
                        options = null
                    )
                )
                tasks.remove(this)
            } == null) {
            println("server: secretly discarding the request because the client didn't choose this server")
            tasks.find { it.chaddr == message.chaddr }?.run {
                tasks.remove(this)
            }
        }

    }

}

data class Task(
    val xid: UInt,
    val reservedIPv4Address: IPv4Address,
    val chaddr: MacAddress,
    val shaddr: MacAddress?,
    val ciaddr: IPv4Address?,
)

