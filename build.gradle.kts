import dev.slne.surf.api.gradle.util.registerRequired

plugins {
    id("dev.slne.surf.api.gradle.paper-plugin")
}

group = "dev.slne.surf.premium.shop"
version = findProperty("version") as String

dependencies {
    compileOnly(libs.surf.transaction.api)
    compileOnly(libs.surf.npc.api)
}

surfPaperPluginApi {
    mainClass("dev.slne.surf.premium.shop.PremiumShop")

    serverDependencies {
        registerRequired("surf-transaction-paper")
        registerRequired("surf-npc-paper")
    }
}