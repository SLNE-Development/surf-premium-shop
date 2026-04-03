package dev.slne.surf.premium.shop.command.subcommands.category.item

import dev.jorel.commandapi.CommandAPI
import dev.jorel.commandapi.CommandAPICommand
import dev.jorel.commandapi.arguments.ArgumentSuggestions
import dev.jorel.commandapi.kotlindsl.anyExecutor
import dev.jorel.commandapi.kotlindsl.getValue
import dev.jorel.commandapi.kotlindsl.stringArgument
import dev.jorel.commandapi.kotlindsl.subcommand
import dev.slne.surf.premium.shop.command.subcommands.category.arguments.furnitureCategoryArgument
import dev.slne.surf.premium.shop.config.PremiumShopConfig
import dev.slne.surf.premium.shop.config.config
import dev.slne.surf.premium.shop.furniture.category.FurnitureCategory
import dev.slne.surf.premium.shop.utils.PermissionRegistry
import dev.slne.surf.surfapi.core.api.messages.adventure.sendText

fun CommandAPICommand.itemRemoveCommand() = subcommand("remove") {
    withPermission(PermissionRegistry.COMMAND_FURNITURE_CATEGORY_REMOVE_ITEM)

    furnitureCategoryArgument("category")
    stringArgument("item") {
        replaceSuggestions(ArgumentSuggestions.stringCollection { info ->
            val category = info.previousArgs().getUnchecked<FurnitureCategory>("category")
                ?: throw CommandAPI.failWithString("Category not set.")

            category.items.map { it.name }
        })
    }

    anyExecutor { sender, arguments ->
        val category: FurnitureCategory by arguments
        val itemName: String by arguments

        val item = config.furniture.itemByName(category.name, itemName) ?: run {
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
            furniture.items.remove(item)
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

