package de.jensilus.networking

open class Packet (val body: Any? = null) {
    override fun toString(): String {
        return "Packet{body=$body}"
    }
}