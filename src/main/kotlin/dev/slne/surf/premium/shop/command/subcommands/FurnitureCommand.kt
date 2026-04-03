package dev.slne.surf.premium.shop.command.subcommands

import dev.jorel.commandapi.CommandAPICommand
import dev.jorel.commandapi.kotlindsl.subcommand
import dev.slne.surf.premium.shop.command.subcommands.category.categoriesCommand
import dev.slne.surf.premium.shop.utils.PermissionRegistry

fun CommandAPICommand.furnitureCommand() = subcommand("furniture") {
    withPermission(PermissionRegistry.COMMAND_FURNITURE_BASE)

    categoriesCommand()
}