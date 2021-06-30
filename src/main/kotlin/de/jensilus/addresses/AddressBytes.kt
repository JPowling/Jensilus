package de.jensilus.addresses

import de.jensilus.exceptions.AddressFormatException

class AddressBytes(val bytes: Array<UByte>) {


    companion object{
        fun fromString(address: String): AddressBytes{
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

fun String.isSubnetMask(): Boolean

private fun String.addressToUByteArray(): AddressBytes {
    return AddressBytes(split(".").map { it.toUByte() }.toTypedArray())
}
