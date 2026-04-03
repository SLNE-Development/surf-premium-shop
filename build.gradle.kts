plugins {
    id("dev.slne.surf.surfapi.gradle.paper-plugin")
}

group = "dev.slne.surf"
version = findProperty("version") as String

surfPaperPluginApi {
    mainClass("dev.slne.surf.premium.shop.PremiumShop")
}