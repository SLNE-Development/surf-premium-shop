package dev.slne.surf.premium.shop.config

import dev.slne.surf.premium.shop.furniture.category.FurnitureCategory
import dev.slne.surf.premium.shop.plugin
import dev.slne.surf.surfapi.core.api.config.SpongeYmlConfigClass
import org.spongepowered.configurate.objectmapping.ConfigSerializable

@ConfigSerializable
data class PremiumShopConfig(
    var enabled: Boolean,
    val furnitureShopCategories: MutableList<FurnitureCategory>
) {
    fun addFurnitureCategory(category: FurnitureCategory) {
        furnitureShopCategories.add(category)
    }

    fun removeFurnitureCategory(category: FurnitureCategory) {
        furnitureShopCategories.remove(category)
    }

    fun furnitureCategoryByName(name: String): FurnitureCategory? {
        return furnitureShopCategories.firstOrNull { it.name.equals(name, true) }
    }

    companion object : SpongeYmlConfigClass<PremiumShopConfig>(
        PremiumShopConfig::class.java,
        plugin.dataPath,
        "config.yml"
    )
}

val config get() = PremiumShopConfig.getConfig()