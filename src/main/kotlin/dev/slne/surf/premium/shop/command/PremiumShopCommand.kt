package dev.slne.surf.premium.shop.command

import dev.jorel.commandapi.kotlindsl.commandAPICommand
import dev.slne.surf.premium.shop.command.subcommands.furnitureCommand
import dev.slne.surf.premium.shop.command.subcommands.openCommand
import dev.slne.surf.premium.shop.command.subcommands.reloadCommand
import dev.slne.surf.premium.shop.utils.PermissionRegistry

fun premiumShopCommand() = commandAPICommand("premiumshop") {
    withPermission(PermissionRegistry.COMMAND_BASE)

    furnitureCommand()
    reloadCommand()
    openCommand()
}