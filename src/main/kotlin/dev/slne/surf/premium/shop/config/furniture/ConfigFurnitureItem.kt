package dev.slne.surf.premium.shop.config.furniture

import dev.slne.surf.api.core.config.constraints.NotBlank
import dev.slne.surf.api.core.config.constraints.PositiveNumber
import dev.slne.surf.api.core.messages.adventure.text
import net.kyori.adventure.text.Component
import org.bukkit.inventory.ItemStack
import org.bukkit.inventory.ItemType
import org.spongepowered.configurate.objectmapping.ConfigSerializable
import org.spongepowered.configurate.objectmapping.meta.Required

@ConfigSerializable
data class ConfigFurnitureItem(
    @Required
    @NotBlank
    val id: String,

    val displayName: Component = text(id),

    val sortingIndex: Int = 0,

    val itemStack: ItemStack = ItemType.STONE.createItemStack(),

    @PositiveNumber
    val price: Int = 0,

    val permission: String = "",

    val enabled: Boolean = true,
)