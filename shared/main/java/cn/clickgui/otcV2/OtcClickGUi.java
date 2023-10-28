package cn.clickgui.otcV2;

import cn.novoline.impl.Fonts;
import cn.clickgui.otcV2.Utils.OtcScroll;
import cn.utils.miku.render.RenderUtil;

import cn.utils.miku.render.round.RoundedUtil;
import cn.utils.render.VisualBase;
import net.ccbluex.liquidbounce.LiquidBounce;
import net.ccbluex.liquidbounce.features.module.ModuleCategory;
import net.ccbluex.liquidbounce.ui.client.hud.designer.GuiHudDesigner;
import net.ccbluex.liquidbounce.utils.MinecraftInstance;
import net.ccbluex.liquidbounce.utils.render.RenderUtils;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.gui.ScaledResolution;
import net.minecraft.util.ResourceLocation;
import org.lwjgl.input.Mouse;

import java.awt.*;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import static net.ccbluex.liquidbounce.features.module.modules.render.UIEffects.Hudcolor;
import static net.ccbluex.liquidbounce.utils.MinecraftInstance.classProvider;


/**
 * Update to 042122
 * @author qingjiu
 * Font: SFBold
 * ui by onetapv2
 */
public class OtcClickGUi extends GuiScreen {

    private float mainx = 320,x = 0;
    private float mainy = 130;
    private float hight = 120;

    private int x2;
    private int y2;
    private boolean dragging;


    private final List<CategoryScreen> tabs = new ArrayList<>();

    public int sHeight() {
        return super.height * 2;
    }
    private final ResourceLocation hudIcon = new ResourceLocation( "liquidbounce/custom_hud_icon.png");

    public OtcClickGUi() {
        for(ModuleCategory category : ModuleCategory.values()) {
                tabs.add(new CategoryScreen(category, x));
                this.x += Fonts.SFBOLD.SFBOLD_13.SFBOLD_13.stringWidth(newcatename(category)) + 10;
            }
    }

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

    @Override
    public void initGui() {
        super.initGui();
    }

    @Override
    public void drawScreen(int mouseX, int mouseY, float partialTicks) {
        try {
            //移动面板
            if (dragging) {
                this.mainx = x2 + mouseX;
                this.mainy = y2 + mouseY;
            }

            final float[] hudHSB = Hudcolor.getHSB();
            Color color = Color.getHSBColor(hudHSB[0], hudHSB[1], 0.5f);

            if (Mouse.isButtonDown(0) && mouseX >= 5 && mouseX <= 50 && mouseY <= height - 5 && mouseY >= height - 50)
                MinecraftInstance.mc.displayGuiScreen(classProvider.wrapGuiScreen(new GuiHudDesigner()));

            ScaledResolution scaledResolution = new ScaledResolution(mc);

            RenderUtil.drawRect(0, 0, scaledResolution.getScaledWidth(), scaledResolution.getScaledHeight(), new Color(0, 0, 0, 120).getRGB());

            //二次元
            // RenderUtil.drawImage(new ResourceLocation("liquidbounce/csgo/miku3.png"),600,0,696/2,1280/2);

            RenderUtil.drawImage(hudIcon, 9, height - 41, 32, 32);

            //背景
            RoundedUtil.drawRound(mainx, mainy, 290, hight + 180, 3, new Color(44, 47, 56));
            RoundedUtil.drawRound(mainx, mainy - 50, 290f, hight - 80, 3, new Color(44, 47, 56));


            RoundedUtil.drawGradientHorizontal(mainx, mainy - 50, 290f, hight - 116, 3, color, new Color(Hudcolor.get()));

            Fonts.SFBOLD.SFBOLD_18.SFBOLD_18.drawString("Glow.today", mainx + 11, mainy - 31, new Color(255, 255, 255).getRGB());

            RoundedUtil.drawRound(mainx + 64, mainy - 35, 0.5f, hight - 105, 1, new Color(255, 255, 255, 150));

            CategoryScreen selectedTab = getSelectedTab();
            if (selectedTab == null) {
                net.ccbluex.liquidbounce.ui.font.Fonts.minecraftFont.drawString("-------------", (int) mainx + 109, (int) mainy + 40, new Color(255, 255, 255).getRGB());
                net.ccbluex.liquidbounce.ui.font.Fonts.minecraftFont.drawString("Select one of", (int) mainx + 109, (int) mainy + 50, new Color(255, 255, 255).getRGB());
                net.ccbluex.liquidbounce.ui.font.Fonts.minecraftFont.drawString("-------------", (int) mainx + 109, (int) mainy + 60, new Color(255, 255, 255).getRGB());

                net.ccbluex.liquidbounce.ui.font.Fonts.minecraftFont.drawString("Enjoy Glow Client", (int) mainx + 107, (int) mainy + 75, new Color(255, 255, 255).getRGB());
                net.ccbluex.liquidbounce.ui.font.Fonts.minecraftFont.drawString("Sk1d by Lemon. ", (int) mainx + 110, (int) ((int) mainy + 290), new Color(255, 255, 255).getRGB());
            }

            //开始绘制categoryScreen内的drawScreen
            tabs.forEach(s -> s.drawScreen(mouseX, mouseY));

            super.drawScreen(mouseX, mouseY, partialTicks);
        } catch (NullPointerException e) {
            e.printStackTrace();
        }
    }

    @Override
    protected void mouseClicked(int mouseX, int mouseY, int mouseButton) throws IOException {

        if (mouseButton == 0){
            //选择面板
                for (CategoryScreen categoryScreen : tabs) {
                    if (categoryScreen.isHovered(mouseX, mouseY)) {
                        for (CategoryScreen other : tabs) {
                            //判断是否是当前已经所选
                            other.setSelected(false);
                        }
                        categoryScreen.setSelected(true);
                    }
                }
        }

        //移动面板
        if(isHovered(mouseX, mouseY)) {
            this.x2 = (int) (mainx - mouseX);
            this.y2 = (int) (mainy - mouseY);
            this.dragging = true;
        }

        //判断是否是已经选择的
        CategoryScreen selectedTab = getSelectedTab();
        if(selectedTab != null) selectedTab.mouseClicked(mouseX, mouseY, mouseButton);

        super.mouseClicked(mouseX, mouseY, mouseButton);
    }

    public CategoryScreen getSelectedTab() {
        return tabs.stream().filter(CategoryScreen::isSelected).findAny().orElse(null);
    }

    private boolean isHovered(int mouseX, int mouseY) {
        return mouseX >= mainx && mouseX <= mainx + 45 + 105 + 270f && mouseY >= mainy-50 - 7 && mouseY <= mainy-50 + 20;
    }

    public static OtcScroll scroll() {
        int mouse = Mouse.getDWheel(); // @off

        if(mouse > 0)
            return OtcScroll.UP;
        else if(mouse < 0)
            return OtcScroll.DOWN;
        else
            return null;
    } // @on

    @Override
    protected void mouseReleased(int mouseX, int mouseY, int state) {
        if(state == 0) {
            this.dragging = false;
        }

        tabs.forEach(e -> e.mouseReleased(mouseX,mouseY,state));
        super.mouseReleased(mouseX, mouseY, state);
    }

    @Override
    protected void keyTyped(char typedChar, int keyCode) throws IOException {
        tabs.forEach(e -> e.keyTyped(typedChar,keyCode));
        super.keyTyped(typedChar,keyCode);
    }
    public float getMainx(){
        return mainx;
    }
    public float getMainy(){
        return mainy;
    }

    public float getX2(){
        return x2;
    }

    public float getY2(){
        return y2;
    }

    public int getHeight() {
        return (int) hight;
    }

}
