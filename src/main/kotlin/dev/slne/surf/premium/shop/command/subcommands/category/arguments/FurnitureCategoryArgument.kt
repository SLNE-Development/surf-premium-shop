package dev.slne.surf.premium.shop.command.subcommands.category.arguments

import dev.jorel.commandapi.CommandAPI
import dev.jorel.commandapi.CommandAPICommand
import dev.jorel.commandapi.arguments.Argument
import dev.jorel.commandapi.arguments.ArgumentSuggestions
import dev.jorel.commandapi.arguments.CustomArgument
import dev.jorel.commandapi.arguments.StringArgument
import dev.slne.surf.premium.shop.config.PremiumShopConfig
import dev.slne.surf.premium.shop.furniture.category.FurnitureCategory

class FurnitureCategoryArgument(
    nodeName: String
) : CustomArgument<FurnitureCategory, String>(
    StringArgument(nodeName),
    { info ->
        val input = info.input
        val category = PremiumShopConfig.getConfig().furnitureShopCategories.firstOrNull {
            it.name.equals(
                input,
                ignoreCase = true
            )
        }

        if (category == null) {
            throw CommandAPI.failWithString("Category '$input' not found.")
        }

        category
    }
) {
    init {
        replaceSuggestions(ArgumentSuggestions.stringCollection { _ ->
            return@stringCollection PremiumShopConfig.getConfig().furnitureShopCategories.map { it.name }
        })
    }
}

inline fun CommandAPICommand.furnitureCategoryArgument(
    nodeName: String,
    optional: Boolean = false,
    block: Argument<*>.() -> Unit = {}
): CommandAPICommand = withArguments(
    FurnitureCategoryArgument(nodeName).setOptional(optional).apply(block)
)