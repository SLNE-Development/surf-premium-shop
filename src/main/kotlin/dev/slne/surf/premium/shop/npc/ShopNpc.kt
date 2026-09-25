package dev.slne.surf.premium.shop.npc

import dev.slne.surf.api.paper.inventory.framework.open
import dev.slne.surf.npc.api.SurfNpcApi
import dev.slne.surf.npc.api.dsl.npc
import dev.slne.surf.npc.api.event.NpcInteractEvent
import dev.slne.surf.npc.api.npc.Npc
import dev.slne.surf.npc.api.npc.property.NpcProperty
import dev.slne.surf.npc.api.npc.property.NpcPropertyType
import dev.slne.surf.npc.api.npc.skin.NpcSkin
import dev.slne.surf.premium.shop.menu.mainMenu
import org.bukkit.Location
import org.bukkit.entity.EntityType
import org.bukkit.event.EventHandler
import org.bukkit.event.Listener

object ShopNpc : Listener {
    const val PROPERTY_KEY = "premium_shop"

    fun isShopNpc(npc: Npc) = npc.hasProperty(PROPERTY_KEY)

    fun create(uniqueName: String, location: Location, skin: NpcSkin = NpcSkin.empty()): Npc? {
        if (SurfNpcApi.getNpc(uniqueName) != null) return null

        val npc = npc {
            this.uniqueName = uniqueName
            this.location = location
            this.skin = skin
            type = EntityType.MANNEQUIN
            persistent = true

            displayName {
                primary("Premium Shop")
            }
        }

        npc.addProperty(NpcProperty(PROPERTY_KEY, true, NpcPropertyType.Types.BOOLEAN_TYPE))

        return npc
    }

    @EventHandler
    fun onNpcInteract(event: NpcInteractEvent) {
        if (!isShopNpc(event.npc)) return

        mainMenu.open(event.player)
    }
}
