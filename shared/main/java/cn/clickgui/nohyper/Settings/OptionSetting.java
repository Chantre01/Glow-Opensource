package cn.clickgui.nohyper.Settings;

import cn.Client;
import cn.novoline.impl.Fonts;
import cn.clickgui.nohyper.Downward;
import cn.clickgui.nohyper.ModuleRender;
import cn.utils.miku.render.RenderUtil;
import cn.utils.miku.render.round.RoundedUtil;
import net.ccbluex.liquidbounce.utils.render.RenderUtils;
import net.ccbluex.liquidbounce.features.module.modules.render.ClickGUI;
import net.ccbluex.liquidbounce.utils.render.RenderUtils;
import net.ccbluex.liquidbounce.value.BoolValue;

import java.awt.*;

public class OptionSetting extends Downward<BoolValue> {

    public OptionSetting(BoolValue s, float x, float y, int width, int height, ModuleRender moduleRender) {
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

        RoundedUtil.drawRound(mainx + 153 + pos.x,mainy + 38 + y,8,8,1,setting.get() ? new Color(ClickGUI.colorValue.getValue()): new Color(121,124,125));

        RenderUtil.drawCheck(mainx + 154.5f + pos.x,mainy + 41.5f + y, (int) 2,setting.get()  ? new Color(-1).getRGB(): new Color(165,167,169).getRGB());


    }

    @Override
    public void mouseClicked(int mouseX, int mouseY, int mouseButton) {
        if (RenderUtil.isHovering(mainx + 153 + pos.x,mainy + 38 + y,8,8,mouseX,mouseY) && mouseButton == 0){
            setting.toggle();
        }
    }

    @Override
    public void mouseReleased(int mouseX, int mouseY, int state) {

    }

}
