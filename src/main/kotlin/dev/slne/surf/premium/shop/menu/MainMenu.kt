@file:Suppress("UnstableApiUsage")

package dev.slne.surf.premium.shop.menu

import dev.slne.surf.api.paper.builder.buildLore
import dev.slne.surf.api.paper.builder.displayName
import dev.slne.surf.api.paper.inventory.framework.dsl.layoutSlot
import dev.slne.surf.api.paper.inventory.framework.dsl.onItemClick
import dev.slne.surf.api.paper.inventory.framework.dsl.openForPlayer
import dev.slne.surf.api.paper.inventory.framework.dsl.withItem
import dev.slne.surf.api.paper.inventory.framework.view.*
import dev.slne.surf.api.paper.inventory.framework.view.container.dsl.blockRow
import dev.slne.surf.api.paper.inventory.framework.view.settings.ViewRows
import dev.slne.surf.premium.shop.menu.furniture.furnitureShopView
import org.bukkit.inventory.ItemType

val mainMenu = surfView("PremiumShop") {
    settings {
        rows(ViewRows.THREE)
        navigateBackOnOutsideClick(false)
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
        layoutSlot('F') {
            withItem(ItemType.OAK_SHELF) {
                displayName {
                    primary("Furniture Shop")
                }

                buildLore {
                    emptyLine()
                    line {
                        spacer("Hier kannst du Furniture kaufen,")
                    }
                    line {
                        spacer("mit welcher du deine Base verschönern kannst.")
                    }
                }
            }

            onItemClick {
                openForPlayer(furnitureShopView)
            }
        }
    }
}