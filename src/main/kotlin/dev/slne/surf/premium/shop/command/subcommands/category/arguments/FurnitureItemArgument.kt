package dev.slne.surf.premium.shop.command.subcommands.category.arguments

import dev.jorel.commandapi.CommandAPI
import dev.jorel.commandapi.CommandAPICommand
import dev.jorel.commandapi.arguments.Argument
import dev.jorel.commandapi.arguments.ArgumentSuggestions
import dev.jorel.commandapi.arguments.CustomArgument
import dev.jorel.commandapi.arguments.StringArgument
import dev.slne.surf.premium.shop.furniture.category.FurnitureCategory
import dev.slne.surf.premium.shop.furniture.item.FurnitureItem

class FurnitureItemArgument(
    nodeName: String,
    categoryNodeName: String
) : CustomArgument<FurnitureItem, String>(
    StringArgument(nodeName),
    { info ->
        val input = info.input
        val category = info.previousArgs.getUnchecked<FurnitureCategory>(categoryNodeName)
            ?: throw CommandAPI.failWithString("Category '$categoryNodeName' not set.")

        category.itemById(input)
            ?: throw CommandAPI.failWithString("Item '$input' not found in category '${category.id}'.")
    }
) {
    init {
        replaceSuggestions(ArgumentSuggestions.stringCollection { info ->
            val category = info.previousArgs.getUnchecked<FurnitureCategory>(categoryNodeName)
            category?.items?.map(FurnitureItem::id) ?: emptyList()
        })
    }
}

inline fun CommandAPICommand.furnitureItemArgument(
    nodeName: String,
    categoryNodeName: String,
    optional: Boolean = false,
    block: Argument<*>.() -> Unit = {}
): CommandAPICommand = withArguments(
    FurnitureItemArgument(nodeName, categoryNodeName).setOptional(optional).apply(block)
)