/*
 * ColorByte Hacked Client
 * A free half-open source mixin-based injection hacked client for Minecraft using Minecraft Forge.
 * https://github.com/SkidderRyF/ColorByte/
 */

package net.ccbluex.liquidbounce.features.module.modules.render

import net.ccbluex.liquidbounce.event.EventTarget
import net.ccbluex.liquidbounce.event.Render2DEvent
import net.ccbluex.liquidbounce.features.module.Module
import net.ccbluex.liquidbounce.features.module.ModuleCategory
import net.ccbluex.liquidbounce.features.module.ModuleInfo
import net.minecraft.client.gui.ScaledResolution
import net.minecraft.client.renderer.Tessellator
import net.minecraft.client.renderer.vertex.DefaultVertexFormats
import net.minecraft.entity.EntityLivingBase
import net.minecraft.util.ResourceLocation
import org.lwjgl.opengl.GL11

import java.awt.Color

@ModuleInfo(name = "KillESP", description = "i love u guys", category = ModuleCategory.RENDER)
class KillESP : Module() {
    @EventTarget
    fun onRender2D(e: Render2DEvent){
        val sr2 = ScaledResolution(mc2)
        val currentTarget = mc.thePlayer
        if (currentTarget != null) {
            GL11.glColor4f(1.0f, 1.0f, 1.0f, 1.0f)
            mc.fontRendererObj.drawStringWithShadow(
                currentTarget.health.toInt().toString(),
                (sr2.scaledWidth / 2 - mc.fontRendererObj.getStringWidth(currentTarget.health.toInt().toString()) / 2),
                (sr2.scaledHeight / 2 + 16),
                16777215
            )
            mc2.textureManager.bindTexture(ResourceLocation("textures/gui/icons.png"))
            var i2 = 0
            while (i2 < currentTarget.maxHealth / 2) {
                mc2.ingameGUI.drawTexturedModalRect(
                    ((sr2.scaledWidth / 2) - currentTarget.maxHealth / 2.0f * 10.0f / 2.0f + 9 + (i2 * 8)).toInt(),
                    (sr2.scaledHeight / 2 + 6),
                    16,
                    0,
                    9,
                    9
                )
                ++i2
            }
            i2 = 0
            while (i2 < currentTarget.health / 2.0) {
                mc2.ingameGUI.drawTexturedModalRect(
                    ((sr2.scaledWidth / 2) - currentTarget.maxHealth / 2.0f * 10.0f / 2.0f + 9 + (i2 * 8)).toInt(),
                    (sr2.scaledHeight / 2 + 6),
                    52,
                    0,
                    9,
                    9
                )
                ++i2
            }
        }
    }
}