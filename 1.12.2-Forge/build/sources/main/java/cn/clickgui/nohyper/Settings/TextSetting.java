package cn.clickgui.nohyper.Settings;

import cn.Client;
import cn.novoline.impl.Fonts;
import cn.clickgui.nohyper.Downward;
import cn.clickgui.nohyper.ModuleRender;
import cn.utils.miku.render.RenderUtil;
import cn.utils.miku.render.round.RoundedUtil;
import net.ccbluex.liquidbounce.utils.render.RenderUtils;
import net.ccbluex.liquidbounce.value.TextValue;
import org.lwjgl.input.Keyboard;

import java.awt.*;

public class TextSetting extends Downward<TextValue> {
    public TextSetting(TextValue s, float x, float y, int width, int height, ModuleRender moduleRender) {
        super(s, x, y, width, height, moduleRender);
    }

    //主界面xy
    public int mainx,mainy,y;

    @Override
    public void draw(int mouseX, int mouseY) {
        mainx = Client.instance.nohyp.mainx;
        mainy = Client.instance.nohyp.mainy;

        y = (int) (pos.y + getScrollY());

        Fonts.SF.SF_17.SF_17.drawString(setting.getName(),mainx + 17 + pos.x,mainy + 38 + y,new Color(200,200,200).getRGB());

        RoundedUtil.drawRound(mainx + 91 + pos.x, mainy + 35 + y, 70, 12, 2, new Color(45, 46, 53));

        //显示框内文字 判断长度
        if (Fonts.SF.SF_16.SF_16.stringWidth(setting.getTextHovered() ? setting.get() + "_": setting.get()) > 65) {
            //修剪字符串长度
            Fonts.SF.SF_16.SF_16
                    .drawString(Fonts.SF.SF_16.SF_16.trimStringToWidth(setting.getTextHovered() ? setting.get() + "_": setting.get(), 59, true),mainx + 93 + pos.x,mainy + 38 + y,
                            new Color(200,200,200).getRGB());
        } else {
            if (setting.get().isEmpty() && !setting.getTextHovered()){
                Fonts.SF.SF_16.SF_16.drawString("Type Here...",mainx + 93 + pos.x,mainy + 38 + y, new Color(200,200,200).getRGB());
            }else {
                Fonts.SF.SF_16.SF_16.drawString(setting.getTextHovered() ? setting.get() + "_" : setting.get(),mainx + 93 + pos.x,mainy + 38 + y, new Color(200,200,200).getRGB());
            }
        }
    }

    @Override
    public void mouseClicked(int mouseX, int mouseY, int mouseButton) {
        if (RenderUtil.isHovering(mainx + 91 + pos.x, mainy + 35 + y, 70, 12,mouseX, mouseY)) {
            setting.setTextHovered(!setting.getTextHovered());
        } else if (setting.getTextHovered()) {
            setting.setTextHovered(false);
        }
    }

    @Override
    public void mouseReleased(int mouseX, int mouseY, int state) {

    }

    @Override
    public void keyTyped(char typedChar, int keyCode) {
        //输入
        if (setting.getTextHovered()) {
            if (keyCode == Keyboard.KEY_ESCAPE) {
                setting.setTextHovered(false);
            } else if (!(keyCode == Keyboard.KEY_BACK) && keyCode != Keyboard.KEY_RCONTROL && keyCode != Keyboard.KEY_LCONTROL && keyCode != Keyboard.KEY_RSHIFT && keyCode != Keyboard.KEY_LSHIFT && keyCode != Keyboard.KEY_TAB && keyCode != Keyboard.KEY_CAPITAL && keyCode != Keyboard.KEY_DELETE && keyCode != Keyboard.KEY_HOME && keyCode != Keyboard.KEY_INSERT && keyCode != Keyboard.KEY_UP && keyCode != Keyboard.KEY_DOWN && keyCode != Keyboard.KEY_RIGHT && keyCode != Keyboard.KEY_LEFT && keyCode != Keyboard.KEY_LMENU && keyCode != Keyboard.KEY_RMENU && keyCode != Keyboard.KEY_PAUSE && keyCode != Keyboard.KEY_SCROLL && keyCode != Keyboard.KEY_END && keyCode != Keyboard.KEY_PRIOR && keyCode != Keyboard.KEY_NEXT && keyCode != Keyboard.KEY_APPS && keyCode != Keyboard.KEY_F1 && keyCode != Keyboard.KEY_F2 && keyCode != Keyboard.KEY_F3 && keyCode != Keyboard.KEY_F4 && keyCode != Keyboard.KEY_F5 && keyCode != Keyboard.KEY_F6 && keyCode != Keyboard.KEY_F7 && keyCode != Keyboard.KEY_F8 && keyCode != Keyboard.KEY_F9 && keyCode != Keyboard.KEY_F10 && keyCode != Keyboard.KEY_F11 && keyCode != Keyboard.KEY_F12) {
                setting.append(typedChar);
            }

            //删除
            if (setting.getTextHovered() && Keyboard.isKeyDown(Keyboard.KEY_BACK) && setting.get().length() >= 1) {
                setting.set(setting.get().substring(0, setting.get().length() - 1));
            }
        }
        super.keyTyped(typedChar, keyCode);
    }
}
