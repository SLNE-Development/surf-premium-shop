package dev.slne.surf.premium.shop.menu.furniture

import dev.slne.surf.premium.shop.config.config
import dev.slne.surf.premium.shop.menu.ShopView
import dev.slne.surf.surfapi.bukkit.api.builder.buildItem
import dev.slne.surf.surfapi.bukkit.api.builder.buildLore
import dev.slne.surf.surfapi.bukkit.api.builder.displayName
import dev.slne.surf.surfapi.bukkit.api.inventory.framework.dsl.onItemClick
import dev.slne.surf.surfapi.bukkit.api.inventory.framework.dsl.openForPlayer
import dev.slne.surf.surfapi.bukkit.api.inventory.framework.view.icon.ViewIconColor
import dev.slne.surf.surfapi.bukkit.api.inventory.framework.view.icon.ViewIconType
import dev.slne.surf.surfapi.bukkit.api.inventory.framework.view.icon.viewIcon
import dev.slne.surf.surfapi.bukkit.api.inventory.framework.view.layoutTarget
import dev.slne.surf.surfapi.bukkit.api.inventory.framework.view.onFirstRender
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
            lazySource { context ->
                config.furniture.categories
                    .filter { it.enabled }
                    .filter { context.player.hasPermission(it.permission) }
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

        onFirstRender {
            slot(1, 5, viewIcon(ViewIconType.EXCLAMATION_MARK, ViewIconColor.YELLOW) {
                displayName {
                    primary("Du brauchst mehr Models?")
                }

                buildLore {
                    emptyLine()
                    line {
                        spacer("Wir beziehen unsere Furniture Models")
                    }
                    line {
                        spacer("aus Quellen, wie bspw. ")
                        variableValue("McModels")
                        spacer(" oder ")
                        variableValue("BuiltByBit")
                        spacer(".")
                    }
                    emptyLine()
                    line {
                        spacer("Wir verwenden das Plugin ")
                        variableValue("Nexo")
                        spacer(", um Furniture anzuzeigen.")
                    }
                    line {
                        spacer("Daher können wir nur Models anbieten, welche Configs")
                    }
                    line {
                        spacer("von Nexo beinhalten. Dies ist immer auf den Seiten beschrieben.")
                    }
                    emptyLine()
                    line {
                        spacer("Sollte dir eine Kollektion gefallen, melde dich gerne")
                    }
                    line {
                        spacer("in einem ")
                        variableValue("survival-ticket")
                        spacer(" auf dem Discord, sodass wir uns")
                    }
                    line {
                        spacer("jene ansehen können und entscheiden,")
                    }
                    line {
                        spacer("ob diese es auf den Server schafft!")
                    }
                }
            })
        }

        settings {
            paginationViewRows(PaginationViewRows.THREE)
        }
    }
}

val furnitureShopView = FurnitureShopView.buildView()