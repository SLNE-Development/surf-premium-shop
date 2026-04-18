package dev.slne.surf.premium.shop.command.subcommands.category

import dev.jorel.commandapi.CommandAPICommand
import dev.jorel.commandapi.kotlindsl.anyExecutor
import dev.jorel.commandapi.kotlindsl.getValue
import dev.jorel.commandapi.kotlindsl.subcommand
import dev.slne.surf.premium.shop.command.subcommands.category.arguments.furnitureCategoryArgument
import dev.slne.surf.premium.shop.furniture.category.FurnitureCategory
import dev.slne.surf.premium.shop.utils.PermissionRegistry
import dev.slne.surf.api.core.messages.adventure.sendText

fun CommandAPICommand.categoriesInfoCommand() = subcommand("info") {
    withPermission(PermissionRegistry.COMMAND_FURNITURE_CATEGORY_INFO)

    furnitureCategoryArgument("category")

    anyExecutor { sender, arguments ->
        val category: FurnitureCategory by arguments

        sender.sendText {
            append(category)
        }
    }
}