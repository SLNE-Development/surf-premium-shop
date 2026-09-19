package dev.slne.surf.premium.shop.config.furniture

import dev.slne.surf.api.core.config.constraints.NotBlank
import dev.slne.surf.api.core.messages.adventure.text
import dev.slne.surf.premium.shop.utils.PermissionRegistry
import net.kyori.adventure.text.Component
import org.bukkit.inventory.ItemType
import org.spongepowered.configurate.objectmapping.ConfigSerializable
import org.spongepowered.configurate.objectmapping.meta.Comment

@ConfigSerializable
data class ConfigFurnitureCategory(
    @Comment("Unique identifier for the category. This is used for permissions and internal references.")
    @NotBlank
    var id: String = "id",

    @Comment("Whether this category is enabled and visible in the shop. Set to false to hide the category.")
    var enabled: Boolean = true,

    @Comment("The order in which this category will be displayed in the shop. Lower numbers appear first.")
    var sortingIndex: Int = 0,

    @Comment("The display name of the category as it will appear in the shop in MiniMessage format.")
    var displayName: Component = text(id),

    @Comment("The item type that will be used to represent this category in the shop. This should be a valid item type.")
    var displayItem: ItemType = ItemType.STONE,

    @Comment("The permission required to use this category.")
    var permission: String = PermissionRegistry.createCategoryUsePermission(id),

    @Comment("The items that belong to this category")
    var items: List<ConfigFurnitureItem> = listOf()
)