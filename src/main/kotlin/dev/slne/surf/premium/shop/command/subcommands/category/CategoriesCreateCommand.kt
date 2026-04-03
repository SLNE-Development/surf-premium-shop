package dev.slne.surf.premium.shop.command.subcommands.category

import dev.jorel.commandapi.CommandAPICommand
import dev.jorel.commandapi.kotlindsl.*
import dev.slne.surf.premium.shop.config.PremiumShopConfig
import dev.slne.surf.premium.shop.config.config
import dev.slne.surf.premium.shop.furniture.category.FurnitureCategory
import dev.slne.surf.premium.shop.utils.PermissionRegistry
import dev.slne.surf.surfapi.core.api.messages.adventure.sendText
import java.util.*

fun CommandAPICommand.categoriesCreateCommand() = subcommand("create") {
    withPermission(PermissionRegistry.COMMAND_FURNITURE_CATEGORY_CREATE)

    stringArgument("name")
    textArgument("displayName")
    booleanArgument("enabled", optional = true)

    anyExecutor { sender, arguments ->
        val name: String by arguments
        val displayName: String by arguments
        val enabled = arguments.getOrDefaultUnchecked("enabled", true)

        if (config.furnitureCategoryByName(name) != null) {
            sender.sendText {
                appendErrorPrefix()

                error("Es existiert bereits eine Kategorie mit dem Namen ")
                variableValue(name)
                error(".")
            }

            return@anyExecutor
        }

        val category = FurnitureCategory(
            name = name,
            itemDisplayName = displayName,
            items = LinkedList(),
            enabled = enabled,
        )

        PremiumShopConfig.edit {
            addFurnitureCategory(category)
        }

        sender.sendText {
            appendSuccessPrefix()

            success("Die Kategorie ")
            append(category)
            success(" wurde erfolgreich erstellt.")
        }
    }
}