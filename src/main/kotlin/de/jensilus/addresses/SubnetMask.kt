package de.jensilus.addresses

import de.jensilus.exceptions.AddressFormatException
import kotlin.math.pow

class SubnetMask(val subnetMask: AddressBytes) {

    constructor(subnetMask: String) : this(AddressBytes.fromString(subnetMask))

    val bytes = subnetMask.bytes

    val maxDevices: Int by lazy {
        val exp = (32 - subnetMask.bytes.toBinaryString().indexOfFirst { it == '0' })

        2.0.pow(exp.toDouble()).toInt() - 2
    }

    init {
        if (!subnetMask.isNetmaskFormat) {
            throw AddressFormatException("Netmask has a wrong format! ($subnetMask)")
        }
    }

}
