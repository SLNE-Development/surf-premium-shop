package dev.slne.surf.premium.shop.menu

import dev.slne.surf.api.core.messages.builder.SurfComponentBuilder
import dev.slne.surf.api.paper.builder.LoreBuilder
import dev.slne.surf.api.paper.inventory.framework.view.AbstractSurfView

abstract class ShopView(
    itemDisplayName: SurfComponentBuilder.() -> Unit,
    itemLore: LoreBuilder.() -> Unit
) {
    val itemDisplayName = SurfComponentBuilder.builder().apply(itemDisplayName).build()
    val itemLore = LoreBuilder().apply(itemLore).build()

    abstract fun buildView(): AbstractSurfView
}