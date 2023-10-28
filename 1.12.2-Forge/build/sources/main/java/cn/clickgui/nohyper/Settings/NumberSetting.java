package cn.clickgui.nohyper.Settings;

import cn.Client;
import cn.novoline.impl.Fonts;
import cn.clickgui.nohyper.Downward;
import cn.clickgui.nohyper.ModuleRender;
import cn.utils.miku.math.MathUtil;
import cn.utils.miku.render.ColorUtils;
import cn.utils.miku.render.RenderUtil;
import cn.utils.miku.render.round.RoundedUtil;
import cn.utils.render.VisualBase;
import net.ccbluex.liquidbounce.utils.render.RenderUtils;
import net.ccbluex.liquidbounce.LiquidBounce;
import net.ccbluex.liquidbounce.features.module.modules.render.ClickGUI;
import net.ccbluex.liquidbounce.value.IntegerValue;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.entity.Render;
import net.minecraft.util.math.MathHelper;

import java.awt.*;

public class NumberSetting extends Downward<IntegerValue> {


    public NumberSetting(IntegerValue s, float x, float y, int width, int height, ModuleRender moduleRender) {
        super(s, x, y, width, height, moduleRender);
    }

    //主界面xy
    public int mainx,mainy,y;

    public float percent = 0;

    //anti-bug
    private boolean iloveyou;

    @Override
    public void draw(int mouseX, int mouseY) {
        mainx = Client.instance.nohyp.mainx;
        mainy = Client.instance.nohyp.mainy;

        y = (int) (pos.y + getScrollY());

        Fonts.SF.SF_17.SF_17.drawString(setting.getName(),mainx + 17 + pos.x,mainy + 38 + y,new Color(200,200,200).getRGB());

        RoundedUtil.drawRound(mainx + 91 + pos.x,mainy + 40 + y,70,5,2,new Color(45,46,53));

        double clamp = MathHelper.clamp(Minecraft.getDebugFPS() / 30, 1, 9999);
        final double percentBar = (setting.get().doubleValue()- setting.getMinimum()
        ) / (setting.getMaximum() - setting.getMinimum());

        percent = Math.max(0, Math.min(1, (float) (percent + (Math.max(0, Math.min(percentBar, 1)) - percent) * (0.2 / clamp))));

        RoundedUtil.drawRound(mainx + 91 + pos.x,mainy + 40 + y, (float) (70 * percent),5,2, new Color(ClickGUI.colorValue.getValue()));

        final float[] hudHSB = ClickGUI.colorValue.getHSB();
        Color color = Color.getHSBColor(hudHSB[0], hudHSB[1], 0.2f);

        RoundedUtil.drawRound((float) (mainx + 87 + pos.x + (70 * percent)),mainy + 40 + y - 0.1f,5,5,2,new Color(39,41,47));

        RoundedUtil.drawRound((float) (mainx + 87.5f + pos.x + (70 * percent)),mainy + 40.5f + y - 0.1f,4f,4f,2, ColorUtils.applyOpacity(new Color(ClickGUI.colorValue.getValue()),0.95f));

        //设置Value
        if (iloveyou){

            float percentt = Math.min(1, Math.max(0, ((mouseX - (mainx + 91 + pos.x)) / 99)* 1.4f));
            double newValue = (percentt * (setting.getMaximum()
                    - setting.getMinimum())) + setting.getMinimum();

            double set = MathUtil.incValue(newValue, 1);

            setting.set(set);
        }
        final ClickGUI cg = (ClickGUI) LiquidBounce.moduleManager.getModule(ClickGUI.class);

        if (!cg.disp.get()) {
            if (iloveyou || RenderUtil.isHovering(mainx + 91 + pos.x, mainy + 40 + y, 70, 5, mouseX, mouseY)) {
                Fonts.SF.SF_16.SF_16.drawString(setting.get().toString(), mainx + 90 + pos.x + (70 * percent) - Fonts.SF.SF_16.SF_16.stringWidth(setting.get().toString()), mainy + 30f + y, -1);
            }
        }


        if (cg.disp.get()){
            Fonts.SF.SF_16.SF_16.drawString(setting.get().toString(),mainx + 90 + pos.x+  70 - Fonts.SF.SF_16.SF_16.stringWidth(setting.get().toString()),mainy + 30f + y,-1);
        }
    }

    @Override
    public void mouseClicked(int mouseX, int mouseY, int mouseButton) {
        if (RenderUtil.isHovering(mainx + 91 + pos.x,mainy + 40 + y,70,5,mouseX,mouseY)) {
            if (mouseButton == 0) {
                iloveyou = true;
            }
        }
    }

    @Override
    public void mouseReleased(int mouseX, int mouseY, int state) {
        if (state == 0) iloveyou = false;
    }
}
