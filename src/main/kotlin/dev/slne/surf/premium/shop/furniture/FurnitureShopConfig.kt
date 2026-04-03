package dev.slne.surf.premium.shop.furniture

import dev.slne.surf.premium.shop.furniture.category.FurnitureCategory
import dev.slne.surf.premium.shop.furniture.item.FurnitureItem
import org.spongepowered.configurate.objectmapping.ConfigSerializable

@ConfigSerializable
data class FurnitureShopConfig(
    val categories: MutableList<FurnitureCategory> = mutableListOf(),
    val items: MutableList<FurnitureItem> = mutableListOf(),
) {
    fun addCategory(category: FurnitureCategory) {
        categories.add(category)
    }

    fun removeCategory(category: FurnitureCategory) {
        removeItems(*category.items.toTypedArray())
        
        categories.remove(category)
    }

    fun addItem(item: FurnitureItem) {
        items.add(item)
    }

    fun removeItem(item: FurnitureItem) {
        items.remove(item)
    }

    fun removeItems(vararg item: FurnitureItem) {
        items.removeAll(item.toSet())
    }

    fun categoryByName(name: String): FurnitureCategory? {
        return categories.firstOrNull { it.name.equals(name, true) }
    }

    fun itemByName(categoryName: String, name: String): FurnitureItem? {
        return items.firstOrNull {
            it.name.equals(name, true) && it.categoryName.equals(categoryName, true)
        }
    }

    fun itemsByCategoryName(categoryName: String): List<FurnitureItem> {
        return items.filter { it.categoryName.equals(categoryName, true) }
    }
}