package dev.slne.surf.premium.shop.command.subcommands.category.item

import dev.jorel.commandapi.CommandAPICommand
import dev.jorel.commandapi.kotlindsl.subcommand

fun CommandAPICommand.itemsCommand() = subcommand("item") {
    itemAddCommand()
    itemRemoveCommand()
}

