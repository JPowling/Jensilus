package de.jensilus.networking.commands

import de.jensilus.components.Device
import de.jensilus.launch
import de.jensilus.networking.Packet

abstract class Command(val keyword: String, vararg val aliases: String) {

    abstract fun onDispatch(dispatcher: Device, vararg arguments: String)
    abstract fun onReact(receiver: Device, packet: Packet)

}

open class CommandData(val owner: Command)

object Commands {

    private val commands = mutableListOf<Command>()

    init {
        commands += CommandPing
    }

    fun handle(device: Device, input: String) {
        launch {
            val splitted = input.split(" ")

            val keyword = splitted[0]
            val args = splitted.run { subList(1, size) }

            commands
                .first { it.keyword == keyword || it.aliases.contains(keyword) }
                .onDispatch(device, *args.toTypedArray())
        }
    }

}