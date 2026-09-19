package dev.slne.surf.premium.shop.manager

import com.github.shynixn.mccoroutine.folia.entityDispatcher
import dev.slne.surf.api.paper.region.TickThreadGuard
import dev.slne.surf.premium.shop.furniture.FurnitureManager
import dev.slne.surf.premium.shop.furniture.category.FurnitureCategory
import dev.slne.surf.premium.shop.furniture.item.FurnitureItem
import dev.slne.surf.premium.shop.plugin
import dev.slne.surf.transaction.api.currency.Currency
import dev.slne.surf.transaction.api.transaction.PendingTransactionResult
import dev.slne.surf.transaction.api.transaction.data.TransactionData
import dev.slne.surf.transaction.api.transactional.PendingExecutionDecision
import dev.slne.surf.transaction.api.transactional.PendingExecutionResult
import dev.slne.surf.transaction.api.user.transactionUser
import io.papermc.paper.datacomponent.DataComponentTypes
import kotlinx.coroutines.withContext
import org.bukkit.entity.Item
import org.bukkit.entity.Player
import org.bukkit.inventory.ItemStack

object PremiumShopManager {

    fun getAvailableCategories(player: Player): List<FurnitureCategory> {
        return FurnitureManager.enabledCategories.filter { player.hasPermission(it.permission) }
    }

    fun getAvailableFromCategory(player: Player, category: FurnitureCategory): List<FurnitureItem> {
        return category.enabledItems.filter { player.hasPermission(it.permission) }
    }

    /**
     * @param onSuccess Invoked when the transaction was successful and the items were given to the player.
     *                  Executed on the player's entity dispatcher.
     * @param onInsufficientFunds Invoked when the player does not have enough funds to complete the transaction.
     *                          Executed on the player's entity dispatcher.
     * @param onError Invoked when an error occurred during the transaction.
     *                Executed on the player's entity dispatcher.
     */
    suspend fun buy(
        player: Player,
        amount: Int,
        item: FurnitureItem,
        onSuccess: () -> Unit = {},
        onInsufficientFunds: () -> Unit = {},
        onError: () -> Unit = {},
    ) {
        val price = amount * item.price

        val result = player.transactionUser().withPendingWithdrawalDecision(
            amount = price.toBigDecimal(),
            currency = Currency.default(),
            additionalData = setOf(
                TransactionData.of(
                    "premium-furniture-item",
                    "${amount}x ${item.id} for ${item.price}"
                )
            ),
        ) { _ ->
            withContext(plugin.entityDispatcher(player)) {
                if (player.isOnline) {
                    giveItems(player, item, amount)
                    PendingExecutionDecision.commit()
                } else {
                    PendingExecutionDecision.rollback()
                }
            }
        }

        withContext(plugin.entityDispatcher(player)) {
            when (result) {
                is PendingExecutionResult.Completed -> {
                    onSuccess()
                }

                is PendingExecutionResult.ReservationFailed -> {
                    if (result.result == PendingTransactionResult.SenderInsufficientFunds) {
                        onInsufficientFunds()
                    } else {
                        onError()
                    }
                }

                is PendingExecutionResult.RolledBack -> Unit

                else -> onError()
            }
        }
    }

    private fun giveItems(player: Player, item: FurnitureItem, amount: Int) {
        TickThreadGuard.ensureTickThread(player, "Cannot give items off tick thread")

        val stacks = splitIntoMultipleItemStacks(item.itemStackTemplate, amount)
        val notAdded = stacks.flatMap { stack ->
            player.inventory.addItem(stack).values
        }

        notAdded.forEach { itemStack ->
            player.world.spawn(
                player.location,
                Item::class.java,
            ) { item ->
                item.itemStack = itemStack
                item.owner = player.uniqueId
                item.pickupDelay = 0

                // 5 minutes - 30 seconds so it despawns after 30 seconds
                item.ticksLived = 6000 - 600
            }
        }
    }

    private fun splitIntoMultipleItemStacks(itemStack: ItemStack, amount: Int): List<ItemStack> {
        val maxStackSize = itemStack.getData(DataComponentTypes.MAX_STACK_SIZE) ?: 1
        val neededStacks = (amount + maxStackSize - 1) / maxStackSize

        return List(neededStacks) { index ->
            val stackAmount = if (index == neededStacks - 1) {
                amount - (maxStackSize * index)
            } else {
                maxStackSize
            }

            itemStack.clone().asQuantity(stackAmount)
        }
    }
}