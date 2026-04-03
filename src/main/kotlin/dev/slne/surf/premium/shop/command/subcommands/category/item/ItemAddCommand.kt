package dev.slne.surf.premium.shop.command.subcommands.category.item

import dev.jorel.commandapi.CommandAPICommand
import dev.jorel.commandapi.kotlindsl.*
import dev.slne.surf.premium.shop.command.subcommands.category.arguments.furnitureCategoryArgument
import dev.slne.surf.premium.shop.config.PremiumShopConfig
import dev.slne.surf.premium.shop.furniture.category.FurnitureCategory
import dev.slne.surf.premium.shop.furniture.item.FurnitureItem
import dev.slne.surf.premium.shop.utils.PermissionRegistry
import dev.slne.surf.surfapi.core.api.messages.adventure.sendText
import net.kyori.adventure.text.Component

fun CommandAPICommand.itemAddCommand() = subcommand("add") {
    withPermission(PermissionRegistry.COMMAND_FURNITURE_CATEGORY_ADD_ITEM)

    furnitureCategoryArgument("category")
    stringArgument("name")
    integerArgument("price")
    booleanArgument("enabled", optional = true)

    playerExecutor { player, arguments ->
        val category: FurnitureCategory by arguments
        val name: String by arguments
        val price: Int by arguments
        val enabled = arguments.getOrDefaultUnchecked("enabled", true)

        if (category.items.any { it.name.equals(name, true) }) {
            player.sendText {
                appendErrorPrefix()

                error("Die Kategorie ")
                append(category)
                error(" enthält bereits ein Item mit dem Namen ")
                variableValue(name)
                error(".")
            }

            return@playerExecutor
        }

        val itemStack = player.inventory.itemInMainHand
        if (itemStack.type.isAir) {
            player.sendText {
                appendErrorPrefix()

                error("Du musst ein Item in der Hand halten, um es hinzuzufügen.")
            }

            return@playerExecutor
        }

        val displayName = itemStack.itemMeta?.displayName() ?: Component.text(name)
        val latestSortingIndex = category.items.maxOfOrNull { it.sortingIndex } ?: 0

        val item = FurnitureItem(
            name = name,
            displayName = displayName,
            price = price,
            itemStack = itemStack.clone(),
            enabled = enabled,
            sortingIndex = latestSortingIndex + 1,
            categoryName = category.name
        )

        PremiumShopConfig.edit {
            furniture.items.add(item)
        }

        player.sendText {
            appendSuccessPrefix()

            success("Das Item ")
            append(item)
            success(" wurde zur Kategorie ")
            append(category)
            success(" hinzugefügt.")
        }
    }
}

