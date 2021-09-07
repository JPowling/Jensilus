package de.jensilus

import kotlinx.coroutines.DelicateCoroutinesApi
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import kotlin.random.Random

fun UByte.Companion.random(): UByte {
    return Random.nextInt(Byte.MAX_VALUE.toInt()).toUByte()
}

@Suppress("EXPERIMENTAL_IS_NOT_ENABLED")
@OptIn(DelicateCoroutinesApi::class)
fun launch(unit: suspend GlobalScope.() -> Unit) {
    GlobalScope.launch { unit.invoke(GlobalScope) }
}