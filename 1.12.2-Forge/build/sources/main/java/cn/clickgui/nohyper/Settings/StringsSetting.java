package cn.clickgui.nohyper.Settings;

import cn.Client;
import cn.novoline.impl.Fonts;
import cn.clickgui.nohyper.Downward;
import cn.clickgui.nohyper.ModuleRender;
import cn.utils.boker.render.RenderUtil;
import cn.utils.miku.render.round.RoundedUtil;
import net.ccbluex.liquidbounce.utils.render.RenderUtils;
import net.ccbluex.liquidbounce.features.module.modules.render.ClickGUI;
import net.ccbluex.liquidbounce.value.ListValue;
import org.lwjgl.opengl.GL11;

import java.awt.*;


import static net.minecraft.client.Minecraft.getDebugFPS;

public class StringsSetting extends Downward<ListValue> {

    public StringsSetting(ListValue s, float x, float y, int width, int height, ModuleRender moduleRender) {
        super(s, x, y, width, height, moduleRender);
    }

    //主界面xy
    public int mainx,mainy,y;

    private double length = 3, anim = 5;

    @Override
    public void draw(int mouseX, int mouseY) {

        mainx = Client.instance.nohyp.mainx;
        mainy = Client.instance.nohyp.mainy;

        y = (int) (pos.y + getScrollY());

        Fonts.SF.SF_17.SF_17.drawString(setting.getName(),mainx + 17 + pos.x,mainy + 38 + y,new Color(200,200,200).getRGB());

        RoundedUtil.drawRound(mainx + 91 + pos.x, mainy + 35 + y, 70, 12, 2, new Color(45, 46, 53));

        Fonts.SF.SF_16.SF_16.drawString(setting.get(),mainx + 93 + pos.x,mainy + 38 + y,new Color(200,200,200).getRGB());

        double val = getDebugFPS() / 8.3;
        if (setting.openList && length > -3) {
            length -= 3 / val;
        } else if (!setting.openList && length < 3) {
            length += 3 / val;
        }
        if (setting.openList && anim < 8) {
            anim += 3 / val;
        } else if (!setting.openList && anim > 5) {
            anim -= 3 / val;
        }

        //绘制箭头
        RenderUtil.drawArrow(mainx + 152 + pos.x,mainy + 34.5f + y + anim, (int) 2, new Color(200,200,200).getRGB(), length);

        if (setting.openList) {
            //循环添加Strings

            //覆盖下面的Value
            GL11.glTranslatef((float) 0.0f, (float) 0.0f, (float) 2.0f);
            RoundedUtil.drawRound(mainx + 91 + pos.x, mainy + 35 +12 + y, 70, setting.getModes().size() * 12f, 2, new Color(45, 46, 53));

            for (String option : setting.getModes()) {
                if (option.equals(setting.get())){
                    RoundedUtil.drawRound(mainx + 91 + 69 + pos.x,mainy + 38 + 11 +  y + setting.getModeListNumber(option)* 12 , 1,8,1,new Color(ClickGUI.colorValue.getValue()));
                }
                Fonts.SF.SF_15.SF_15.drawString(option,mainx + 93 + pos.x,mainy + 38 + 12 +  y + setting.getModeListNumber(option)* 12 , option.equals(setting.get()) ?new Color(ClickGUI.colorValue.getValue()).getRGB():new Color(200,200,200).getRGB());
            }
            GL11.glTranslatef((float) 0.0f, (float) 0.0f, (float) -2.0f);
        }
    }

    @Override
    public void mouseClicked(int mouseX, int mouseY, int mouseButton) {
        if (mouseButton == 1 && RenderUtil.isHovering(mainx + 91 + pos.x,mainy + 35 + y,70,12,mouseX,mouseY)){
            setting.openList = !setting.openList;
        }

        //设置mode
        if (mouseButton == 0) {
            if (this.setting.openList //在这个x里面
                    && mouseX >= mainx + 91 + pos.x // 最小x
                    && mouseX <= mainx + 91 + pos.x + 70 // 最大x
            ) {
                //循环判断点击
                for (int i = 0; i < setting.getModes().size(); i++) {
                    //判断Y
                    final int v = (mainy + 38 + 12 +  y + i * 12);

                    if (mouseY >= v && mouseY <= v + 12) {
                        this.setting.set(this.setting.getModeGet(i));
                       // this.setting.openList = false;
                        //   moduleRender.sub.updatepostion();
                    }

                }
            }
        }
    }

    @Override
    public void mouseReleased(int mouseX, int mouseY, int state) {

    }
}
