@file:Suppress("UnstableApiUsage")

package dev.slne.surf.premium.shop.dialog

import dev.slne.surf.api.paper.dialog.base
import dev.slne.surf.api.paper.dialog.builder.DialogBodyBuilder
import dev.slne.surf.api.paper.dialog.dialog
import dev.slne.surf.api.paper.dialog.type
import dev.slne.surf.premium.shop.furniture.category.FurnitureCategory
import dev.slne.surf.premium.shop.furniture.item.FurnitureItem
import io.papermc.paper.dialog.Dialog
import io.papermc.paper.registry.data.dialog.DialogBase
import net.kyori.adventure.text.Component.text
import org.bukkit.entity.Player

object CategoryDialog {
    fun create(
        player: Player,
        category: FurnitureCategory
    ): Dialog = dialog {
        val items = category.items
            .filter { it.enabled }
            .filter { player.hasPermission(it.permission) }
            .sortedByDescending { it.name }
            .sortedByDescending { it.sortingIndex }

        base {
            title(MainDialog.buildTitle(category))
            afterAction(DialogBase.DialogAfterAction.NONE)

            body {
                plainMessage {
                    if (items.isEmpty()) {
                        info("Es sind keine Möbelstücke in dieser Kategorie verfügbar.")
                    } else {
                        info("Wähle ein Möbelstück aus, um es zu kaufen.")

                    }
                }

                items.forEach { item ->
                    buildItem(player, category, item)
                }
            }
        }

        type {
            notice {
                label(text("Hauptmenü"))
                tooltip(text("Kehre zum Hauptmenü zurück"))

                action {
                    playerCallback {
                        it.showDialog(MainDialog.create(player))
                    }
                }
            }
        }
    }

    private fun DialogBodyBuilder.buildItem(
        player: Player,
        category: FurnitureCategory,
        item: FurnitureItem
    ) = item {
        item(item.itemStack)
        showTooltip(true)
        showDecorations(true)

        description {
            message {
                append(item)

                clickCallback {
                    it.showDialog(ItemDialog.create(player, category, item))
                }
            }
        }
    }
}