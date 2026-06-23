@file:Suppress("UnstableApiUsage")
@file:OptIn(NmsUseWithCaution::class)

package dev.slne.surf.premium.shop.dialog

import com.github.shynixn.mccoroutine.folia.entityDispatcher
import com.github.shynixn.mccoroutine.folia.launch
import com.github.shynixn.mccoroutine.folia.regionDispatcher
import dev.slne.surf.api.core.messages.adventure.playSound
import dev.slne.surf.api.core.messages.adventure.sendText
import dev.slne.surf.api.core.messages.adventure.text
import dev.slne.surf.api.paper.dialog.base
import dev.slne.surf.api.paper.dialog.clearDialogs
import dev.slne.surf.api.paper.dialog.dialog
import dev.slne.surf.api.paper.dialog.type
import dev.slne.surf.api.paper.nms.NmsUseWithCaution
import dev.slne.surf.premium.shop.furniture.category.FurnitureCategory
import dev.slne.surf.premium.shop.furniture.item.FurnitureItem
import dev.slne.surf.premium.shop.plugin
import dev.slne.surf.transaction.api.currency.Currency
import dev.slne.surf.transaction.api.user.transactionUser
import io.papermc.paper.datacomponent.DataComponentTypes
import io.papermc.paper.dialog.Dialog
import io.papermc.paper.registry.data.dialog.DialogBase
import kotlinx.coroutines.withContext
import net.kyori.adventure.sound.Sound
import org.bukkit.entity.Player
import org.bukkit.inventory.ItemStack
import org.bukkit.Sound as BukkitSound

private val castCoin get() = Currency.default()

object ItemDialog {
    fun create(
        player: Player,
        category: FurnitureCategory,
        item: FurnitureItem,
        amount: Int = 1
    ): Dialog = dialog {
        val totalPrice = item.price * amount

        base {
            title(MainDialog.buildTitle(category, item))
            afterAction(DialogBase.DialogAfterAction.WAIT_FOR_RESPONSE)

            body {
                item {
                    item(item.itemStack)
                }

                plainMessage {
                    info("Du möchtest ")
                    variableValue("${amount}x")
                    appendSpace()
                    append(item)
                    appendSpace()
                    info("für")
                    appendSpace()
                    append(castCoin.format(totalPrice.toBigDecimal()))
                    appendSpace()
                    info("kaufen?")
                }

                plainMessage {
                    append {
                        error("-10")
                        clickCallback {
                            it.showDialog(
                                create(
                                    player = player,
                                    category = category,
                                    item = item,
                                    amount = (amount - 10).coerceAtLeast(1)
                                )
                            )
                        }
                    }

                    appendSpace()
                    appendSpace()
                    appendSpace()

                    append {
                        error("-1")

                        clickCallback {
                            it.showDialog(
                                create(
                                    player = player,
                                    category = category,
                                    item = item,
                                    amount = (amount - 1).coerceAtLeast(1)
                                )
                            )
                        }
                    }

                    appendSpace()
                    appendSpace()
                    appendSpace()

                    append {
                        variableValue("${amount.coerceAtLeast(1)}x")
                    }

                    appendSpace()
                    appendSpace()
                    appendSpace()

                    append {
                        success("+1")

                        clickCallback {
                            it.showDialog(
                                create(
                                    player = player,
                                    category = category,
                                    item = item,
                                    amount = amount + 1
                                )
                            )
                        }
                    }

                    appendSpace()
                    appendSpace()
                    appendSpace()

                    append {
                        success("+10")

                        clickCallback {
                            it.showDialog(
                                create(
                                    player = player,
                                    category = category,
                                    item = item,
                                    amount = amount + 10
                                )
                            )
                        }
                    }
                }
            }
        }

        type {
            confirmation {
                yes {
                    label {
                        success("Kaufen")
                    }

                    tooltip {
                        success("Kaufe das Möbelstück")
                        appendSpace()
                        variableValue("${amount}x")
                        appendSpace()
                        append(item)
                        appendSpace()
                        success("für")
                        appendSpace()
                        append(castCoin.format(totalPrice.toBigDecimal()))
                    }

                    action {
                        playerCallback { player ->
                            val transactionUser = player.transactionUser()

                            plugin.launch {
                                val result = transactionUser.withdraw(
                                    totalPrice.toBigDecimal(),
                                    castCoin
                                )

                                if (result.success) {
                                    val itemStacks = calculateItemStacks(item.itemStack, amount)

                                    withContext(plugin.entityDispatcher(player)) {
                                        val notAdded = itemStacks.flatMap { itemStack ->
                                            player.inventory.addItem(itemStack).values
                                        }

                                        if (notAdded.isNotEmpty()) {
                                            withContext(plugin.regionDispatcher(player.location)) {
                                                notAdded.forEach { na ->
                                                    player.world.dropItem(
                                                        player.location,
                                                        na
                                                    ) { dropped ->
                                                        dropped.owner = player.uniqueId
                                                        dropped.isUnlimitedLifetime = true
                                                    }
                                                }
                                            }
                                        }
                                    }

                                    player.sendText {
                                        appendSuccessPrefix()
                                        success("Du hast ")
                                        variableValue("${amount}x")
                                        appendSpace()
                                        append(item)
                                        appendSpace()
                                        success("für")
                                        appendSpace()
                                        append(castCoin.format(totalPrice.toBigDecimal()))
                                        appendSpace()
                                        success("erfolgreich gekauft!")
                                    }

                                    player.playSound {
                                        type(BukkitSound.ENTITY_PLAYER_LEVELUP)
                                        source(Sound.Source.MASTER)
                                        volume(.5f)
                                    }

                                    player.clearDialogs(true)

                                    return@launch
                                }

                                // print error message to user
                            }
                        }
                    }
                }

                no {
                    label(text("Zurück"))
                    tooltip(text("Kehre zur Kategorie zurück"))

                    action {
                        playerCallback {
                            it.showDialog(CategoryDialog.create(player, category))
                        }
                    }
                }
            }
        }
    }

    private fun calculateItemStacks(item: ItemStack, amount: Int): List<ItemStack> {
        val maxStackSize = item.getData(DataComponentTypes.MAX_STACK_SIZE) ?: 1
        val fullStacks = amount / maxStackSize
        val remainder = amount % maxStackSize

        val itemStacks = mutableListOf<ItemStack>()

        repeat(fullStacks) {
            itemStacks.add(item.clone().apply { setAmount(maxStackSize) })
        }

        if (remainder > 0) {
            itemStacks.add(item.clone().apply { setAmount(remainder) })
        }

        return itemStacks
    }
}