package cn.clickgui.otcV2.Settings;
import cn.Client;
import cn.novoline.impl.Fonts;
import cn.clickgui.otcV2.Downward;
import cn.clickgui.otcV2.ModuleRender;

import cn.utils.miku.render.RenderUtil;
import cn.utils.miku.render.round.RoundedUtil;
import cn.utils.render.VisualBase;
import net.ccbluex.liquidbounce.features.module.modules.render.HUD;
import net.ccbluex.liquidbounce.features.module.modules.render.UIEffects;
import net.ccbluex.liquidbounce.utils.render.RenderUtils;
import net.ccbluex.liquidbounce.value.BoolValue;

import java.awt.*;

public class BoolSetting extends Downward<BoolValue> {

    public BoolSetting(BoolValue s, float x, float y, int width, int height, ModuleRender moduleRender) {
        super(s, x, y, width, height,moduleRender);

    }

    //获取主界面xy跟随移动
    private float modulex,moduley,booly;

    @Override
    public void draw(int mouseX, int mouseY) {
        modulex = Client.instance.otc.getMainx();
        moduley= Client.instance.otc.getMainy();

        //修复滚轮
        booly = pos.y + getScrollY();

        RenderUtil.drawRoundedRect(modulex+ 5+ pos.x + 4 ,moduley+ 17 +booly + 8  ,135 -128,7,1,setting.get() ?new Color(86,94,115).getRGB() :new Color(50,54,65).getRGB()  ,1,setting.get() ? new Color(86,94,115).getRGB():new Color(85,90,96).getRGB());
        if (isHovered(mouseX,mouseY)) {
            RenderUtil.drawRoundedRect(modulex + 5 + pos.x + 4, moduley + 17 + booly + 8, 135 - 128, 7, 1,  new Color(0,0,0,0).getRGB(), 1, new Color(UIEffects.Hudcolor.get()).getRGB());
        }
        Fonts.SFBOLD.SFBOLD_11.SFBOLD_11.drawString(setting.getName(),modulex+ 5+ pos.x + 4 + 10,moduley+ 17 + booly + 8+ 3,new Color(200,200,200).getRGB());

    }

    @Override
    public void mouseClicked(int mouseX, int mouseY, int mouseButton) {
        if (isHovered(mouseX,mouseY)){
            setting.toggle();
        }
    }

    @Override
    public void mouseReleased(int mouseX, int mouseY, int state) {

    }

    public boolean isHovered(int mouseX, int mouseY) {
        return mouseX >=modulex+ 5+ pos.x + 4   && mouseX <= modulex+ 5+ pos.x + 4 + 135 -128 && mouseY >= moduley+ 17 +booly + 8 && mouseY <= moduley+ 17 + booly + 8+ 7;
    }
}
