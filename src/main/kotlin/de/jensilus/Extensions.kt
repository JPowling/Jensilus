package de.jensilus

import kotlin.random.Random

fun UByte.Companion.random(): UByte {
    return Random.nextInt(Byte.MAX_VALUE.toInt()).toUByte()
}