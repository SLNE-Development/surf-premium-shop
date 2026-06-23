package dev.slne.surf.premium.shop

import com.github.shynixn.mccoroutine.folia.SuspendingJavaPlugin
import dev.slne.surf.premium.shop.command.premiumShopCommand
import org.bukkit.plugin.java.JavaPlugin

class PremiumShop : SuspendingJavaPlugin() {
    override suspend fun onLoadAsync() {

    }

    override suspend fun onEnableAsync() {
        premiumShopCommand()
    }

    override suspend fun onDisableAsync() {

    }
}

val plugin: PremiumShop get() = JavaPlugin.getPlugin(PremiumShop::class.java)