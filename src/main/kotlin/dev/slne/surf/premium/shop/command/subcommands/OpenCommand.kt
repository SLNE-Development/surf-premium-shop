package dev.slne.surf.premium.shop.command.subcommands

import dev.jorel.commandapi.CommandAPICommand
import dev.jorel.commandapi.kotlindsl.playerExecutor
import dev.jorel.commandapi.kotlindsl.subcommand
import dev.slne.surf.premium.shop.dialog.MainDialog
import dev.slne.surf.premium.shop.utils.PermissionRegistry

fun CommandAPICommand.openCommand() = subcommand("open") {
    withPermission(PermissionRegistry.COMMAND_OPEN)

    playerExecutor { player, _ ->
        player.showDialog(MainDialog.create(player))
    }
}