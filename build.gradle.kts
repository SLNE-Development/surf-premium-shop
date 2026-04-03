import dev.slne.surf.surfapi.gradle.util.registerRequired

plugins {
    id("dev.slne.surf.surfapi.gradle.paper-plugin")
}

group = "dev.slne.surf"
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