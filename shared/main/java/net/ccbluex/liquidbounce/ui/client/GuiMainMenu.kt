
package net.ccbluex.liquidbounce.ui.client

import cn.utils.render.RenderUtil
import net.ccbluex.liquidbounce.LiquidBounce
import net.ccbluex.liquidbounce.api.minecraft.client.gui.IGuiButton
import net.ccbluex.liquidbounce.api.util.WrappedGuiScreen
import net.ccbluex.liquidbounce.ui.client.altmanager.GuiAltManager
import net.ccbluex.liquidbounce.ui.font.Fonts
import net.ccbluex.liquidbounce.utils.render.RenderUtils
import net.minecraft.util.ResourceLocation
import java.awt.Color

class GuiMainMenu : WrappedGuiScreen() {

        var lastAnimTick: Long = 0L
        var alrUpdate = false

        override fun initGui() {
        val defaultHeight = representedScreen.height / 2;

        representedScreen.buttonList.add(classProvider.createGuiButton(1, representedScreen.width / 2 - 150, defaultHeight + 24, 300, 20, "S I N G L E P L A Y E R"))
        representedScreen.buttonList.add(classProvider.createGuiButton(2, representedScreen.width / 2 - 150, defaultHeight + 48, 300, 20, "M U L T I P L A Y E R"))
        representedScreen.buttonList.add(classProvider.createGuiButton(0, representedScreen.width / 2 - 150, defaultHeight + 72, 148, 20,"O P T I O N S"))
        representedScreen.buttonList.add(classProvider.createGuiButton(100, representedScreen.width / 2 + 2, defaultHeight + 72, 148, 20,"A L T S"))
        //representedScreen.buttonList.add(classProvider.createGuiButton(4, representedScreen.width / 2 - 50, defaultHeight + 120 , 100, 20, functions.formatI18n("menu.quit")))
        }

        override fun drawScreen(mouseX: Int, mouseY: Int, partialTicks: Float) {
                if (!alrUpdate) {
                        lastAnimTick = System.currentTimeMillis()
                        alrUpdate = true
                }

                representedScreen.drawBackground(0)
                //logo
                RenderUtil.drawImage(ResourceLocation("liquidbounce/menulogo.png"),representedScreen.width / 2-45,representedScreen.height / 2 - 120,90,90)
                //title
                Fonts.sfbold65.drawCenteredString("Glow Client", representedScreen.width / 2F, representedScreen.height / 2 - 20f, Color.WHITE.rgb)
                representedScreen.superDrawScreen(mouseX, mouseY, partialTicks)
                if (!LiquidBounce.mainMenuPrep) {
                        val animProgress = ((System.currentTimeMillis() - lastAnimTick).toFloat() / 2000F).coerceIn(0F, 1F)
                        RenderUtils.drawRect(0F, 0F, representedScreen.width.toFloat(), representedScreen.height.toFloat(), Color(0F, 0F, 0F, 1F - animProgress))
                        if (animProgress >= 1F)
                                LiquidBounce.mainMenuPrep = true

                }
        }

        override fun actionPerformed(button: IGuiButton) {
        when (button.id) {
        0 -> mc.displayGuiScreen(classProvider.createGuiOptions(this.representedScreen, mc.gameSettings))
        1 -> mc.displayGuiScreen(classProvider.createGuiSelectWorld(this.representedScreen))
        2 -> mc.displayGuiScreen(classProvider.createGuiMultiplayer(this.representedScreen))
        4 -> mc.shutdown()
        100 -> mc.displayGuiScreen(classProvider.wrapGuiScreen(GuiAltManager(this.representedScreen)))
        101 -> mc.displayGuiScreen(classProvider.wrapGuiScreen(GuiServerStatus(this.representedScreen)))
        102 -> mc.displayGuiScreen(classProvider.wrapGuiScreen(GuiBackground(this.representedScreen)))
        103 -> mc.displayGuiScreen(classProvider.wrapGuiScreen(GuiModsMenu(this.representedScreen)))
        108 -> mc.displayGuiScreen(classProvider.wrapGuiScreen(GuiContributors(this.representedScreen)))
        }
        }
        }
