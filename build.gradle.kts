import dev.slne.surf.api.gradle.util.registerRequired

plugins {
    id("dev.slne.surf.api.gradle.paper-plugin")
}

group = "dev.slne.surf.premium.shop"
version = findProperty("version") as String

dependencies {
    compileOnly(libs.surf.transaction.api)
}

surfPaperPluginApi {
    mainClass("dev.slne.surf.premium.shop.PremiumShop")
    foliaSupported(true)

    serverDependencies {
        registerRequired("surf-transaction-paper")
    }
}