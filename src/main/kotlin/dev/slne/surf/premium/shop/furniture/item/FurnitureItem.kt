package dev.slne.surf.premium.shop.furniture.item

import dev.slne.surf.api.core.messages.Colors
import dev.slne.surf.premium.shop.config.furniture.ConfigFurnitureItem
import net.kyori.adventure.text.Component
import net.kyori.adventure.text.ComponentLike
import org.bukkit.inventory.ItemStack

data class FurnitureItem(
    val id: String,
    val displayName: Component,
    val sortingIndex: Int,
    private val itemStack: ItemStack,
    val price: Int,
    val permission: String,
    val enabled: Boolean,
) : ComponentLike, Comparable<FurnitureItem> {
    val itemStackTemplate get() = itemStack.clone()

    override fun asComponent() = displayName.colorIfAbsent(Colors.VARIABLE_VALUE)

    override fun compareTo(other: FurnitureItem): Int {
        return sortingIndex.compareTo(other.sortingIndex)
    }

    fun toConfig() = ConfigFurnitureItem(
        id = id,
        displayName = displayName,
        sortingIndex = sortingIndex,
        itemStack = itemStack,
        price = price,
        permission = permission,
        enabled = enabled,
    )

    companion object {
        fun fromConfig(config: ConfigFurnitureItem) = FurnitureItem(
            id = config.id,
            displayName = config.displayName,
            sortingIndex = config.sortingIndex,
            itemStack = config.itemStack,
            price = config.price,
            permission = config.permission,
            enabled = config.enabled,
        )
    }
}
