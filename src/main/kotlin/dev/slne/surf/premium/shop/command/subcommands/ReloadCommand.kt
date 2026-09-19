package dev.slne.surf.premium.shop.command.subcommands

import dev.jorel.commandapi.CommandAPICommand
import dev.jorel.commandapi.kotlindsl.subcommand
import dev.slne.surf.api.core.messages.adventure.sendText
import dev.slne.surf.api.paper.command.executors.anyExecutorSuspend
import dev.slne.surf.premium.shop.furniture.FurnitureManager
import dev.slne.surf.premium.shop.utils.PermissionRegistry

fun CommandAPICommand.reloadCommand() = subcommand("reload") {
    withPermission(PermissionRegistry.COMMAND_RELOAD)

    anyExecutorSuspend { sender, _ ->
        FurnitureManager.loadFromFile()

        sender.sendText {
            appendSuccessPrefix()
            success("Die Furniture Kategorien wurden erfolgreich neu geladen.")
        }
    }
}