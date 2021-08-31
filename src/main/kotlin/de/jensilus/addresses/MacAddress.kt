package de.jensilus.addresses

import de.jensilus.components.subcomponents.NetworkInterface
import de.jensilus.exceptions.AddressFormatException
import de.jensilus.random
import java.io.Closeable

class MacAddress internal constructor(val bytes: Array<UByte>, val netInterface: NetworkInterface) : Closeable {

    init {
        if (bytes.size != 6) {
            throw AddressFormatException("Mac address bytes have an invalid length! (${bytes.size})")
        }
        if (containsMac(bytes)) {
            throw AddressFormatException("Mac address is already in use!")
        }
        if (containsNetInterface(netInterface)) {
            throw AddressFormatException("Network interface already bound to a mac address!")
        }

        savedMacAddresses += this
    }

    companion object {
        val savedMacAddresses = mutableListOf<MacAddress>()

        fun containsMac(mac: Array<UByte>): Boolean {
            outer@ for (oMac in savedMacAddresses.map { it.bytes }) {
                for (i in 0..6) {
                    if (mac[i] != oMac[i]) {
                        continue@outer
                    }
                    return true
                }
            }
            return false
        }

        fun containsNetInterface(netInterface: NetworkInterface): Boolean {
            return savedMacAddresses.map { it.netInterface }.contains(netInterface)
        }

        private fun createRandomMacBytes(): Array<UByte> {
            val array = mutableListOf<UByte>()

            do {
                array.clear()

                for (i in 1..6) {
                    array += UByte.random()
                }
            } while (containsMac(array.toTypedArray()))

            return array.toTypedArray()
        }

        fun createMac(netInterface: NetworkInterface): MacAddress {
            return MacAddress(createRandomMacBytes(), netInterface)
        }
    }

    override fun toString(): String {
        var str = ""
        for (i in 0 until 5) {
            str += bytes[i].toString(16) + ":"
        }
        str += bytes[5].toString(16)
        return str
    }

    override fun close() {
        savedMacAddresses -= this
    }

    override fun equals(other: Any?): Boolean {
        if (other == null || other !is MacAddress) {
            return false
        }

        return bytes.contentEquals(other.bytes)
    }

    override fun hashCode(): Int {
        return bytes.contentHashCode()
    }

    fun cloneTo(netInterface: NetworkInterface): MacAddress {
        return MacAddress(bytes, netInterface)
    }

}