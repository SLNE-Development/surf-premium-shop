@file:Suppress("UnstableApiUsage")

package dev.slne.surf.premium.shop.furniture.category

import dev.slne.surf.api.core.messages.Colors
import dev.slne.surf.api.core.util.freeze
import dev.slne.surf.premium.shop.config.furniture.ConfigFurnitureCategory
import dev.slne.surf.premium.shop.furniture.item.FurnitureItem
import it.unimi.dsi.fastutil.objects.Object2ObjectOpenHashMap
import it.unimi.dsi.fastutil.objects.ObjectImmutableList
import net.kyori.adventure.text.Component
import net.kyori.adventure.text.ComponentLike
import org.bukkit.inventory.ItemType

class FurnitureCategory(
    val id: String,
    val enabled: Boolean,
    val sortingIndex: Int,
    val displayName: Component,
    val displayItem: ItemType,
    val permission: String,
    items: List<FurnitureItem> = emptyList(),
) : ComponentLike, Comparable<FurnitureCategory> {
    val items: List<FurnitureItem> = ObjectImmutableList(items.sorted())
    val enabledItems: List<FurnitureItem> = ObjectImmutableList(this.items.filter { it.enabled })

    private val itemById =
        Object2ObjectOpenHashMap(items.associateBy { it.id.lowercase() }).freeze()

    fun itemById(id: String) = itemById[id.lowercase()]

    override fun asComponent() = displayName.colorIfAbsent(Colors.VARIABLE_VALUE)

    override fun compareTo(other: FurnitureCategory): Int {
        return sortingIndex.compareTo(other.sortingIndex)
    }

    fun toConfig() = ConfigFurnitureCategory(
        id = id,
        enabled = enabled,
        sortingIndex = sortingIndex,
        displayName = displayName,
        displayItem = displayItem,
        permission = permission,
        items = items.map(FurnitureItem::toConfig),
    )

    companion object {
        fun fromConfig(config: ConfigFurnitureCategory) = FurnitureCategory(
            id = config.id,
            enabled = config.enabled,
            sortingIndex = config.sortingIndex,
            displayName = config.displayName,
            displayItem = config.displayItem,
            permission = config.permission,
            items = config.items.map(FurnitureItem::fromConfig)
        )
    }
}
