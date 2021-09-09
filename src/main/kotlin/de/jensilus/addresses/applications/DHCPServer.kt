package de.jensilus.addresses.applications

import de.jensilus.components.Device
import de.jensilus.components.subcomponents.NetworkInterface
import de.jensilus.launch
import de.jensilus.networking.DHCPMessage
import de.jensilus.networking.DHCPMessageType

class DHCPServer(val owner: Device, val networkInterface: NetworkInterface) {
    var isActive = false

    fun startServer() {
        initServer()
        isActive = true
        launch()
    }


    private fun initServer() {

    }

    private fun launch() {
        launch {
            while (true) {

            }
        }
    }

    fun receive(message: DHCPMessage) {
        when (message.op) {
            DHCPMessageType.OFFER -> { offer(message)}
        }
    }

    private fun offer(message: DHCPMessage) {
        println("server received DHCPOFFER message")
    }
}
