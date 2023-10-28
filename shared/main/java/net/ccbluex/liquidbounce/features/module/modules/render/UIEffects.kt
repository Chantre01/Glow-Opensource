package net.ccbluex.liquidbounce.features.module.modules.render

import cn.utils.render.VisualBase
import net.ccbluex.liquidbounce.features.module.Module
import net.ccbluex.liquidbounce.features.module.ModuleCategory
import net.ccbluex.liquidbounce.features.module.ModuleInfo
import net.ccbluex.liquidbounce.utils.render.RenderUtils
import net.ccbluex.liquidbounce.value.*
import org.lwjgl.opengl.GL11
import java.awt.Color

@ModuleInfo(name = "UIEffects", description = "RenderSkills", category = ModuleCategory.RENDER)
public class UIEffects : Module() {
    companion object{
        @JvmField
        val hudstyle = ListValue("CustomStyle", arrayOf("CustomColor", "CustomOutlined", "CustomShader", "OneTapV2"), "CustomShader")
        @JvmField
        var hudshaderradius = IntegerValue("ShaderRadius", 10, 0, 50)
        @JvmField
        val gidentoutline = BoolValue("GradientOutline", false)
        @JvmField
        val outlineWidth = FloatValue("OutlineWidth", 2.5f, 0f, 35f)
        @JvmField
        val hudblur = BoolValue("CustomBlur", false)
        @JvmField
        val hudblurradius = IntegerValue("BlurStrength", 10, 1, 50)
        @JvmField
        val cBGcolor = ColorValue("bgColor", Color(200, 200, 200, 200).rgb)
        @JvmField
        val cOutlinecolor = ColorValue("OutlineColor", Color(200, 200, 200, 100).rgb)
        @JvmField
        val cOutlinecolor2 = ColorValue("OutlineColor2", Color(200, 200, 200, 100).rgb)
        @JvmField
        val Hudcolor = ColorValue("HudColor", Color.CYAN.rgb)



        @JvmStatic
        fun render(x: Float, y: Float, width: Float, height: Float, radius: Float) {
            when (hudstyle.get()) {
                "CustomColor" -> RenderUtils.drawRoundedRect2(x, y, width, height, radius, cBGcolor.get())
                "CustomOutlined" -> {
                    RenderUtils.drawRoundedRect2(x, y, width, height, radius, cBGcolor.get())
                    if(gidentoutline.get()){
                        VisualBase.twoColorOutline(
                            x.toDouble(),
                            y.toDouble(),
                            width.toDouble(),
                            height.toDouble(),
                            radius.toDouble(),
                            outlineWidth.get(),
                            cOutlinecolor.get(),
                            cOutlinecolor2.get()
                        )
                    }else{
                        RenderUtils.drawOutlinedRoundedRect(
                            x.toDouble(),
                            y.toDouble(),
                            width.toDouble(),
                            height.toDouble(),
                            radius.toDouble(),
                            outlineWidth.get(),
                            cOutlinecolor.get()
                        )
                    }

                }
                "CustomShader" -> VisualBase.drawBlurredShadow(
                    x - 2f,
                    y - 2f,
                    width + 4f,
                    height + 4f,
                    hudshaderradius.get(),
                    cBGcolor.getAwtColor()
                )
                "OneTapV2" -> {

                }
            }
        }
    }



}