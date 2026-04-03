package dev.slne.surf.premium.shop.command.subcommands.category

import dev.jorel.commandapi.CommandAPICommand
import dev.jorel.commandapi.kotlindsl.subcommand
import dev.slne.surf.premium.shop.utils.PermissionRegistry

fun CommandAPICommand.categoriesCommand() = subcommand("category") {
    withPermission(PermissionRegistry.COMMAND_FURNITURE_CATEGORY_BASE)

    categoriesListCommand()
    categoriesInfoCommand()
    categoriesCreateCommand()
    categoriesDeleteCommand()
}