package de.filius.addresses

import de.filius.exceptions.AddressFormatException
import kotlin.math.pow
import kotlin.system.measureNanoTime
import kotlin.system.measureTimeMillis
import kotlin.time.measureTime

class IPv4Address(val addressString: String, val netmaskString: String) {

    val addressBytes: Array<UByte>
    val netmaskBytes: Array<UByte>

    init {
        if (!addressString.isIPv4Format()) {
            throw AddressFormatException("IPv4 address has a wrong format! ($addressString)")
        }
        if (!netmaskString.isNetmaskFormat()) {
            throw AddressFormatException("Netmask has a wrong format! ($netmaskString)")
        }

        addressBytes = addressString.addressToUByteArray()
        netmaskBytes = netmaskString.addressToUByteArray()
    }

    val networkAddressBytes: Array<UByte> by lazy {
        val array = mutableListOf<UByte>()

        for (i in 0 until 4) {
            array += addressBytes[i] and netmaskBytes[i]
        }
        array.toTypedArray()
    }

    val maxDevices: Int by lazy {
        val exp = (32 - netmaskString.addressToBinaryString().indexOfFirst { it == '0' })

        2.0.pow(exp.toDouble()).toInt() - 2
    }

}

private fun String.isIPv4Format(): Boolean {
    return matches(
        Regex(
            "^(25[0-5]|2[0-4][0-9]|[01]?[0-9][0-9]?)\\.(25[0-5]|2[0-4][0-9]|[01]?[0-9][0-9]?)" +
                    "\\.(25[0-5]|2[0-4][0-9]|[01]?[0-9][0-9]?)\\.(25[0-5]|2[0-4][0-9]|[01]?[0-9][0-9]?)\$"
        )
    )
}

private fun String.isNetmaskFormat(): Boolean {
    val string = addressToBinaryString()
    val indexOfZeros = string.indexOfFirst { it == '0' }

    if (indexOfZeros == -1)
        return true

    return !string.substring(indexOfZeros).contains("1")
}

private fun String.addressToBinaryString(): String {
    if (!isIPv4Format()) {
        throw AddressFormatException("IP address has wrong format! ($this)")
    }
    val binaryBytes = addressToUByteArray().map { it.toBinaryString() }

    var concatenated = ""
    binaryBytes.forEach { concatenated += it }
    return concatenated
}

private fun String.addressToUInt(): UInt {
    return addressToBinaryString().toUInt(2)
}

private fun String.addressToUByteArray(): Array<UByte> {
    return split(".").map { it.toUByte() }.toTypedArray()
}

private fun String.binaryToIPv4String(): String {
    var str = ""
    for (i in 0 until 4) {
        str += substring(i * 8, i * 8 + 8).toUByte(2)

        if (i != 3) {
            str += "."
        }
    }
    return str
}

private fun UByte.toBinaryString(): String {
    val zero = "00000000"
    val binary = toString(2)
    return zero.substring(binary.length) + binary
}

private fun Array<UByte>.toBinaryString(): String {
    val binaryBytes = map { it.toBinaryString() }

    var concatenated = ""
    binaryBytes.forEach { concatenated += it }
    return concatenated
}

private fun Array<UByte>.toIPv4String(): String {
    return toBinaryString().binaryToIPv4String()
}

private fun String.binaryToUByteArray(): Array<UByte> {
    val array = arrayOf<UByte>()
    for (i in 0..3) {
        val string = substring(i * 8, i * 8 + 8)
        array[i] = string.toUByte(2)
    }
    return array
}

private fun Array<UByte>.toUInt(): UInt {
    return toBinaryString().toUInt(2)
}