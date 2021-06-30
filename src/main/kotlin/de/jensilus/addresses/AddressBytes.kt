package de.jensilus.addresses

import de.jensilus.exceptions.AddressFormatException

class AddressBytes(val bytes: Array<UByte>) {

    val isNetmaskFormat: Boolean
        get() {
            val string = bytes.toBinaryString()
            val indexOfZeros = string.indexOfFirst { it == '0' }

            if (indexOfZeros == -1)
                return true

            return !string.substring(indexOfZeros).contains("1")
        }

    companion object {

        fun fromString(address: String): AddressBytes {
            if (!address.isIPv4Format()) {
                throw AddressFormatException("IPv4 address has a wrong format! ($address)")
            }

            return address.addressToUByteArray()
        }

    }

    override fun toString(): String{
        var retVal = ""
        for(byte in bytes){
            retVal += "$byte."
        }
        return retVal.substring(0, retVal.length-1)
    }



}

fun String.isIPv4Format(): Boolean {
    return matches(
        Regex(
            "^(25[0-5]|2[0-4][0-9]|[01]?[0-9][0-9]?)\\.(25[0-5]|2[0-4][0-9]|[01]?[0-9][0-9]?)" +
                    "\\.(25[0-5]|2[0-4][0-9]|[01]?[0-9][0-9]?)\\.(25[0-5]|2[0-4][0-9]|[01]?[0-9][0-9]?)\$"
        )
    )
}

fun String.addressToUByteArray(): AddressBytes {
    return AddressBytes(split(".").map { it.toUByte() }.toTypedArray())
}

fun String.addressToBinaryString(): String {
    val binaryBytes = addressToUByteArray().bytes.map { it.toBinaryString() }

    var concatenated = ""
    binaryBytes.forEach { concatenated += it }
    return concatenated
}

fun UByte.toBinaryString(): String {
    val zero = "00000000"
    val binary = toString(2)
    return zero.substring(binary.length) + binary
}

fun Array<UByte>.toBinaryString(): String {
    val binaryBytes = map { it.toBinaryString() }

    var concatenated = ""
    binaryBytes.forEach { concatenated += it }
    return concatenated
}

