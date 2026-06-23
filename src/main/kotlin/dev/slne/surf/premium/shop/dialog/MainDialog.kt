@file:Suppress("UnstableApiUsage")

package dev.slne.surf.premium.shop.dialog

import dev.slne.surf.api.core.messages.adventure.buildText
import dev.slne.surf.api.core.messages.adventure.text
import dev.slne.surf.api.paper.dialog.base
import dev.slne.surf.api.paper.dialog.builder.actionButton
import dev.slne.surf.api.paper.dialog.dialog
import dev.slne.surf.api.paper.dialog.type
import dev.slne.surf.premium.shop.config.PremiumShopConfig
import dev.slne.surf.premium.shop.furniture.category.FurnitureCategory
import dev.slne.surf.premium.shop.furniture.item.FurnitureItem
import io.papermc.paper.dialog.Dialog
import io.papermc.paper.registry.data.dialog.ActionButton
import io.papermc.paper.registry.data.dialog.DialogBase
import org.bukkit.entity.Player

object MainDialog {
    fun create(
        player: Player
    ): Dialog = dialog {
        val categories = PremiumShopConfig.getConfig().furniture.categories
            .filter { it.enabled }
            .filter { player.hasPermission(it.permission) }
            .sortedByDescending { it.name }
            .sortedByDescending { it.sortingIndex }

        base {
            title(buildTitle())
            afterAction(DialogBase.DialogAfterAction.NONE)

            body {
                plainMessage {
                    if (categories.isEmpty()) {
                        info("Es sind keine Kategorien verfügbar.")
                    } else {
                        info("Wähle eine Kategorie aus, um die darin enthaltenen Möbelstücke zu sehen.")
                    }
                }
            }
        }

        type {
            if (categories.isEmpty()) {
                notice {
                    label(text("Schließen"))
                    tooltip(text("Schließt das Menü"))

                    action {
                        playerCallback {
                            it.closeDialog()
                        }
                    }
                }
            } else {
                multiAction {
                    columns(2)

                    categories.forEach { category ->
                        action(buildCategoryAction(player, category))
                    }

                    exitAction {
                        label(text("Schließen"))
                        tooltip(text("Schließt das Menü"))

                        action {
                            playerCallback {
                                it.closeDialog()
                            }
                        }
                    }
                }
            }
        }
    }

    fun buildTitle(
        category: FurnitureCategory? = null,
        item: FurnitureItem? = null
    ) = buildText {
        primary("Premium Shop")

        if (category != null) {
            appendSpace()
            text("-")
            appendSpace()
            append(category)
        }

        if (item != null) {
            appendSpace()
            text("-")
            appendSpace()
            append(item)
        }
    }

    private fun buildCategoryAction(
        player: Player,
        category: FurnitureCategory
    ): ActionButton = actionButton {
        label(category.displayName)
        tooltip {
            text("Öffnet die Kategorie")
            appendSpace()
            append(category)
        }

        action {
            playerCallback {
                it.showDialog(CategoryDialog.create(player, category))
            }
        }
    }
}