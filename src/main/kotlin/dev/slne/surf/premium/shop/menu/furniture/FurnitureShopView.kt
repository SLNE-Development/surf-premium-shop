package dev.slne.surf.premium.shop.menu.furniture

import dev.slne.surf.premium.shop.config.config
import dev.slne.surf.premium.shop.menu.ShopView
import dev.slne.surf.surfapi.bukkit.api.builder.buildItem
import dev.slne.surf.surfapi.bukkit.api.builder.displayName
import dev.slne.surf.surfapi.bukkit.api.inventory.framework.dsl.onItemClick
import dev.slne.surf.surfapi.bukkit.api.inventory.framework.dsl.openForPlayer
import dev.slne.surf.surfapi.bukkit.api.inventory.framework.view.layoutTarget
import dev.slne.surf.surfapi.bukkit.api.inventory.framework.view.paginatedSurfView
import dev.slne.surf.surfapi.bukkit.api.inventory.framework.view.pagination.pagination
import dev.slne.surf.surfapi.bukkit.api.inventory.framework.view.settings
import dev.slne.surf.surfapi.bukkit.api.inventory.framework.view.settings.PaginationViewRows

object FurnitureShopView : ShopView(
    itemDisplayName = {
        primary("Furniture Shop")
    },
    itemLore = {
        emptyLine()
        line {
            spacer("Hier kannst du Furniture kaufen,")
        }
        line {
            spacer(" mit welcher du deine Base verschönern kannst.")
        }
    }
) {
    override fun buildView() = paginatedSurfView("Furniture") {
        pagination {
            lazySource {
                config.furnitureShopCategories
                    .filter { it.enabled }
                    .sortedBy { it.sortingIndex }
            }
            elementFactory { _, builder, _, category ->
                builder.withItem(buildItem(category.displayItemType) {
                    displayName(category.displayName)
                }).onItemClick {
                    openForPlayer(furnitureItemView, category)
                }
            }
        }

        layoutTarget('L')

        settings {
            paginationViewRows(PaginationViewRows.THREE)
        }
    }
}

val furnitureShopView = FurnitureShopView.buildView()