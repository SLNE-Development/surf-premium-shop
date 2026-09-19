package dev.slne.surf.premium.shop.command.subcommands.category.item

import dev.jorel.commandapi.CommandAPICommand
import dev.jorel.commandapi.kotlindsl.getValue
import dev.jorel.commandapi.kotlindsl.subcommand
import dev.slne.surf.api.core.messages.adventure.sendText
import dev.slne.surf.api.paper.command.executors.anyExecutorSuspend
import dev.slne.surf.premium.shop.command.subcommands.category.arguments.furnitureCategoryArgument
import dev.slne.surf.premium.shop.command.subcommands.category.arguments.furnitureItemArgument
import dev.slne.surf.premium.shop.furniture.FurnitureManager
import dev.slne.surf.premium.shop.furniture.category.FurnitureCategory
import dev.slne.surf.premium.shop.furniture.item.FurnitureItem
import dev.slne.surf.premium.shop.utils.PermissionRegistry

fun CommandAPICommand.itemRemoveCommand() = subcommand("remove") {
    withPermission(PermissionRegistry.COMMAND_FURNITURE_CATEGORY_REMOVE_ITEM)

    furnitureCategoryArgument("category")
    furnitureItemArgument("item", "category")

    anyExecutorSuspend { sender, arguments ->
        val category: FurnitureCategory by arguments
        val item: FurnitureItem by arguments

        FurnitureManager.removeItemFromCategory(category, item)

        sender.sendText {
            appendSuccessPrefix()

            success("Das Item ")
            append(item)
            success(" wurde aus der Kategorie ")
            append(category)
            success(" entfernt.")
        }
    }
}

