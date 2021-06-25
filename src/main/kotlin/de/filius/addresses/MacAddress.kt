package de.filius.addresses

import de.filius.components.subcomponents.NetworkInterface
import de.filius.exceptions.AddressFormatException
import de.filius.random
import java.io.Closeable
import javax.crypto.Mac

class MacAddress internal constructor(val bytes: Array<UByte>, val netInterface: NetworkInterface): Closeable {

    init {
        if (bytes.size != 6) {
            throw AddressFormatException("Mac address bytes have an invalid length!")
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
            val array = arrayOf<UByte>()

            do {
                for (i in 0..6) {
                    array[i] = UByte.random()
                }
            } while (containsMac(array))

            return array
        }

        fun createMac(netInterface: NetworkInterface): MacAddress {
            return MacAddress(createRandomMacBytes(), netInterface)
        }
    }

    override fun close() {
        savedMacAddresses -= this
    }

}