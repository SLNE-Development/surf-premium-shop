package dev.slne.surf.premium.shop.furniture.item

import net.kyori.adventure.text.Component
import net.kyori.adventure.text.ComponentLike
import org.bukkit.inventory.ItemStack
import org.spongepowered.configurate.objectmapping.ConfigSerializable

@ConfigSerializable
data class FurnitureItem(
    val name: String,
    val displayName: Component,
    val sortingIndex: Int,
    val itemStack: ItemStack,
    val price: Int,
    val enabled: Boolean = true,
) : ComponentLike, Comparable<FurnitureItem> {
    override fun asComponent() = displayName

    override fun compareTo(other: FurnitureItem): Int {
        return sortingIndex.compareTo(other.sortingIndex)
    }
}
