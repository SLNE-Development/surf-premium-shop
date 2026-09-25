package dev.slne.surf.premium.shop.command.subcommands

import dev.jorel.commandapi.CommandAPICommand
import dev.jorel.commandapi.kotlindsl.getValue
import dev.jorel.commandapi.kotlindsl.stringArgument
import dev.jorel.commandapi.kotlindsl.subcommand
import dev.slne.surf.api.core.messages.adventure.sendText
import dev.slne.surf.api.paper.command.executors.playerExecutorSuspend
import dev.slne.surf.npc.api.SurfNpcApi
import dev.slne.surf.npc.api.npc.skin.NpcSkin
import dev.slne.surf.premium.shop.npc.ShopNpc
import dev.slne.surf.premium.shop.utils.PermissionRegistry

fun CommandAPICommand.npcCommand() = subcommand("npc") {
    withPermission(PermissionRegistry.COMMAND_NPC_BASE)

    subcommand("create") {
        withPermission(PermissionRegistry.COMMAND_NPC_CREATE)

        stringArgument("uniqueName")
        stringArgument("skinOwner", optional = true)

        playerExecutorSuspend { player, arguments ->
            val uniqueName: String by arguments
            val skinOwner: String? by arguments

            val location = player.location
            val skin = skinOwner?.let { SurfNpcApi.fetchSkin(it) } ?: NpcSkin.empty()
            val npc = ShopNpc.create(uniqueName, location, skin)

            player.sendText {
                if (npc == null) {
                    appendErrorPrefix()
                    error("Ein Npc mit dem Namen ")
                    variableValue(uniqueName)
                    error(" existiert bereits.")
                } else {
                    appendSuccessPrefix()
                    success("Der Premium Shop Npc ")
                    variableValue(uniqueName)
                    success(" wurde erfolgreich erstellt.")
                }
            }
        }
    }
}
