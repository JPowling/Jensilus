package de.filius.components.subcomponents

import de.filius.addresses.MacAddress

class NetworkInterface {

    val macAddress = MacAddress.createMac(this)

}