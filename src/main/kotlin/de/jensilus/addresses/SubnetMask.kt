package de.jensilus.addresses

import de.jensilus.exceptions.AddressFormatException

class SubnetMask(val subnetMask: AddressBytes){
    init {
        if()
    }



}

private fun String.isNetmaskFormat(): Boolean {
    val string = addressToBinaryString()
    val indexOfZeros = string.indexOfFirst { it == '0' }

    if (indexOfZeros == -1)
        return true

    return !string.substring(indexOfZeros).contains("1")
}


private fun String.addressToBinaryString(): String {
    val binaryBytes = addressToUByteArray().map { it.toBinaryString() }

    var concatenated = ""
    binaryBytes.forEach { concatenated += it }
    return concatenated
}
