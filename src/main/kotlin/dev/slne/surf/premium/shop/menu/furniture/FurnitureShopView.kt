package dev.slne.surf.premium.shop.menu.furniture

import dev.slne.surf.api.paper.builder.displayName
import dev.slne.surf.api.paper.inventory.framework.dsl.onItemClick
import dev.slne.surf.api.paper.inventory.framework.dsl.openForPlayer
import dev.slne.surf.api.paper.inventory.framework.dsl.withItem
import dev.slne.surf.api.paper.inventory.framework.view.layoutTarget
import dev.slne.surf.api.paper.inventory.framework.view.paginatedSurfView
import dev.slne.surf.api.paper.inventory.framework.view.pagination.pagination
import dev.slne.surf.api.paper.inventory.framework.view.settings
import dev.slne.surf.api.paper.inventory.framework.view.settings.PaginationViewRows
import dev.slne.surf.premium.shop.manager.PremiumShopManager

val furnitureShopView = paginatedSurfView("Furniture") {
    pagination {
        lazySource { context ->
            PremiumShopManager.getAvailableCategories(context.player)
        }
        elementFactory { _, builder, _, category ->
            with(builder) {
                withItem(category.displayItem) {
                    displayName(category.displayName)
                }

                onItemClick {
                    openForPlayer(furnitureItemView, category)
                }
            }
        }
    }

    layoutTarget('L')

    settings {
        paginationViewRows(PaginationViewRows.THREE)
    }
}