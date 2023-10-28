/*
 * ColorByte Hacked Client
 * A free half-open source mixin-based injection hacked client for Minecraft using Minecraft Forge.
 * https://github.com/SkidderRyF/ColorByte/
 */

package net.ccbluex.liquidbounce.features.module.modules.movement

import net.ccbluex.liquidbounce.event.*
import net.ccbluex.liquidbounce.features.module.Module
import net.ccbluex.liquidbounce.features.module.ModuleCategory
import net.ccbluex.liquidbounce.features.module.ModuleInfo
import net.ccbluex.liquidbounce.utils.MovementUtils
import net.ccbluex.liquidbounce.value.BoolValue
import net.minecraft.client.gui.GuiChat
import net.minecraft.client.gui.inventory.GuiContainer
import org.lwjgl.input.Keyboard

@ModuleInfo(name = "GuiMove", description = "Allows you to walk while in a GUI.", category = ModuleCategory.MOVEMENT)
class GuiMove : Module() {

    private val undetectable = BoolValue("Undetectable", false)
    val aacAdditionProValue = BoolValue("AACAdditionPro", false)
    private val testResetSprintValue = BoolValue("ResetSprint", true)
    private val noMoveClicksValue = BoolValue("NoMoveClicks", false)
    private val rotateValue = BoolValue("Rotate", true)


    private val affectedBindings = arrayOf(
        mc.gameSettings.keyBindForward,
        mc.gameSettings.keyBindBack,
        mc.gameSettings.keyBindRight,
        mc.gameSettings.keyBindLeft,
        mc.gameSettings.keyBindJump,
        mc.gameSettings.keyBindSprint
    )

    private fun updateKeyState() {
        if (mc2.currentScreen != null && mc2.currentScreen !is GuiChat && (!undetectable.get() || mc2.currentScreen !is GuiContainer)) {
            for (affectedBinding in affectedBindings) {
                affectedBinding.pressed = mc.gameSettings.isKeyDown(affectedBinding)
            }

            if (rotateValue.get()) {
                rotate()
            }
        }
    }

    @EventTarget
    fun onUpdate(event: UpdateEvent) {
        if (!testResetSprintValue.get()) tick()
    }

    @EventTarget
    fun onMotion(e: MotionEvent) {
        if (testResetSprintValue.get()) updateKeyState()
    }

    @EventTarget
    fun onScreen(e: ScreenEvent) {
        if (testResetSprintValue.get()) updateKeyState()
    }

    private fun rotate() {
        if (Keyboard.isKeyDown(Keyboard.KEY_UP)) {
            if (mc.thePlayer!!.rotationPitch > -90) {
                mc.thePlayer!!.rotationPitch -= 5
            }
        }
        if (Keyboard.isKeyDown(Keyboard.KEY_DOWN)) {
            if (mc.thePlayer!!.rotationPitch < 90) {
                mc.thePlayer!!.rotationPitch += 5
            }
        }
        if (Keyboard.isKeyDown(Keyboard.KEY_LEFT)) {
            mc.thePlayer!!.rotationYaw -= 5
        }
        if (Keyboard.isKeyDown(Keyboard.KEY_RIGHT)) {
            mc.thePlayer!!.rotationYaw += 5
        }
    }


    private fun tick() {
        if (!classProvider.isGuiChat(mc.currentScreen) && !classProvider.isGuiIngameMenu(mc.currentScreen) && (!undetectable.get() || !classProvider.isGuiContainer(
                mc.currentScreen
            ))
        ) {
            for (affectedBinding in affectedBindings) {
                affectedBinding.pressed = mc.gameSettings.isKeyDown(affectedBinding)
            }
            if (rotateValue.get() && mc2.currentScreen != null) rotate()
        }
    }

    @EventTarget
    fun onClick(event: ClickWindowEvent) {
        if (noMoveClicksValue.get() && MovementUtils.isMoving)
            event.cancelEvent()
    }

    override fun onDisable() {
        val isIngame = mc.currentScreen != null

        for (affectedBinding in affectedBindings) {
            if (!mc.gameSettings.isKeyDown(affectedBinding) || isIngame)
                affectedBinding.pressed = false
        }
    }

    override val tag: String?
        get() = if (aacAdditionProValue.get()) "AACAdditionPro" else null
}
