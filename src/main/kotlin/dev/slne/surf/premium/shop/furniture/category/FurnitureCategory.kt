package dev.slne.surf.premium.shop.furniture.category

import dev.slne.surf.premium.shop.furniture.item.FurnitureItem
import net.kyori.adventure.text.Component
import net.kyori.adventure.text.ComponentLike
import org.bukkit.inventory.ItemStack
import org.spongepowered.configurate.objectmapping.ConfigSerializable

@ConfigSerializable
data class FurnitureCategory(
    val name: String,
    val sortingIndex: Int,
    val displayName: Component,
    val displayItem: ItemStack,
    val items: MutableList<FurnitureItem>,
    val enabled: Boolean = true,
) : ComponentLike, Comparable<FurnitureCategory> {
    override fun asComponent() = displayName
    
    override fun compareTo(other: FurnitureCategory): Int {
        return sortingIndex.compareTo(other.sortingIndex)
    }
}
