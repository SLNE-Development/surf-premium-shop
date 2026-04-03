package dev.slne.surf.premium.shop.command.subcommands.category.arguments

import dev.jorel.commandapi.CommandAPI
import dev.jorel.commandapi.CommandAPICommand
import dev.jorel.commandapi.arguments.Argument
import dev.jorel.commandapi.arguments.ArgumentSuggestions
import dev.jorel.commandapi.arguments.CustomArgument
import dev.jorel.commandapi.arguments.StringArgument
import dev.slne.surf.premium.shop.config.config
import dev.slne.surf.premium.shop.furniture.category.FurnitureCategory

class FurnitureCategoryArgument(
    nodeName: String
) : CustomArgument<FurnitureCategory, String>(
    StringArgument(nodeName),
    { info ->
        val input = info.input
        config.furniture.categoryByName(input)
            ?: throw CommandAPI.failWithString("Category '$input' not found.")
    }
) {
    init {
        replaceSuggestions(ArgumentSuggestions.stringCollection { _ ->
            return@stringCollection config.furniture.categories.map(FurnitureCategory::name)
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