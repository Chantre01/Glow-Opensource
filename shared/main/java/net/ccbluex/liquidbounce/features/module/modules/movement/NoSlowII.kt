package net.ccbluex.liquidbounce.features.module.modules.movement

import net.ccbluex.liquidbounce.event.ClickUpdateEvent
import net.ccbluex.liquidbounce.event.EventTarget
import net.ccbluex.liquidbounce.features.module.Module
import net.ccbluex.liquidbounce.features.module.ModuleCategory
import net.ccbluex.liquidbounce.features.module.ModuleInfo
import net.ccbluex.liquidbounce.features.module.modules.combat.KillAura

import net.minecraft.item.ItemBow
import net.minecraft.item.ItemStack
import net.minecraft.item.ItemSword

@ModuleInfo(name = "NoSlowII", description = "sb is u",
    category = ModuleCategory.MOVEMENT)
class NoSlowII : Module() {
    private var aura: KillAura? = null
    private var count = 0
    private var lastItem: ItemStack? = null
    private val isBlocking: Boolean
        get() = mc.thePlayer!!.isUsingItem && mc.thePlayer!!.heldItem != null && mc.thePlayer!!.heldItem!!.item is ItemSword
    @EventTarget
    fun onClick(event: ClickUpdateEvent) {
        val player = mc.thePlayer ?: return
        val currentItem = player.heldItem as ItemStack
        if (currentItem != null && (player.isUsingItem || isBlocking) && (player.moveForward != 0.0f || player.moveStrafing != 0.0f) && currentItem.item !is ItemBow) {
            if (lastItem != null && lastItem!! != currentItem) {
                count = 0
            }
            val state = if (currentItem.item is ItemSword) 1 else 3
            if (count != state) {
                event.cancelEvent()
                mc.playerController.windowClick(mc.thePlayer!!.openContainer!!.windowId, 20, 1, 1,
                    mc.thePlayer!!
                )
                player.stopUsingItem()
                player.closeScreen()
                count = state
            }
            if (event.isCancelled) mc2.sendClickBlockToController(mc.currentScreen == null && mc.gameSettings.keyBindAttack.isKeyDown && mc2.inGameHasFocus)
            lastItem = currentItem
        } else {
            count = 0
        }

    }
}