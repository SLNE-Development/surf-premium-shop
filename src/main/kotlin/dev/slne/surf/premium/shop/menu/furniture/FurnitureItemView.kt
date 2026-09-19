package dev.slne.surf.premium.shop.menu.furniture

import dev.slne.surf.api.core.messages.adventure.plain
import dev.slne.surf.api.paper.builder.buildLore
import dev.slne.surf.api.paper.builder.displayName
import dev.slne.surf.api.paper.inventory.framework.dsl.onItemClick
import dev.slne.surf.api.paper.inventory.framework.dsl.openForPlayer
import dev.slne.surf.api.paper.inventory.framework.dsl.withItem
import dev.slne.surf.api.paper.inventory.framework.view.*
import dev.slne.surf.api.paper.inventory.framework.view.container.dsl.header
import dev.slne.surf.api.paper.inventory.framework.view.pagination.pagination
import dev.slne.surf.api.paper.inventory.framework.view.settings.PaginationViewRows
import dev.slne.surf.api.paper.inventory.framework.view.state.get
import dev.slne.surf.api.paper.inventory.framework.view.state.initialState
import dev.slne.surf.premium.shop.furniture.category.FurnitureCategory
import dev.slne.surf.premium.shop.manager.PremiumShopManager
import dev.slne.surf.transaction.api.currency.Currency

val furnitureItemView = paginatedSurfView("__Category__") {
    val categoryStateHandle = initialState<FurnitureCategory>()

    layoutTarget('L')

    settings {
        paginationViewRows(PaginationViewRows.THREE)
    }

    pagination {
        lazySource { context ->
            PremiumShopManager.getAvailableFromCategory(
                context.player,
                categoryStateHandle[context],
            )
        }

        elementFactory { _, builder, _, item ->
            builder.withItem(item.itemStackTemplate.type) {
                displayName(item.displayName)

                buildLore {
                    emptyLine()
                    line {
                        variableKey("Preis: ")
                        append(Currency.default().format(item.price.toBigDecimal()))
                    }
                }
            }

            builder.onItemClick {
                openForPlayer(furnitureItemBuyView, item)
            }
        }
    }

    onOpen {
        val category = categoryStateHandle[this]

        modifyContainer {
            header(category.displayName.plain())
        }
    }
}