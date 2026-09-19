package dev.slne.surf.premium.shop.command.subcommands.category.item

import dev.jorel.commandapi.CommandAPICommand
import dev.jorel.commandapi.kotlindsl.*
import dev.slne.surf.api.core.messages.adventure.sendText
import dev.slne.surf.api.paper.command.executors.playerExecutorSuspend
import dev.slne.surf.premium.shop.command.subcommands.category.arguments.furnitureCategoryArgument
import dev.slne.surf.premium.shop.furniture.FurnitureManager
import dev.slne.surf.premium.shop.furniture.category.FurnitureCategory
import dev.slne.surf.premium.shop.furniture.item.FurnitureItem
import dev.slne.surf.premium.shop.utils.PermissionRegistry
import io.papermc.paper.datacomponent.DataComponentTypes
import net.kyori.adventure.text.Component

@Suppress("UnstableApiUsage")
fun CommandAPICommand.itemAddCommand() = subcommand("add") {
    withPermission(PermissionRegistry.COMMAND_FURNITURE_CATEGORY_ADD_ITEM)

    furnitureCategoryArgument("category")
    stringArgument("id")
    integerArgument("price")
    booleanArgument("enabled", optional = true)

    playerExecutorSuspend { player, arguments ->
        val category: FurnitureCategory by arguments
        val id: String by arguments
        val price: Int by arguments
        val enabled = arguments.getOrDefaultUnchecked("enabled", true)

        if (category.itemById(id) != null) {
            player.sendText {
                appendErrorPrefix()

                error("Die Kategorie ")
                append(category)
                error(" enthält bereits ein Item mit der ID ")
                variableValue(id)
                error(".")
            }

            return@playerExecutorSuspend
        }

        val itemStack = player.inventory.itemInMainHand
        if (itemStack.isEmpty) {
            player.sendText {
                appendErrorPrefix()
                error("Du musst ein Item in der Hand halten, um es hinzuzufügen.")
            }

            return@playerExecutorSuspend
        }

        val displayName = itemStack.getData(DataComponentTypes.CUSTOM_NAME) ?: Component.text(id)

        val item = FurnitureItem(
            id = id,
            displayName = displayName,
            price = price,
            itemStack = itemStack.clone(),
            enabled = enabled,
            sortingIndex = 0,
            permission = PermissionRegistry.createFurnitureItemUsePermission(category.id, id)
        )

        FurnitureManager.registerItemToCategory(category, item)

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

