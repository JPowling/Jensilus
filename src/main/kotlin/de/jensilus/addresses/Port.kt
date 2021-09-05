package de.jensilus.addresses

import com.google.common.primitives.UnsignedInteger

class Port(portLong: Long) {
    var port: UnsignedInteger = UnsignedInteger.valueOf(portLong)

    override fun toString(): String {
        return port.toString()
    }
}
