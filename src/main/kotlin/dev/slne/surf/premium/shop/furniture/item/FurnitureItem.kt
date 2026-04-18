package dev.slne.surf.premium.shop.furniture.item

import dev.slne.surf.premium.shop.config.config
import dev.slne.surf.premium.shop.utils.PermissionRegistry
import dev.slne.surf.api.core.messages.Colors
import kotlinx.serialization.Transient
import net.kyori.adventure.text.Component
import net.kyori.adventure.text.ComponentLike
import org.bukkit.Material
import org.bukkit.inventory.ItemStack
import org.spongepowered.configurate.objectmapping.ConfigSerializable

@ConfigSerializable
data class FurnitureItem(
    val name: String = "",
    val displayName: Component = Component.empty(),
    val sortingIndex: Int = 0,
    val itemStack: ItemStack = ItemStack(Material.STONE),
    val price: Int = 0,
    val categoryName: String,
    val permission: String = PermissionRegistry.createFurnitureItemUsePermission(
        categoryName, name
    ),
    var enabled: Boolean = true,
) : ComponentLike, Comparable<FurnitureItem> {
    @Transient
    val category get() = config.furniture.categoryByName(categoryName)

    override fun asComponent() = displayName.colorIfAbsent(Colors.VARIABLE_VALUE)

    override fun compareTo(other: FurnitureItem): Int {
        return sortingIndex.compareTo(other.sortingIndex)
    }
}
