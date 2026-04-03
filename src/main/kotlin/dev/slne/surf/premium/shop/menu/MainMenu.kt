@file:Suppress("UnstableApiUsage")

package dev.slne.surf.premium.shop.menu

import dev.slne.surf.premium.shop.menu.furniture.FurnitureShopView
import dev.slne.surf.premium.shop.menu.furniture.furnitureShopView
import dev.slne.surf.surfapi.bukkit.api.builder.buildItem
import dev.slne.surf.surfapi.bukkit.api.builder.displayName
import dev.slne.surf.surfapi.bukkit.api.inventory.framework.dsl.onItemClick
import dev.slne.surf.surfapi.bukkit.api.inventory.framework.dsl.openForPlayer
import dev.slne.surf.surfapi.bukkit.api.inventory.framework.view.*
import dev.slne.surf.surfapi.bukkit.api.inventory.framework.view.container.dsl.blockRow
import dev.slne.surf.surfapi.bukkit.api.inventory.framework.view.settings.ViewRows
import org.bukkit.inventory.ItemType

val mainMenu = surfView("PremiumShop") {
    settings {
        rows(ViewRows.THREE)
    }

    containerDefaults {
        blockRow(1)
        blockRow(2, exemptColumns = intArrayOf(4))
        blockRow(3)
    }

    onInit {
        layout(
            "         ",
            "    F    ",
            "         "
        )
    }

    onFirstRender {
        layoutSlot('F', buildItem(ItemType.OAK_SHELF) {
            displayName(FurnitureShopView.itemDisplayName)
            lore(FurnitureShopView.itemLore)
        }).onItemClick {
            openForPlayer(furnitureShopView)
        }
    }
}