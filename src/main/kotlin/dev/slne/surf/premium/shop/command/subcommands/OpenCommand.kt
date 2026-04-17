package dev.slne.surf.premium.shop.command.subcommands

import dev.jorel.commandapi.CommandAPICommand
import dev.jorel.commandapi.kotlindsl.playerExecutor
import dev.jorel.commandapi.kotlindsl.subcommand
import dev.slne.surf.premium.shop.menu.mainMenu
import dev.slne.surf.premium.shop.utils.PermissionRegistry
import dev.slne.surf.api.paper.inventory.framework.open

fun CommandAPICommand.openCommand() = subcommand("open") {
    withPermission(PermissionRegistry.COMMAND_OPEN)

    playerExecutor { player, _ ->
        mainMenu.open(player)
    }
}