package dev.slne.surf.premium.shop.furniture.item

import kotlinx.serialization.Transient
import net.kyori.adventure.text.ComponentLike
import net.kyori.adventure.text.minimessage.MiniMessage
import org.bukkit.inventory.ItemStack
import org.spongepowered.configurate.objectmapping.ConfigSerializable

@ConfigSerializable
data class FurnitureItem(
    val name: String,
    private val itemDisplayName: String,
    val description: String?,
    val price: Int,
    val itemStack: ItemStack,
    val enabled: Boolean = true,
) : ComponentLike {
    @Transient
    val displayName = MiniMessage.miniMessage().deserialize(itemDisplayName)
    override fun asComponent() = displayName
}
