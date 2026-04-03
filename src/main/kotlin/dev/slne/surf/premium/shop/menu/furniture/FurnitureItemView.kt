package dev.slne.surf.premium.shop.menu.furniture

import dev.slne.surf.premium.shop.furniture.category.FurnitureCategory
import dev.slne.surf.surfapi.bukkit.api.builder.buildItem
import dev.slne.surf.surfapi.bukkit.api.builder.buildLore
import dev.slne.surf.surfapi.bukkit.api.builder.displayName
import dev.slne.surf.surfapi.bukkit.api.inventory.framework.dsl.onItemClick
import dev.slne.surf.surfapi.bukkit.api.inventory.framework.view.*
import dev.slne.surf.surfapi.bukkit.api.inventory.framework.view.container.dsl.header
import dev.slne.surf.surfapi.bukkit.api.inventory.framework.view.pagination.pagination
import dev.slne.surf.surfapi.bukkit.api.inventory.framework.view.settings.PaginationViewRows
import dev.slne.surf.surfapi.bukkit.api.inventory.framework.view.state.get
import dev.slne.surf.surfapi.bukkit.api.inventory.framework.view.state.initialState
import dev.slne.surf.surfapi.core.api.messages.adventure.plain
import dev.slne.surf.surfapi.core.api.messages.adventure.sendText

val furnitureItemView = paginatedSurfView("...") {
    val categoryStateHandle = initialState<FurnitureCategory>()

    layoutTarget('L')

    settings {
        paginationViewRows(PaginationViewRows.THREE)
    }

    pagination {
        lazySource {
            val categoryState = categoryStateHandle[it]

            categoryState.items
        }

        elementFactory { _, builder, _, item ->
            builder.withItem(buildItem(item.itemStack.type) {
                displayName(item.displayName)
                buildLore {
                    emptyLine()
                    line {
                        variableKey("Preis: ")
                        // TODO Add transaction api to retrieve correct currency display
                        variableValue("${item.price} CC")
                    }
                }
            }).onItemClick {
                player.inventory.addItem(item.itemStack.clone())

                // TODO: Remove currency via transaction api

                player.sendText {
                    appendSuccessPrefix()
                    success("Du hast ")
                    append(item)
                    success(" erfolgreich für ")
                    // TODO Add transaction api to retrieve correct currency display
                    variableValue("${item.price} CC")
                    success(" erworben!")
                }
            }
        }
    }

    onOpen {
        val categoryState = categoryStateHandle[this]

        modifyContainer {
            header(categoryState.displayName.plain())
        }
    }
}