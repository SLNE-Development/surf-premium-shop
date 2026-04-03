package dev.slne.surf.premium.shop.furniture.category

import dev.slne.surf.premium.shop.furniture.item.FurnitureItem
import kotlinx.serialization.Transient
import net.kyori.adventure.text.ComponentLike
import net.kyori.adventure.text.minimessage.MiniMessage
import org.spongepowered.configurate.objectmapping.ConfigSerializable
import java.util.*

@ConfigSerializable
data class FurnitureCategory(
    val name: String,
    private val itemDisplayName: String,
    val items: LinkedList<FurnitureItem>,
    val enabled: Boolean = true,
) : ComponentLike {
    @Transient
    val displayName = MiniMessage.miniMessage().deserialize(itemDisplayName)
    override fun asComponent() = displayName
}
