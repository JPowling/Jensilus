package de.filius.components

import de.filius.components.subcomponents.NetworkInterface

open class Component(defaultNetworkInterfaces: Int) {

    val networkInterfaces = mutableListOf<NetworkInterface>()

    init {
        for (i in 0..defaultNetworkInterfaces) {
            networkInterfaces.add(NetworkInterface())
        }
    }

}