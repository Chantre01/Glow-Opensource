package cn.clickgui.otcV2.Settings;

import cn.Client;
import cn.novoline.impl.Fonts;
import cn.clickgui.otcV2.Downward;
import cn.clickgui.otcV2.ModuleRender;
import cn.utils.miku.math.MathUtil;
import cn.utils.miku.render.RenderUtil;

import cn.utils.miku.render.round.RoundedUtil;
import net.ccbluex.liquidbounce.LiquidBounce;
import net.ccbluex.liquidbounce.features.module.modules.render.ClickGUI;
import net.ccbluex.liquidbounce.features.module.modules.render.HUD;
import net.ccbluex.liquidbounce.features.module.modules.render.UIEffects;
import net.ccbluex.liquidbounce.utils.render.RenderUtils;
import net.ccbluex.liquidbounce.value.IntegerValue;
import net.minecraft.client.Minecraft;
import net.minecraft.util.math.MathHelper;

import java.awt.*;

public class NumberSetting extends Downward<IntegerValue> {

    public NumberSetting(IntegerValue s, float x, float y, int width, int height, ModuleRender moduleRender) {
        super(s, x, y, width, height, moduleRender);
    }

    //获取主界面xy跟随移动
    private float modulex,moduley,numbery;

    public float percent = 0;

    //anti-bug
    private boolean iloveyou;

    @Override
    public void draw(int mouseX, int mouseY) {
        modulex = Client.instance.otc.getMainx();
        moduley= Client.instance.otc.getMainy();

        //修复滚轮
        numbery = pos.y + getScrollY();

        double clamp = MathHelper.clamp(Minecraft.getMinecraft().getDebugFPS() / 30, 1, 9999);
        final double percentBar = (setting.get().doubleValue()- setting.getMinimum()
        ) / (setting.getMaximum()- setting.getMinimum());

        percent = Math.max(0, Math.min(1, (float) (percent + (Math.max(0, Math.min(percentBar, 1)) - percent) * (0.2 / clamp))));
        //V2 Style
        //背景
        RoundedUtil.drawRound(modulex+ 5+ pos.x + 55 ,moduley+ 17 +numbery + 8,75,2.5f,1, new Color(34,38,48));
        //value大小线条
        RoundedUtil.drawRound(modulex+ 5+ pos.x + 55 ,moduley+ 17 +numbery + 8,75 * percent,2.5f,1, new Color(UIEffects.Hudcolor.get()));
        //Value名字显示
        Fonts.SFBOLD.SFBOLD_11.SFBOLD_11.drawString(setting.getName(),modulex+ 5+ pos.x + 4 ,moduley+ 17 +numbery + 8,new Color(200,200,200).getRGB());

        //设置Value
        if (iloveyou){

            float percentt = Math.min(1, Math.max(0, ((mouseX - (modulex+ 5+ pos.x + 55)) / 99)* 1.3f));
            double newValue = (percentt * (setting.getMaximum()
                    - setting.getMinimum())) + setting.getMinimum();

            double set = MathUtil.incValue(newValue, 1);

            setting.set(set);
        }

        //绘制文字框
        final ClickGUI cg = (ClickGUI) LiquidBounce.moduleManager.getModule(ClickGUI.class);
        if (iloveyou || isHovered(mouseX,mouseY) || cg.disp.get()) {
            RoundedUtil.drawRound(modulex+ 5+ pos.x + 55 + 61 * percent, moduley+ 17 +numbery + 8 + 6,Fonts.SFBOLD.SFBOLD_12.SFBOLD_12.stringWidth(setting.get() + "") + 2,6,1,new Color(32,34,39));
            Fonts.SFBOLD.SFBOLD_12.SFBOLD_12.drawString(setting.get() + "", modulex+ 5+ pos.x + 55 + 62 * percent, moduley+ 17 +numbery + 8 + 8, new Color(250, 250, 250).getRGB());
        }
        //所选
        if (isHovered(mouseX,mouseY)){
            RenderUtil.drawRoundedRect(modulex+ 5+ pos.x + 55 ,moduley+ 17 +numbery + 8,75,2.5f,1, new Color(0,0,0,0).getRGB(),1,new Color(UIEffects.Hudcolor.get()).getRGB());
        }
        //V3 Style
    //    RoundedUtil.drawRound(modulex+ 5+ pos.x + 55 ,moduley+ 17 +numbery + 8,75,2.5f,1, new Color(UIEffects.Hudcolor.getVaule()));
     //   Fonts.SFBOLD.SFBOLD_11.SFBOLD_11.drawString(setting.getName(),modulex+ 5+ pos.x + 4 ,moduley+ 17 +numbery + 8,new Color(200,200,200).getRGB());
    }

    @Override
    public void mouseClicked(int mouseX, int mouseY, int mouseButton) {
        if (isHovered(mouseX, mouseY)) {
            if (mouseButton == 0) {
                iloveyou = true;
            }
        }
    }

    @Override
    public void mouseReleased(int mouseX, int mouseY, int state) {
        if (state == 0) iloveyou = false;
    }

    //如果指针在线条上
    public boolean isHovered(int mouseX, int mouseY) {
        return mouseX >=modulex+ 5+ pos.x + 55 && mouseX <= modulex+ 5+ pos.x + 55 + 75 && mouseY >= moduley+ 17 +numbery + 8 && mouseY <= moduley+ 17 +numbery + 8 + 2.5f;
    }
}
