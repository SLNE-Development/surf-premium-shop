package dev.slne.surf.premium.shop.command.subcommands.category

import dev.jorel.commandapi.CommandAPICommand
import dev.jorel.commandapi.kotlindsl.*
import dev.slne.surf.api.core.messages.adventure.sendText
import dev.slne.surf.api.paper.command.args.miniMessageArgument
import dev.slne.surf.api.paper.command.executors.playerExecutorSuspend
import dev.slne.surf.premium.shop.furniture.FurnitureManager
import dev.slne.surf.premium.shop.furniture.category.FurnitureCategory
import dev.slne.surf.premium.shop.utils.PermissionRegistry
import net.kyori.adventure.text.Component
import org.bukkit.Registry

fun CommandAPICommand.categoriesCreateCommand() = subcommand("create") {
    withPermission(PermissionRegistry.COMMAND_FURNITURE_CATEGORY_CREATE)

    stringArgument("id")
    miniMessageArgument("displayName")
    booleanArgument("enabled", optional = true)

    playerExecutorSuspend { player, arguments ->
        val id: String by arguments
        val displayName: Component by arguments
        val enabled = arguments.getOrDefaultUnchecked("enabled", true)

        if (FurnitureManager.categoryById(id) != null) {
            player.sendText {
                appendErrorPrefix()

                error("Es existiert bereits eine Kategorie mit der ID ")
                variableValue(id)
                error(".")
            }

            return@playerExecutorSuspend
        }

        val displayItem = player.inventory.itemInMainHand

        if (displayItem.isEmpty) {
            player.sendText {
                appendErrorPrefix()
                error("Du musst ein Item in deiner Hand halten, welches als Display-Item für die Kategorie verwendet wird.")
            }

            return@playerExecutorSuspend
        }

        val category = FurnitureCategory(
            id = id,
            displayName = displayName,
            displayItem = Registry.ITEM.getOrThrow(displayItem.type.key()),
            enabled = enabled,
            sortingIndex = 0,
            permission = PermissionRegistry.createCategoryUsePermission(id),
        )

        FurnitureManager.registerCategory(category)

        player.sendText {
            appendSuccessPrefix()

            success("Die Kategorie ")
            append(category)
            success(" wurde erfolgreich erstellt.")
        }
    }
}