package de.jensilus.networking.commands

import de.jensilus.addresses.IPv4Address
import de.jensilus.components.Device
import de.jensilus.networking.Packet
import de.jensilus.networking.PacketICMP

object CommandPing : Command("ping", "penis") {

    override fun onDispatch(dispatcher: Device, vararg arguments: String) {
        val sender = dispatcher.networkInterface
        val destinationAddress = IPv4Address(arguments[0])

        for (i in 1..4) {
            val packetPing = PacketICMP(dispatcher.networkInterface, destinationAddress, Ping(i))
            dispatcher.sendPacket(packetPing)
        }
    }

    override fun onReact(receiver: Device, packet: Packet) {
        if (packet !is PacketICMP) return

        when (val content = packet.body) {
            is Ping -> {
                val result = PingResult(content.currentCount, System.currentTimeMillis() - content.startingTime)

                receiver.sendPacketICMP(packet.sender.ipv4, result)
            }
            is PingResult -> {
                receiver.log("${content.currentCount}. answer from ${packet.sender.ipv4} Delay:${content.time}")
            }
        }
    }

}

class Ping(
    val currentCount: Int,
) : CommandData(CommandPing) {
    val startingTime = System.currentTimeMillis()
}

class PingResult(
    val currentCount: Int,
    val time: Long,
) : CommandData(CommandPing)