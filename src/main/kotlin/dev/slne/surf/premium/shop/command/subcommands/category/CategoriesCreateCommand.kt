package dev.slne.surf.premium.shop.command.subcommands.category

import dev.jorel.commandapi.CommandAPICommand
import dev.jorel.commandapi.kotlindsl.*
import dev.slne.surf.premium.shop.config.PremiumShopConfig
import dev.slne.surf.premium.shop.config.config
import dev.slne.surf.premium.shop.furniture.category.FurnitureCategory
import dev.slne.surf.premium.shop.utils.PermissionRegistry
import dev.slne.surf.surfapi.bukkit.api.command.args.miniMessageArgument
import dev.slne.surf.surfapi.core.api.messages.adventure.sendText
import net.kyori.adventure.text.Component
import org.bukkit.Material

fun CommandAPICommand.categoriesCreateCommand() = subcommand("create") {
    withPermission(PermissionRegistry.COMMAND_FURNITURE_CATEGORY_CREATE)

    stringArgument("name")
    miniMessageArgument("displayName")
    booleanArgument("enabled", optional = true)

    playerExecutor { player, arguments ->
        val name: String by arguments
        val displayName: Component by arguments
        val enabled = arguments.getOrDefaultUnchecked("enabled", true)

        if (config.furniture.categoryByName(name) != null) {
            player.sendText {
                appendErrorPrefix()

                error("Es existiert bereits eine Kategorie mit dem Namen ")
                variableValue(name)
                error(".")
            }

            return@playerExecutor
        }

        val displayItem = player.inventory.itemInMainHand

        if (displayItem.type == Material.AIR) {
            player.sendText {
                appendErrorPrefix()

                error("Du musst ein Item in deiner Main-Hand halten, welches als Displayitem für die Kategorie verwendet wird.")
            }

            return@playerExecutor
        }

        val latestSortingIndex = config.furniture.categories.maxOfOrNull { it.sortingIndex } ?: -1

        val category = FurnitureCategory(
            name = name,
            displayName = displayName,
            displayItemKey = displayItem.type.key().asString(),
            enabled = enabled,
            sortingIndex = latestSortingIndex + 1
        )

        PremiumShopConfig.edit {
            furniture.addCategory(category)
        }

        player.sendText {
            appendSuccessPrefix()

            success("Die Kategorie ")
            append(category)
            success(" wurde erfolgreich erstellt.")
        }
    }
}