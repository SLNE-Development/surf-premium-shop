package dev.slne.surf.premium.shop.config.furniture

import dev.slne.surf.api.core.config.constraints.NotBlank
import dev.slne.surf.api.core.config.constraints.PositiveNumber
import dev.slne.surf.api.core.messages.adventure.text
import net.kyori.adventure.text.Component
import org.bukkit.inventory.ItemStack
import org.bukkit.inventory.ItemType
import org.spongepowered.configurate.objectmapping.ConfigSerializable
import org.spongepowered.configurate.objectmapping.meta.Comment
import org.spongepowered.configurate.objectmapping.meta.Required

@ConfigSerializable
data class ConfigFurnitureItem(
    @Comment("Unique identifier for this furniture item.")
    @Required
    @NotBlank
    val id: String,

    @Comment("The display name of this item in MiniMessage format.")
    val displayName: Component = text(id),

    @Comment("The order in which this item is displayed within its category. Lower numbers appear first.")
    val sortingIndex: Int = 0,

    @Comment("The item stack that will be given to the player when purchasing this item.")
    val itemStack: ItemStack = ItemType.STONE.createItemStack(),

    @Comment("The price of a single item.")
    @PositiveNumber
    val price: Int = 100,

    @Comment("The permission required to purchase this item.")
    val permission: String = "",

    @Comment("Whether this item is enabled and visible in the shop.")
    val enabled: Boolean = true,
)