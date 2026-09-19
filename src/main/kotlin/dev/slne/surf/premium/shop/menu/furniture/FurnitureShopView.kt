package dev.slne.surf.premium.shop.menu.furniture

import dev.slne.surf.api.paper.builder.buildLore
import dev.slne.surf.api.paper.builder.displayName
import dev.slne.surf.api.paper.inventory.framework.dsl.onItemClick
import dev.slne.surf.api.paper.inventory.framework.dsl.openForPlayer
import dev.slne.surf.api.paper.inventory.framework.dsl.withItem
import dev.slne.surf.api.paper.inventory.framework.view.icon.ViewIconColor
import dev.slne.surf.api.paper.inventory.framework.view.icon.ViewIconType
import dev.slne.surf.api.paper.inventory.framework.view.icon.viewIcon
import dev.slne.surf.api.paper.inventory.framework.view.layoutTarget
import dev.slne.surf.api.paper.inventory.framework.view.onFirstRender
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