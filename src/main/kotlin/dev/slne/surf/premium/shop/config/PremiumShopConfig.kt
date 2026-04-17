package dev.slne.surf.premium.shop.config

import dev.slne.surf.premium.shop.furniture.FurnitureShopConfig
import dev.slne.surf.premium.shop.plugin
import dev.slne.surf.api.core.config.SpongeYmlConfigClass
import org.spongepowered.configurate.objectmapping.ConfigSerializable

@ConfigSerializable
data class PremiumShopConfig(
    var enabled: Boolean = true,
    val furniture: FurnitureShopConfig
) {
    companion object : SpongeYmlConfigClass<PremiumShopConfig>(
        PremiumShopConfig::class.java,
        plugin.dataPath,
        "config.yml"
    )
}

val config get() = PremiumShopConfig.getConfig()