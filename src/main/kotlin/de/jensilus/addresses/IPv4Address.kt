package de.jensilus.addresses

class IPv4Address(val addressBytes: AddressBytes) {

    constructor(addressBytes: String) : this(AddressBytes.fromString(addressBytes))

    fun getNetworkAddressBytes(subnetmask: SubnetMask): AddressBytes {
        val array = mutableListOf<UByte>()

        for (i in 0 until 4) {
            array += addressBytes.bytes[i] and subnetmask.bytes[i]
        }
        return AddressBytes(array.toTypedArray())
    }

}

private fun String.isNetmaskFormat(): Boolean {
    val string = addressToBinaryString()
    val indexOfZeros = string.indexOfFirst { it == '0' }

    if (indexOfZeros == -1)
        return true

    return !string.substring(indexOfZeros).contains("1")
}

private fun String.addressToUInt(): UInt {
    return addressToBinaryString().toUInt(2)
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

private fun Array<UByte>.toIPv4String(): String {
    return toBinaryString().binaryToIPv4String()
}