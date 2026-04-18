package dev.slne.surf.premium.shop.command.subcommands

import dev.jorel.commandapi.CommandAPICommand
import dev.jorel.commandapi.kotlindsl.anyExecutor
import dev.jorel.commandapi.kotlindsl.subcommand
import dev.slne.surf.premium.shop.config.PremiumShopConfig
import dev.slne.surf.premium.shop.utils.PermissionRegistry
import dev.slne.surf.api.core.messages.adventure.sendText

fun CommandAPICommand.reloadCommand() = subcommand("reload") {
    withPermission(PermissionRegistry.COMMAND_RELOAD)

    anyExecutor { sender, _ ->
        PremiumShopConfig.reloadFromFile()

        sender.sendText {
            appendSuccessPrefix()

            success("Die Konfiguration wurde erfolgreich neu geladen.")
        }
    }
}