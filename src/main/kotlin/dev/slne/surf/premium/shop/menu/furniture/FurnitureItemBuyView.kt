@file:Suppress("UnstableApiUsage")

package dev.slne.surf.premium.shop.menu.furniture

import com.github.shynixn.mccoroutine.folia.launch
import dev.slne.surf.api.core.messages.adventure.playSound
import dev.slne.surf.api.core.messages.adventure.sendText
import dev.slne.surf.api.paper.builder.buildLore
import dev.slne.surf.api.paper.builder.displayName
import dev.slne.surf.api.paper.inventory.framework.dsl.layoutSlot
import dev.slne.surf.api.paper.inventory.framework.dsl.onItemClick
import dev.slne.surf.api.paper.inventory.framework.dsl.onItemRender
import dev.slne.surf.api.paper.inventory.framework.view.*
import dev.slne.surf.api.paper.inventory.framework.view.container.dsl.blockRow
import dev.slne.surf.api.paper.inventory.framework.view.icon.ViewIconColor
import dev.slne.surf.api.paper.inventory.framework.view.icon.ViewIconType
import dev.slne.surf.api.paper.inventory.framework.view.icon.viewIcon
import dev.slne.surf.api.paper.inventory.framework.view.settings.ViewRows
import dev.slne.surf.api.paper.inventory.framework.view.state.get
import dev.slne.surf.api.paper.inventory.framework.view.state.initialState
import dev.slne.surf.api.paper.inventory.framework.view.state.mutableState
import dev.slne.surf.api.paper.inventory.framework.view.state.set
import dev.slne.surf.api.paper.util.BukkitSound
import dev.slne.surf.premium.shop.furniture.item.FurnitureItem
import dev.slne.surf.premium.shop.manager.PremiumShopManager
import dev.slne.surf.premium.shop.plugin
import dev.slne.surf.transaction.api.currency.Currency
import net.kyori.adventure.sound.Sound
import org.bukkit.entity.Player
import kotlin.math.max

private val waitingMessages = listOf(
    "Dein Einkauf wird gerade zusammengepackt ... hoffentlich fällt nichts runter.",
    "Die Bestellung ist angekommen! Jetzt muss nur noch jemand das richtige Regal finden.",
    "Kauf wird verarbeitet ... bitte nicht den Stecker ziehen.",
    "Bestellung wird bearbeitet ... unser Hamster im Serverraum gibt alles.",
    "Dein Kauf läuft! Wir zählen gerade noch einmal nach, sicher ist sicher.",
    "Kauf gestartet ... jetzt bloß nichts explodieren lassen."
)

private val successMessages = listOf(
    "Geschafft! Dein Einkauf hat es heil durch die Kasse geschafft.",
    "Zack, gekauft! Das Lager ist jetzt ein bisschen leerer.",
    "Kauf abgeschlossen! Niemand wurde dabei verletzt.",
    "Bestellung erfolgreich! Unser Hamster darf jetzt kurz Pause machen."
)

private val insufficientFundsMessages = listOf(
    "Die Kasse hat nachgezählt ... da fehlt leider noch etwas.",
    "Fast! Dein Geldbeutel ist nur leider anderer Meinung.",
    "Die Kasse piept. Und diesmal leider nicht vor Freude.",
    "Das Lager wäre bereit, aber dein Kontostand noch nicht.",
    "Dein Einkauf wollte schon loslaufen, wurde aber von der Kasse aufgehalten.",
    "Leider zu teuer. Selbst unser Hamster konnte da nichts mehr machen."
)

private val errorMessages = listOf(
    "Da ist uns wohl eine Schraube aus dem Shop gefallen.",
    "Ups! Dein Einkauf ist irgendwo zwischen Kasse und Lager stecken geblieben.",
    "Irgendetwas ist schiefgelaufen. Der Hamster bestreitet jede Beteiligung.",
    "Das war so nicht geplant. Die Technik hatte offenbar andere Ideen.",
    "Der Shop hat gerade kurz den Faden verloren.",
    "Da hat es irgendwo geknirscht. Dein Kauf wurde vorsichtshalber nicht abgeschlossen."
)

val furnitureItemBuyView = surfView("Kaufen") {
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

        layoutSlot('N') {
            withItem(buildAmountItem(ViewIconType.MINUS, -1))

            onItemClick {
                amountStateHolder[this] = max(amountStateHolder[this].dec(), 1)
                player.playClickSound()
                update()
            }
        }

        layoutSlot('M') {
            withItem(buildAmountItem(ViewIconType.MINUS, -10))

            onItemClick {
                amountStateHolder[this] = max(amountStateHolder[this] - 10, 1)
                player.playClickSound()
                update()
            }
        }

        layoutSlot('I') {
            onItemRender {
                val amount = amountStateHolder[this]

                this.item = viewIcon(ViewIconType.QUESTION, ViewIconColor.BLUE) {
                    displayName {
                        primary("Anzahl: ")
                        variableValue(amount)
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
        }

        layoutSlot('P') {
            withItem(buildAmountItem(ViewIconType.PLUS, 1))

            onItemClick {
                amountStateHolder[this] = amountStateHolder[this].inc()
                player.playClickSound()
                update()
            }
        }

        layoutSlot('O') {
            withItem(buildAmountItem(ViewIconType.PLUS, 10))

            onItemClick {
                amountStateHolder[this] = amountStateHolder[this] + 10
                player.playClickSound()
                update()
            }
        }

        layoutSlot('B') {
            onItemRender {
                val amount = amountStateHolder[this]
                val price = amount * item.price

                this.item = viewIcon(ViewIconType.CHECK, ViewIconColor.GREEN) {
                    displayName {
                        primary("Kaufen")
                    }

                    buildLore {
                        emptyLine()
                        line {
                            spacer("Klicke, um ")
                            variableValue("${amount}x")
                            appendSpace()
                            append(item)
                            spacer(" für ")
                            append(Currency.default().format(price.toBigDecimal()))
                            spacer(" zu kaufen")
                        }
                    }
                }
            }

            onItemClick {
                val amount = amountStateHolder[this]
                val price = item.price * amount

                closeForPlayer()

                player.sendText {
                    appendInfoPrefix()
                    info(waitingMessages.random())
                }

                plugin.launch {
                    PremiumShopManager.buy(
                        player,
                        amount,
                        item,
                        onSuccess = {
                            player.sendText {
                                appendSuccessPrefix()
                                success(successMessages.random())

                                appendNewSuccessPrefixedLine()
                                success("Du hast ")
                                variableValue("${amount}x")
                                appendSpace()
                                append(item)
                                success(" für ")
                                append(Currency.default().format(price.toBigDecimal()))
                                success(" gekauft!")
                            }

                            player.playSound(true) {
                                type(BukkitSound.ENTITY_PLAYER_LEVELUP)
                                volume(.5f)
                                source(Sound.Source.PLAYER)
                            }
                        },
                        onInsufficientFunds = {
                            player.sendText {
                                appendWarningPrefix()
                                warning(insufficientFundsMessages.random())

                                appendNewWarningPrefixedLine()
                                warning("Dir fehlen die nötigen ")
                                append(Currency.default().displayName)
                                warning(" für ")
                                variableValue("${amount}x")
                                appendSpace()
                                append(item)
                                warning(" zum Preis von ")
                                append(Currency.default().format(price.toBigDecimal()))
                                warning(".")
                            }

                            player.playSound {
                                type(BukkitSound.ENTITY_VILLAGER_NO)
                                volume(.5f)
                                source(Sound.Source.PLAYER)
                            }
                        },
                        onError = {
                            player.sendText {
                                appendErrorPrefix()
                                error(errorMessages.random())

                                appendNewErrorPrefixedLine()
                                appendSpace()
                                error("Der Kauf von ")
                                variableValue("${amount}x")
                                appendSpace()
                                append(item)
                                error(" für ")
                                append(Currency.default().format(price.toBigDecimal()))
                                error(" konnte nicht abgeschlossen werden.")
                            }


                            player.playSound {
                                type(BukkitSound.ENTITY_VILLAGER_NO)
                                volume(.5f)
                                source(Sound.Source.PLAYER)
                            }
                        }
                    )
                }
            }
        }
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