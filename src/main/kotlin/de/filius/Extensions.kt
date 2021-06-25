package de.filius

import kotlin.random.Random
import kotlin.random.nextUBytes

fun UByte.Companion.random(): UByte {
    return Random.nextInt(Byte.MAX_VALUE.toInt()).toUByte()
}