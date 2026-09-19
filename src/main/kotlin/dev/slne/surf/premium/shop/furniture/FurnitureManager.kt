package dev.slne.surf.premium.shop.furniture

import dev.slne.surf.api.core.util.freeze
import dev.slne.surf.premium.shop.config.PremiumShopConfigManager
import dev.slne.surf.premium.shop.furniture.category.FurnitureCategory
import dev.slne.surf.premium.shop.furniture.item.FurnitureItem
import it.unimi.dsi.fastutil.objects.Object2ObjectOpenHashMap
import it.unimi.dsi.fastutil.objects.ObjectImmutableList

object FurnitureManager {

    private val lock = Any()

    @Volatile
    var allCategories: List<FurnitureCategory> = emptyList()
        private set

    @Volatile
    var enabledCategories: List<FurnitureCategory> = emptyList()
        private set

    @Volatile
    private var categoriesById: Map<String, FurnitureCategory> = emptyMap()

    suspend fun loadFromFile() {
        val configCategories = PremiumShopConfigManager.loadAllCategories()

        val categories =
            ObjectImmutableList(configCategories.map(FurnitureCategory::fromConfig).sorted())
        val enabledCategories = ObjectImmutableList(categories.filter { it.enabled })
        val categoriesById = Object2ObjectOpenHashMap(categories.associateBy { it.id }).freeze()

        synchronized(lock) {
            this.allCategories = categories
            this.enabledCategories = enabledCategories
            this.categoriesById = categoriesById
        }
    }

    fun categoryById(id: String): FurnitureCategory? {
        return categoriesById[id]
    }

    suspend fun registerCategory(category: FurnitureCategory) {
        PremiumShopConfigManager.saveCategory(category.toConfig())
        loadFromFile()
    }

    suspend fun deleteCategory(category: FurnitureCategory) {
        PremiumShopConfigManager.deleteCategory(category.id)
        loadFromFile()
    }

    suspend fun registerItemToCategory(category: FurnitureCategory, item: FurnitureItem) {
        val old = category.toConfig()
        val new = old.copy(items = old.items + item.toConfig())

        PremiumShopConfigManager.saveCategory(new)
        loadFromFile()
    }

    suspend fun removeItemFromCategory(category: FurnitureCategory, item: FurnitureItem) {
        val old = category.toConfig()
        val new = old.copy(items = old.items.filter { it.id != item.id })

        PremiumShopConfigManager.saveCategory(new)
        loadFromFile()
    }
}