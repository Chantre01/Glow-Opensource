package cn.clickgui.nohyper;

import cn.Client;
import cn.novoline.impl.Fonts;

import cn.clickgui.nohyper.Settings.*;
import cn.utils.miku.animations.Animation;
import cn.utils.miku.animations.Direction;
import cn.utils.miku.animations.impl.EaseInOutQuad;
import cn.utils.miku.render.RenderUtil;
import cn.utils.miku.render.round.RoundedUtil;
import net.ccbluex.liquidbounce.utils.render.RenderUtils;
import net.ccbluex.liquidbounce.features.module.Module;
import net.ccbluex.liquidbounce.features.module.modules.render.ClickGUI;
import net.ccbluex.liquidbounce.features.module.modules.render.HUD;
import net.ccbluex.liquidbounce.value.*;
import net.minecraft.client.Minecraft;
import net.minecraftforge.fml.common.Mod;

import java.awt.*;
import java.util.ArrayList;
import java.util.List;

public class ModuleRender {

    public Module module;

    public int height = 0;

    private Position pos;

    public float y,x;

    //主界面xy
    public int mainx,mainy;

    public int scrollY = 0;

    public Animation statsalpha = new EaseInOutQuad(100,1, Direction.FORWARDS);

    public List<Downward> downwards;

    public ModuleRender(Module module, float modX, float modY, float w, float h) {
        this.module = module;
        this.downwards = new ArrayList<>();

        int cHeight = 43;
        //value向下递增y
        for(Value setting : module.getValues()) {
            if (setting instanceof BoolValue) {
                this.downwards.add(new OptionSetting((BoolValue) setting, modX, modY+cHeight, 0, 0,this));
                cHeight += 20;
            }
            if (setting instanceof IntegerValue) {
                this.downwards.add(new NumberSetting((IntegerValue) setting, modX, modY+cHeight, 0, 0,this));
                cHeight += 20;
            }
            if (setting instanceof FloatValue) {
                this.downwards.add(new FloatSetting((FloatValue) setting, modX, modY+cHeight, 0, 0,this));
                cHeight += 20;
            }
            if (setting instanceof ColorValue) {
                this.downwards.add(new ColorSetting((ColorValue) setting, modX, modY+cHeight, 0, 0,this));
                cHeight += 20;
            }
            if (setting instanceof ListValue){
                this.downwards.add(new StringsSetting((ListValue) setting, modX, modY+cHeight, 0, 0,this));
                cHeight += 21;
            }
            if (setting instanceof TextValue){
                this.downwards.add(new TextSetting((TextValue) setting, modX, modY+cHeight, 0, 0,this));
                cHeight += 20;
            }
        }

        this.height = cHeight;

        pos = new Position(modX,modY,w,cHeight);

    }

    public void draw( int mx , int my){
        mainx = Client.instance.nohyp.mainx;
        mainy = Client.instance.nohyp.mainy;

        x = pos.x;
        y = pos.y + scrollY;

        RoundedUtil.drawRound(mainx + 10 + x,mainy + 35 + y,160,pos.height,4,new Color(36,38,44));

        Fonts.SF.SF_20.SF_20.drawString(module.getName(),mainx + 17 + x,mainy + 42 + y,-1);

        Fonts.SF.SF_17.SF_17.drawString("Activate",mainx + 17 + x,mainy + 60 + y,new Color(200,200,200).getRGB());

        RoundedUtil.drawRound(mainx + 153 + x,mainy + 60 + y,8,8,1,module.getState() ? new Color(ClickGUI.colorValue.get()): new Color(121,124,125));

        RenderUtil.drawCheck(mainx + 154.5f + x,mainy + 63.5f + y, (int) 2,module.getState() ? new Color(-1).getRGB(): new Color(165,167,169).getRGB());

        downwards.forEach(e -> e.draw(mx,my));

    }

    public void mouseClicked(int mouseX, int mouseY, int mouseButton){

        if (mouseButton == 0){
            if (RenderUtil.isHovering(mainx + 153 + x,mainy + 60 + y,8,8,mouseX,mouseY)){
                module.setState(!module.getState());
            }
        }


        for (Downward downward : downwards){
            downward.mouseClicked(mouseX,mouseY,mouseButton);
        }
    }

    public void mouseReleased(int mouseX, int mouseY, int state) {
        downwards.forEach(e -> e.mouseReleased(mouseX,mouseY,state));
    }
    public void keyTyped(char typedChar, int keyCode) {
        downwards.forEach(e -> e.keyTyped(typedChar,keyCode));
    }

    public int getMaxScrollY(){
        return (int) ((int) pos.y+ pos.height );
    }
    public int getPosY(){
        return (int) pos.y;
    }

    public int getScrollY() {
        return scrollY;
    }
}
