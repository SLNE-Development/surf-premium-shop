package dev.slne.surf.premium.shop.config

import dev.jorel.commandapi.CommandAPIPaper
import dev.slne.surf.api.core.config.manager.SpongeConfigManager
import dev.slne.surf.premium.shop.config.furniture.ConfigFurnitureCategory
import dev.slne.surf.premium.shop.plugin
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.asFlow
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.toList
import kotlinx.coroutines.withContext
import kotlin.io.path.*

object PremiumShopConfigManager {

    private val categoriesPath = plugin.dataPath / "categories"

    suspend fun loadAllCategories(): List<ConfigFurnitureCategory> {
        if (!categoriesPath.exists()) return emptyList()
        if (!categoriesPath.isDirectory()) return emptyList()

        return categoriesPath.useDirectoryEntries("*.yml") { entries ->
            entries.filter { it.isRegularFile() }
                .asFlow()
                .flowOn(Dispatchers.IO)
                .map { path ->
                    SpongeConfigManager.yaml(
                        ConfigFurnitureCategory::class.java,
                        path.parent,
                        path.fileName.toString(),
                    ).config
                }
                .toList()
        }
    }

    suspend fun saveCategory(category: ConfigFurnitureCategory) {
        val categoryFile = categoriesPath / "${category.id}.yml"

        withContext(Dispatchers.IO) {
            if (!categoryFile.exists()) {
                categoryFile.createParentDirectories()
                categoryFile.createFile()
            }

            val manager = SpongeConfigManager.yaml(
                ConfigFurnitureCategory::class.java,
                categoryFile.parent,
                categoryFile.fileName.toString(),
            )

            manager.edit(save = true) {
                id = category.id
                enabled = category.enabled
                sortingIndex = category.sortingIndex
                displayName = category.displayName
                displayItem = category.displayItem
                permission = category.permission
                items = category.items
            }
        }
    }

    suspend fun deleteCategory(id: String) {
        val categoryFile = categoriesPath / "$id.yml"
        withContext(Dispatchers.IO) {
            categoryFile.deleteIfExists()
        }
    }
}