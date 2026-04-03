package dev.slne.surf.premium.shop.utils

import dev.slne.surf.surfapi.bukkit.api.permission.PermissionRegistry

object PermissionRegistry : PermissionRegistry() {
    private const val PREFIX = "surf.premium.shop"
    private const val COMMAND_PREFIX = "$PREFIX.command"

    val COMMAND_BASE = create("$COMMAND_PREFIX.base")
    val COMMAND_RELOAD = create("$COMMAND_PREFIX.reload")
    val COMMAND_OPEN = create("$COMMAND_PREFIX.open")

    private const val COMMAND_FURNITURE_PREFIX = "$COMMAND_PREFIX.furniture"
    val COMMAND_FURNITURE_BASE = create("$COMMAND_FURNITURE_PREFIX.base")

    private const val COMMAND_FURNITURE_CATEGORY_PREFIX = "$COMMAND_FURNITURE_PREFIX.category"
    val COMMAND_FURNITURE_CATEGORY_BASE = create("$COMMAND_FURNITURE_CATEGORY_PREFIX.base")
    val COMMAND_FURNITURE_CATEGORY_LIST = create("$COMMAND_FURNITURE_CATEGORY_PREFIX.list")
    val COMMAND_FURNITURE_CATEGORY_INFO = create("$COMMAND_FURNITURE_CATEGORY_PREFIX.info")
    val COMMAND_FURNITURE_CATEGORY_CREATE = create("$COMMAND_FURNITURE_CATEGORY_PREFIX.create")
    val COMMAND_FURNITURE_CATEGORY_DELETE = create("$COMMAND_FURNITURE_CATEGORY_PREFIX.delete")
    val COMMAND_FURNITURE_CATEGORY_ADD_ITEM = create("$COMMAND_FURNITURE_CATEGORY_PREFIX.add-item")
    val COMMAND_FURNITURE_CATEGORY_REMOVE_ITEM =
        create("$COMMAND_FURNITURE_CATEGORY_PREFIX.remove-item")
}