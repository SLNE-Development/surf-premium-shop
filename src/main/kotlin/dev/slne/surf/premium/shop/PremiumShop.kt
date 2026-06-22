package dev.slne.surf.premium.shop

import com.github.shynixn.mccoroutine.folia.SuspendingJavaPlugin
import dev.slne.surf.api.paper.inventory.framework.register
import dev.slne.surf.premium.shop.command.premiumShopCommand
import dev.slne.surf.premium.shop.menu.furniture.furnitureItemBuyView
import dev.slne.surf.premium.shop.menu.furniture.furnitureItemView
import dev.slne.surf.premium.shop.menu.furniture.furnitureShopView
import dev.slne.surf.premium.shop.menu.mainMenu
import org.bukkit.plugin.java.JavaPlugin

class PremiumShop : SuspendingJavaPlugin() {
    override suspend fun onLoadAsync() {
        mainMenu.register()
        furnitureShopView.register()
        furnitureItemView.register()
        furnitureItemBuyView.register()
    }

    override suspend fun onEnableAsync() {
        premiumShopCommand()
    }

    override suspend fun onDisableAsync() {

    }
}

val plugin: PremiumShop get() = JavaPlugin.getPlugin(PremiumShop::class.java)