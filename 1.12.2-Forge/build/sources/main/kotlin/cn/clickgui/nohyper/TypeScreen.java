package cn.clickgui.nohyper;

import cn.Client;
import cn.novoline.impl.Fonts;
import cn.utils.miku.animations.Animation;
import cn.utils.miku.animations.Direction;
import cn.utils.miku.animations.impl.SmoothStepAnimation;
import cn.utils.miku.math.MathUtil;
import cn.utils.miku.render.RenderUtil;
import cn.utils.miku.render.round.RoundedUtil;
import net.ccbluex.liquidbounce.utils.render.RenderUtils;
import net.ccbluex.liquidbounce.LiquidBounce;
import net.ccbluex.liquidbounce.features.module.Module;
import net.ccbluex.liquidbounce.features.module.ModuleCategory;
import net.ccbluex.liquidbounce.features.module.modules.render.ClickGUI;
import net.ccbluex.liquidbounce.features.module.modules.render.HUD;
import org.lwjgl.input.Mouse;
import org.lwjgl.opengl.GL11;

import java.awt.*;
import java.util.ArrayList;

public class TypeScreen {

    private Position pos;
    public int x ,y;

    private ModuleCategory type;

    //主界面xy
    public int mainx,mainy;

    //选择大类
    private boolean selected;

    public int leftAdd,rightAdd;

    public ArrayList<ModuleRender> moduleRect;

    //滚轮
    private float maxScroll = Float.MAX_VALUE, minScroll = 0, rawScroll;
    private float scroll;
    private Animation scrollAnimation = new SmoothStepAnimation(0, 0, Direction.BACKWARDS);
    //

    public TypeScreen(ModuleCategory category, int x) {
        this.x = x;
        this.type = category;

        this.pos = new Position(0,0,0,0);

        int count=0;
        moduleRect = new ArrayList<>();
        //new ly ry
        for (Module holder : LiquidBounce.moduleManager.getModules()) {
            if (holder.getCategory().equals(category)) {
                float posWidth = 0;

                //判断左右分别添加
                //奇偶判断
                float posX = pos.x+((count%2 == 0) ? 0 : 170);
                float posY = pos.y+((count%2 == 0) ? leftAdd : rightAdd);

                Position pos = new Position(posX, posY, posWidth, 20);
                //只需要x y height
                ModuleRender otlM = new ModuleRender(holder,pos.x, pos.y, pos.width, 0);
                pos.height = otlM.height;
                if(count%2 == 0){
                    leftAdd+=pos.height+10;
                }else{
                    rightAdd+=pos.height+10;
                }
                moduleRect.add(otlM);
                count++;
            }
        }

    }

    public void draw(int mx , int my){
        mainx = Client.instance.nohyp.mainx;
        mainy = Client.instance.nohyp.mainy;

        //绘制图标ICONFONT
        String l = "";
        if (type.name().equalsIgnoreCase("Combat")) {
            l = "D";
        }  else if (type.name().equalsIgnoreCase("Player")) {
            l = "B";
        } else if (type.name().equalsIgnoreCase("Misc")) {
            l = "F";
        }

        //CHeck icon
        String l2 = "";
        if (type.name().equalsIgnoreCase("Render")) {
            l2 = "d";
        } else if (type.name().equalsIgnoreCase("World")) {
            l2 = "b";
        } else if (type.name().equalsIgnoreCase("Exploit")) {
            l2 = "a";
        }else if (type.name().equalsIgnoreCase("Movement")) {
            l2 = "f";
        }

        Fonts.SF.SF_18.SF_18.drawString(newcatename(type),mainx + 112 + x ,mainy+ 15,selected ? ClickGUI.colorValue.get():new Color(133,135,139).getRGB());

        Fonts.ICONFONT.ICONFONT_18.ICONFONT_18.drawString(l,mainx + 100 + x,mainy+ 16,selected ? ClickGUI.colorValue.get():new Color(133,135,139).getRGB());
        Fonts.CheckFont.CheckFont_18.CheckFont_18.drawString(l2,mainx + 100 + x,mainy+ 16,selected ? ClickGUI.colorValue.get():new Color(133,135,139).getRGB());

        if (selected) {
            RoundedUtil.drawRound(mainx + 100 + x, mainy, Fonts.SF.SF_18.SF_18.stringWidth(newcatename(type)) + 12, 1.2f, 0, new Color(ClickGUI.colorValue.get()));
        }

        if (selected) {
            GL11.glPushMatrix();
            RenderUtil.scissor(mainx,mainy + 35,500,300 - 35);
            GL11.glEnable(GL11.GL_SCISSOR_TEST);
            moduleRect.forEach(e -> e.draw(mx, my));
            GL11.glDisable(GL11.GL_SCISSOR_TEST);
            GL11.glPopMatrix();


            double scrolll = getScroll();
            for (ModuleRender module : moduleRect)
            {
                module.scrollY = (int) MathUtil.roundToHalf(scrolll);
            }
            onScroll(30,mx,my);
            //判断
            maxScroll = Math.max(0,moduleRect.size() == 0 ? 0 : moduleRect.get(moduleRect.size() -1).getMaxScrollY()) ;
            //
        }
    }

    public String newcatename(ModuleCategory moduleCategory){
        if (moduleCategory.getDisplayName().equals("Combat")){
            return "Combat";
        }else if (moduleCategory.getDisplayName().equals("Player")){
            return "Player";
        }else if (moduleCategory.getDisplayName().equals("Movement")){
            return "Move";
        }else if (moduleCategory.getDisplayName().equals("Render")){
            return "Visuals";
        }else if (moduleCategory.getDisplayName().equals("World")){
            return "World";
        }else if (moduleCategory.getDisplayName().equals("Misc")){
            return "Misc";
        }else if (moduleCategory.getDisplayName().equals("Exploit")){
            return "Exploit";
        }
        return "";
    }

    public void setSelected(boolean s){
        this.selected = s;
    }

    public boolean isSelected() {
        return this.selected;
    }

    //滚轮
    public void onScroll(int ms,int mx ,int my) {
        scroll = (float) (rawScroll - scrollAnimation.getOutput());
        if (RenderUtil.isHovering(mainx,mainy + 35,350,300 - 35,mx,my)) {
            rawScroll += Mouse.getDWheel() / 4f;
        }
        rawScroll = Math.max(Math.min(minScroll, rawScroll), -maxScroll);
        scrollAnimation = new SmoothStepAnimation(ms, rawScroll - scroll, Direction.BACKWARDS);
    }
    public float getScroll() {
        scroll = (float) (rawScroll - scrollAnimation.getOutput());
        return scroll;
    }

    protected void mouseClicked(int mouseX, int mouseY, int mouseButton) {

        moduleRect.forEach(s -> s.mouseClicked(mouseX,mouseY,mouseButton));

    }
    public void mouseReleased(int mouseX, int mouseY, int state) {
        moduleRect.forEach(e ->e.mouseReleased(mouseX,mouseY,state));
    }
    public void keyTyped(char typedChar, int keyCode) {
        moduleRect.forEach(e ->e.keyTyped(typedChar,keyCode));
    }

    public boolean isHovered(int mouseX, int mouseY) {
        return RenderUtil.isHovering(mainx + 105 + x,mainy+ 14,Fonts.SF.SF_18.SF_18.stringWidth(newcatename(type)),Fonts.SF.SF_18.SF_18.getHeight()+2,mouseX,mouseY);
    }
}
