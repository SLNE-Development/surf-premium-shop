@file:Suppress("UnstableApiUsage")

package dev.slne.surf.premium.shop.menu.furniture

import com.github.shynixn.mccoroutine.folia.launch
import com.github.shynixn.mccoroutine.folia.regionDispatcher
import dev.slne.surf.premium.shop.furniture.item.FurnitureItem
import dev.slne.surf.premium.shop.plugin
import dev.slne.surf.surfapi.bukkit.api.builder.buildLore
import dev.slne.surf.surfapi.bukkit.api.builder.displayName
import dev.slne.surf.surfapi.bukkit.api.inventory.framework.dsl.onItemClick
import dev.slne.surf.surfapi.bukkit.api.inventory.framework.view.*
import dev.slne.surf.surfapi.bukkit.api.inventory.framework.view.container.dsl.blockRow
import dev.slne.surf.surfapi.bukkit.api.inventory.framework.view.icon.ViewIconColor
import dev.slne.surf.surfapi.bukkit.api.inventory.framework.view.icon.ViewIconType
import dev.slne.surf.surfapi.bukkit.api.inventory.framework.view.icon.viewIcon
import dev.slne.surf.surfapi.bukkit.api.inventory.framework.view.settings.ViewRows
import dev.slne.surf.surfapi.bukkit.api.inventory.framework.view.state.get
import dev.slne.surf.surfapi.bukkit.api.inventory.framework.view.state.initialState
import dev.slne.surf.surfapi.bukkit.api.inventory.framework.view.state.mutableState
import dev.slne.surf.surfapi.bukkit.api.inventory.framework.view.state.set
import dev.slne.surf.surfapi.core.api.messages.adventure.playSound
import dev.slne.surf.surfapi.core.api.messages.adventure.sendText
import io.papermc.paper.datacomponent.DataComponentTypes
import net.kyori.adventure.sound.Sound
import org.bukkit.entity.EntityType
import org.bukkit.entity.Item
import org.bukkit.entity.Player
import org.bukkit.event.entity.CreatureSpawnEvent
import org.bukkit.inventory.ItemStack
import kotlin.math.max
import org.bukkit.Sound as BukkitSound

val furnitureItemBuyView = surfView("KAUFEN") {
    val itemStateHolder = initialState<FurnitureItem>()
    val amountStateHolder = mutableState(1)

    settings {
        rows(ViewRows.TWO)
    }

    containerDefaults {
        blockRow(1)
        blockRow(2)
    }

    onInit {
        layout(
            " NM I PO ",
            "    B    "
        )
    }

    onFirstRender {
        val item = itemStateHolder[this]

        layoutSlot(
            'N', buildAmountItem(
                ViewIconType.MINUS,
                -1,
            )
        ).onItemClick {
            amountStateHolder[this] = max(amountStateHolder[this].dec(), 1)
            player.playClickSound()
            update()
        }

        layoutSlot(
            'M', buildAmountItem(
                ViewIconType.MINUS,
                -10,
            )
        ).onItemClick {
            amountStateHolder[this] = max(amountStateHolder[this] - 10, 1)
            player.playClickSound()
            update()
        }

        layoutSlot('I').onRender {
            val amountState = amountStateHolder[this]

            it.item = viewIcon(ViewIconType.QUESTION, ViewIconColor.BLUE) {
                displayName {
                    primary("Anzahl: ")
                    variableValue(amountState)
                }

                buildLore {
                    emptyLine()
                    line {
                        spacer("Bitte wähle aus, wie oft du ")
                        append(item)
                        spacer(" kaufen möchtest.")
                    }
                }
            }
        }

        layoutSlot(
            'P', buildAmountItem(
                ViewIconType.PLUS,
                1,
            )
        ).onItemClick {
            amountStateHolder[this] = amountStateHolder[this].inc()
            player.playClickSound()
            update()
        }

        layoutSlot(
            'O', buildAmountItem(
                ViewIconType.PLUS,
                10,
            )
        ).onItemClick {
            amountStateHolder[this] = amountStateHolder[this] + 10
            player.playClickSound()
            update()
        }

        layoutSlot('B').onRender {
            val amountState = amountStateHolder[this]
            val price = amountState * item.price

            it.item = viewIcon(ViewIconType.CHECK, ViewIconColor.GREEN) {
                displayName {
                    primary("Kaufen")
                }

                buildLore {
                    emptyLine()
                    line {
                        spacer("Klicke, um ")
                        variableValue("${amountState}x")
                        appendSpace()
                        append(item)
                        spacer(" für ")
                        // TODO: Add transaction currency display
                        variableValue("$price CC")
                        spacer(" zu kaufen")
                    }
                }
            }
        }.onItemClick {
            val amount = amountStateHolder[this]
            val price = item.price * amount

            closeForPlayer()

            // Try to remove money from the player via transaction api

            val stacks = splitIntoMultipleItemStacks(item.itemStack, amount)
            val notAdded = stacks.flatMap { stack ->
                player.inventory.addItem(stack).values
            }

            plugin.launch(plugin.regionDispatcher(player.location)) {
                notAdded.forEach { itemStack ->
                    player.world.spawnEntity(
                        player.location,
                        EntityType.ITEM,
                        CreatureSpawnEvent.SpawnReason.CUSTOM
                    ) { item ->
                        require(item is Item)

                        item.itemStack = itemStack
                        item.owner = player.uniqueId
                        item.pickupDelay = 0

                        // 5 minutes - 30 seconds so it despawns after 30 seconds
                        item.ticksLived = 6000 - 600
                    }
                }
            }

            player.playSound(true) {
                type(BukkitSound.ENTITY_PLAYER_LEVELUP)
                volume(.5f)
                source(Sound.Source.PLAYER)
            }

            player.sendText {
                appendSuccessPrefix()
                success("Du hast ")
                variableValue("${amount}x")
                append(item)
                success(" für ")
                // TODO: Add transaction currency display
                variableValue("$price CC")
                success(" gekauft!")
            }
        }
    }
}

private fun splitIntoMultipleItemStacks(itemStack: ItemStack, amount: Int): List<ItemStack> {
    val maxStackSize = itemStack.getData(DataComponentTypes.MAX_STACK_SIZE) ?: 1
    val neededStacks = (amount + maxStackSize - 1) / maxStackSize

    return List(neededStacks) { index ->
        val stackAmount = if (index == neededStacks - 1) {
            amount - (maxStackSize * index)
        } else {
            maxStackSize
        }

        itemStack.clone().asQuantity(stackAmount)
    }
}

private fun buildAmountItem(
    type: ViewIconType,
    amount: Int,
) = viewIcon(type, ViewIconColor.WHITE) {
    val positive = amount > 0

    displayName {
        primary(amount)
    }

    buildLore {
        emptyLine()
        line {
            spacer("Anzahl um ")
            variableValue(amount)

            if (positive) {
                spacer(" erhöhen")
            } else {
                spacer(" verringern")
            }
        }
    }
}

private fun Player.playClickSound() = playSound(true) {
    type(BukkitSound.UI_BUTTON_CLICK)
    volume(.5f)
    source(Sound.Source.UI)
}