package net.ccbluex.liquidbounce.ui.client.hud.element.elements;



import cn.utils.miku.render.round.RoundedUtil;
import net.ccbluex.liquidbounce.LiquidBounce;
import net.ccbluex.liquidbounce.api.minecraft.client.gui.IFontRenderer;
import net.ccbluex.liquidbounce.features.module.Module;

import net.ccbluex.liquidbounce.features.module.modules.render.UIEffects;

import net.ccbluex.liquidbounce.ui.client.hud.element.Border;
import net.ccbluex.liquidbounce.ui.client.hud.element.Element;
import net.ccbluex.liquidbounce.ui.client.hud.element.ElementInfo;

import net.ccbluex.liquidbounce.ui.font.Fonts;

import net.ccbluex.liquidbounce.utils.render.RenderUtils;
import net.ccbluex.liquidbounce.value.BoolValue;

import java.awt.*;

import static net.ccbluex.liquidbounce.features.module.modules.render.UIEffects.Hudcolor;

@ElementInfo(name = "KeyBinds")
public class KeyBinds extends Element {
    public final BoolValue onlyState = new BoolValue("OnlyModuleState", false);
    int y2 =0;


    @Override
    public Border drawElement() {

        //draw Background


        switch (UIEffects.hudstyle.get().toLowerCase()){
            case "customcolor":
            case "customshader":
            case "customoutlined": {
                //if(UIEffects.hudblur.get()){
                //    UIEffects.renderblur(getRenderX(),getRenderY(),120f,23f + getModuleY(),6f);
                //}
                UIEffects.render(0, 0, 120, 23 + getModuleY(),10);
                colorText();
                break;
            }
            case "onetapv2":{
                int y =0;
                RoundedUtil.drawRound(0, 0, 80, 17 + getModuleY(), 1, new Color(22, 18, 27));
                RoundedUtil.drawRound(0, 0, 80, 15, 0, new Color(28, 30, 40));
                RoundedUtil.drawRound(0, 16f, 80, 0.7f, 0, new Color(42, 39, 44));
                RoundedUtil.drawRound(-0.5f, -1.7f, 81f, 1.2f, 0.5f, new Color(Hudcolor.getValue()));

                UIEffects.render(0, 0, 120, 23 + getModuleY(),10);
                cn.novoline.impl.Fonts.CsgoIcon.csgoicon_18.csgoicon_18.drawString("a", 3, 6.5f, -1);
                cn.novoline.impl.Fonts.SFTHIN.SFTHIN_22.SFTHIN_22.drawString("|", 17, 4f, new Color(47, 51, 52).getRGB());
                cn.novoline.impl.Fonts.SFTHIN.SFTHIN_16.SFTHIN_16.drawString("KeyBinds", 23, 5.5f, -1);
                for (Module module : LiquidBounce.moduleManager.getModules()) {
                    if (module.getKeyBind() == 0) continue;
                    if (onlyState.get()) {
                        if (!module.getState()) continue;
                    }
                    cn.novoline.impl.Fonts.Tahoma.Tahoma_14.Tahoma_14.drawString(module.getName(), 3,  y + 21f, -1);

                    cn.novoline.impl.Fonts.Tahoma.Tahoma_14.Tahoma_14.drawString("[toggled]", 78 - cn.novoline.impl.Fonts.Tahoma.Tahoma_14.Tahoma_14.stringWidth("[toggled]"), y + 21f, new Color(100, 100, 100).getRGB());

                    RoundedUtil.drawRound( 5, y + 28f, 70, 0.3f, 0, new Color(100, 100, 100));

                    y += 12;
                }

            }
        }

        return new Border(0,0,84,17+ getModuleY());
    }
    private void colorText(){
        int y2 =0;
        //draw text
        IFontRenderer cs40 = Fonts.cs40;
        IFontRenderer prod40 = Fonts.prod40;
        cn.novoline.impl.Fonts.CsgoIcon.csgoicon_20.csgoicon_20.drawString("a", 5, 7.5f, -1, false);
        cn.novoline.impl.Fonts.SFBOLD.SFBOLD_22.SFBOLD_22.drawString("KeyBinds", 10 + cn.novoline.impl.Fonts.CsgoIcon.csgoicon_20.csgoicon_20.stringWidth("a"), 5.5f, -1, false);
        //draw Module Bind
        for (Module module : LiquidBounce.moduleManager.getModules()) {
            if (module.getKeyBind() == 0) continue;
            if (onlyState.get()) {
                if (!module.getState()) continue;
            }
            cn.novoline.impl.Fonts.SF.SF_20.SF_20.drawString(module.getName(), 5, y2 + 21f, -1, false);
            cn.novoline.impl.Fonts.SF.SF_20.SF_20.drawString(module.getState() ? "[ON]" : "[OFF]", 116 - prod40.getStringWidth(module.getState() ? "[ON]" : "[OFF]"), y2 + 21f, module.getState() ? new Color(255, 255, 255).getRGB() : new Color(100, 100, 100).getRGB(), false);
            y2 += 12;
        }
    }

    public int getModuleY(){
        int y=0;
        for (Module module: LiquidBounce.moduleManager.getModules()) {
            if (module.getKeyBind() == 0) continue;
            if(onlyState.get()) {
                if (!module.getState()) continue;
            }
            y+=12;
        }

        return y;
    }

}
