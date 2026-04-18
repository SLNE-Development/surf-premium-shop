package dev.slne.surf.premium.shop.command.subcommands.category

import dev.jorel.commandapi.CommandAPICommand
import dev.jorel.commandapi.kotlindsl.anyExecutor
import dev.jorel.commandapi.kotlindsl.getValue
import dev.jorel.commandapi.kotlindsl.subcommand
import dev.slne.surf.premium.shop.command.subcommands.category.arguments.furnitureCategoryArgument
import dev.slne.surf.premium.shop.config.PremiumShopConfig
import dev.slne.surf.premium.shop.furniture.category.FurnitureCategory
import dev.slne.surf.premium.shop.utils.PermissionRegistry
import dev.slne.surf.api.core.messages.adventure.clickCallback
import dev.slne.surf.api.core.messages.adventure.sendText
import net.kyori.adventure.text.format.TextDecoration

fun CommandAPICommand.categoriesDeleteCommand() = subcommand("delete") {
    withPermission(PermissionRegistry.COMMAND_FURNITURE_CATEGORY_DELETE)

    furnitureCategoryArgument("category")

    anyExecutor { sender, arguments ->
        val category: FurnitureCategory by arguments

        sender.sendText {
            appendInfoPrefix()

            info("Bist du dir sicher, dass du die Kategorie ")
            append(category)
            info(" löschen möchtest? Alle darin enthaltenen Furnitures werden ebenfalls gelöscht. Klicke ")
            append {
                error("HIER", TextDecoration.BOLD)
                clickCallback { clicker ->
                    PremiumShopConfig.edit {
                        furniture.removeCategory(category)
                    }

                    clicker.sendText {
                        appendSuccessPrefix()

                        success("Die Kategorie ")
                        append(category)
                        success(" wurde erfolgreich gelöscht.")
                    }
                }
            }
            info(" um zu bestätigen.")
        }
    }
}