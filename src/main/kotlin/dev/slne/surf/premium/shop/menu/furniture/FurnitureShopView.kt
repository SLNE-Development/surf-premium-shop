package dev.slne.surf.premium.shop.menu.furniture

import dev.slne.surf.premium.shop.menu.ShopView
import dev.slne.surf.surfapi.bukkit.api.inventory.framework.view.paginatedSurfView

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

    }
}

val furnitureShopView = FurnitureShopView.buildView()