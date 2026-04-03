package dev.slne.surf.premium.shop.command.subcommands.category.item

import dev.jorel.commandapi.CommandAPICommand
import dev.jorel.commandapi.kotlindsl.anyExecutor
import dev.jorel.commandapi.kotlindsl.getValue
import dev.jorel.commandapi.kotlindsl.stringArgument
import dev.jorel.commandapi.kotlindsl.subcommand
import dev.slne.surf.premium.shop.command.subcommands.category.arguments.furnitureCategoryArgument
import dev.slne.surf.premium.shop.config.PremiumShopConfig
import dev.slne.surf.premium.shop.furniture.category.FurnitureCategory
import dev.slne.surf.premium.shop.utils.PermissionRegistry
import dev.slne.surf.surfapi.core.api.messages.adventure.sendText

fun CommandAPICommand.itemRemoveCommand() = subcommand("remove") {
    withPermission(PermissionRegistry.COMMAND_FURNITURE_CATEGORY_REMOVE_ITEM)

    furnitureCategoryArgument("category")
    stringArgument("item")

    anyExecutor { sender, arguments ->
        val category: FurnitureCategory by arguments
        val itemName: String by arguments

        val item = category.items.firstOrNull { it.name.equals(itemName, true) }
        if (item == null) {
            sender.sendText {
                appendErrorPrefix()

                error("Das Item ")
                variableValue(itemName)
                error(" wurde in der Kategorie ")
                append(category)
                error(" nicht gefunden.")
            }

            return@anyExecutor
        }

        PremiumShopConfig.edit {
            furnitureCategoryByName(category.name)?.items?.removeIf { it.name.equals(itemName, true) }
        }

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

