@file:Suppress("UnstableApiUsage")

package dev.slne.surf.premium.shop.furniture.category

import dev.slne.surf.premium.shop.config.config
import dev.slne.surf.premium.shop.utils.PermissionRegistry
import dev.slne.surf.surfapi.core.api.messages.Colors
import dev.slne.surf.surfapi.core.api.messages.adventure.key
import kotlinx.serialization.Transient
import net.kyori.adventure.text.Component
import net.kyori.adventure.text.ComponentLike
import org.bukkit.Material
import org.bukkit.Registry
import org.bukkit.inventory.ItemType
import org.spongepowered.configurate.objectmapping.ConfigSerializable

@ConfigSerializable
data class FurnitureCategory(
    val name: String = "",
    val sortingIndex: Int = 0,
    val displayName: Component = Component.empty(),
    val displayItemKey: String = Material.STONE.key().asString(),
    val permission: String = PermissionRegistry.createCategoryUsePermission(name),
    var enabled: Boolean = true,
) : ComponentLike, Comparable<FurnitureCategory> {
    override fun asComponent() = displayName.colorIfAbsent(Colors.VARIABLE_VALUE)

    @Transient
    val items get() = config.furniture.itemsByCategoryName(name)

    @Transient
    val displayItemType = Registry.ITEM.get(key(displayItemKey)) ?: ItemType.STONE

    override fun compareTo(other: FurnitureCategory): Int {
        return sortingIndex.compareTo(other.sortingIndex)
    }
}
