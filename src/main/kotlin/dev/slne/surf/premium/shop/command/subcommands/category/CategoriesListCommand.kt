package dev.slne.surf.premium.shop.command.subcommands.category

import dev.jorel.commandapi.CommandAPICommand
import dev.jorel.commandapi.kotlindsl.anyExecutor
import dev.jorel.commandapi.kotlindsl.subcommand
import dev.slne.surf.premium.shop.config.config
import dev.slne.surf.premium.shop.furniture.category.FurnitureCategory
import dev.slne.surf.premium.shop.utils.PermissionRegistry
import dev.slne.surf.api.core.messages.adventure.buildText
import dev.slne.surf.api.core.messages.pagination.Pagination

private val pagination = Pagination<FurnitureCategory> {
    title {
        primary("Furniture Kategorien")
    }

    rowRenderer { category, i ->
        listOf(buildText {
            appendInfoPrefix()
            append(category)
        })
    }
}

fun CommandAPICommand.categoriesListCommand() = subcommand("list") {
    withPermission(PermissionRegistry.COMMAND_FURNITURE_CATEGORY_LIST)

    anyExecutor { sender, _ ->
        sender.sendMessage(pagination.renderComponent(config.furniture.categories))
    }
}