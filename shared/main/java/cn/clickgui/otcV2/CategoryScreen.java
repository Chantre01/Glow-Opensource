package cn.clickgui.otcV2;

import cn.Client;
import cn.novoline.impl.Fonts;
import cn.clickgui.otcV2.Utils.Position;
import cn.utils.miku.animations.Animation;
import cn.utils.miku.animations.Direction;
import cn.utils.miku.animations.impl.SmoothStepAnimation;
import cn.utils.miku.math.MathUtil;
import cn.utils.miku.render.RenderUtil;

import cn.utils.miku.render.round.RoundedUtil;
import net.ccbluex.liquidbounce.LiquidBounce;
import net.ccbluex.liquidbounce.features.module.Module;
import net.ccbluex.liquidbounce.features.module.ModuleCategory;
import net.ccbluex.liquidbounce.utils.render.RenderUtils;
import org.lwjgl.input.Mouse;
import org.lwjgl.opengl.GL11;

import java.awt.*;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

public class CategoryScreen {

    private float maxScroll = Float.MAX_VALUE, minScroll = 0, rawScroll;

    private float scroll;

    public Position pos;

    private ModuleCategory category = ModuleCategory.COMBAT;
    private float x;

    private float categoryX;
    private float categoryY;

    //选择大类
    private boolean selected;

    private final List<ModuleRender> moduleList = new CopyOnWriteArrayList<>();

    private Animation scrollAnimation = new SmoothStepAnimation(0, 0, Direction.BACKWARDS);

    public String newcatename(ModuleCategory moduleCategory){
        if (moduleCategory.getDisplayName().equals("Combat")){
            return "combat";
        }else if (moduleCategory.getDisplayName().equals("Player")){
            return "player";
        }else if (moduleCategory.getDisplayName().equals("Movement")){
            return "move";
        }else if (moduleCategory.getDisplayName().equals("Render")){
            return "visuals";
        }else if (moduleCategory.getDisplayName().equals("World")){
            return "world";
        }else if (moduleCategory.getDisplayName().equals("Misc")){
            return "misc";
        }else if (moduleCategory.getDisplayName().equals("Exploit")){
            return "exploit";
        }
        return "";
    }
    
    public CategoryScreen(ModuleCategory category, float x) {
        this.category = category;
        this.x = x;

        this.pos = new Position(0, 0, 0, 0);

        int count = 0;

        //new ly ry
        int leftAdd = 0;
        int rightAdd = 0;

        for (final Module module : LiquidBounce.moduleManager.getModuleInCategory(this.category)) {
                //奇偶判断
                float posWidth = 0;

                //判断左右分别添加
                float posX = pos.x + ((count % 2 == 0) ? 0 : 145);
                float posY = pos.y + ((count % 2 == 0) ? leftAdd : rightAdd);

                Position pos = new Position(posX, posY, posWidth, 30);
                //只需要x y height
                ModuleRender otlM = new ModuleRender(module, pos.x, pos.y, pos.width, pos.height);
                pos.height = otlM.height;
                if (count % 2 == 0) {
                    leftAdd += pos.height + 20;
                } else {
                    rightAdd += pos.height + 20;
                }
                moduleList.add(otlM);
                count++;
        }
    }

    public void drawScreen(int mouseX, int mouseY) {
        try{
        categoryX = Client.instance.otc.getMainx();
        categoryY = Client.instance.otc.getMainy();

        if (selected ) {

            double scrolll = getScroll();
            for (ModuleRender module : moduleList)
            {
                module.scrollY = (int) MathUtil.roundToHalf(scrolll);
            }

            onScroll(30);
            maxScroll = Math.max(0,moduleList.get(moduleList.size() -1).getY() + moduleList.get(moduleList.size() -1).height*2 + 2500) ;
        }

        Fonts.SFBOLD.SFBOLD_13.SFBOLD_13.drawString(newcatename(category),
                x + categoryX + 77, categoryY - 29, 0xFFFFFFFF);

        if (selected){
            RoundedUtil.drawRound(x + categoryX + 76 ,categoryY - 29 -3,Fonts.SFBOLD.SFBOLD_13.SFBOLD_13.stringWidth(newcatename(category)) + 2,5+ 4,1,new Color(255,255,255,60));
          //  RenderUtils.drawRect();
            //隐藏不可见的
            GL11.glPushMatrix();
            RenderUtil.scissor(0, Client.instance.otc.getMainy() , 1920,   300 );
          //  GL11.glScissor(0, (int) (LiquidBounce.otc.sHeight() - LiquidBounce.otc.getMainy() * 2  + 375 - LiquidBounce.otc.getHeight() * 2) + 40, 1920, LiquidBounce.otc.getHeight() * 2 - 42 -375 );
            GL11.glEnable(GL11.GL_SCISSOR_TEST);
            moduleList.stream() ////
                    .sorted((o1, o2) -> Boolean.compare(o1.isSelected(), o2.isSelected())) //
                    .forEach(module -> module.drawScreen(mouseX, mouseY));

            GL11.glDisable(GL11.GL_SCISSOR_TEST);
            GL11.glPopMatrix();
        }

        if (isHovered(mouseX,mouseY)){
            RoundedUtil.drawRound(x + categoryX + 76 ,categoryY - 29 -3, Fonts.SFBOLD.SFBOLD_13.SFBOLD_13.stringWidth(newcatename(category)) + 2,5+ 4,1,new Color(255,255,255,60));
        }

        } catch (NullPointerException e) {
            e.printStackTrace();
        }
    }

    public void onScroll(int ms) {
        scroll = (float) (rawScroll - scrollAnimation.getOutput());
        rawScroll += Mouse.getDWheel() / 4f;
        rawScroll = Math.max(Math.min(minScroll, rawScroll), -maxScroll);
        scrollAnimation = new SmoothStepAnimation(ms, rawScroll - scroll, Direction.BACKWARDS);
    }
    public float getScroll() {
        scroll = (float) (rawScroll - scrollAnimation.getOutput());
        return scroll;
    }

    public boolean isHovered(int mouseX, int mouseY) {
        return mouseX >= x + categoryX + 76 && mouseX <= x + categoryX + 76 + Fonts.SFBOLD.SFBOLD_13.SFBOLD_13.stringWidth(newcatename(category)) + 2 && mouseY >= categoryY - 29 -3 && mouseY <= categoryY - 29 -3 + 9;
    }

    protected void mouseClicked(int mouseX, int mouseY, int mouseButton) {

        moduleList.forEach(s -> s.mouseClicked(mouseX,mouseY,mouseButton));

    }
    public void mouseReleased(int mouseX, int mouseY, int state) {
        moduleList.forEach(e ->e.mouseReleased(mouseX,mouseY,state));
    }
    public void keyTyped(char typedChar, int keyCode) {
        moduleList.forEach(e ->e.keyTyped(typedChar,keyCode));
    }
    public void setSelected(boolean s){
        this.selected = s;
    }


    public boolean isSelected() {
        return this.selected;
    }
}
