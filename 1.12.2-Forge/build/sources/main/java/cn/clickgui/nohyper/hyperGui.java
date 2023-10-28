package cn.clickgui.nohyper;

import cn.novoline.impl.Fonts;
import cn.utils.miku.animations.Animation;
import cn.utils.miku.animations.Direction;
import cn.utils.miku.animations.impl.EaseInOutQuad;
import cn.utils.miku.animations.impl.SmoothStepAnimation;
import cn.utils.miku.render.RenderUtil;
import cn.utils.miku.render.round.RoundedUtil;
import net.ccbluex.liquidbounce.utils.render.RenderUtils;
import net.ccbluex.liquidbounce.LiquidBounce;
import net.ccbluex.liquidbounce.features.module.ModuleCategory;
import net.ccbluex.liquidbounce.features.module.modules.render.ClickGUI;
import net.ccbluex.liquidbounce.ui.client.hud.designer.GuiHudDesigner;
import net.ccbluex.liquidbounce.utils.MinecraftInstance;
import net.ccbluex.liquidbounce.utils.render.RenderUtils;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiNewChat;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.gui.ScaledResolution;
import net.minecraft.client.renderer.entity.Render;
import net.minecraft.client.renderer.texture.DynamicTexture;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.common.Loader;
import org.lwjgl.input.Keyboard;
import org.lwjgl.input.Mouse;
import org.lwjgl.opengl.GL11;

import javax.imageio.ImageIO;
import java.awt.*;
import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import static net.ccbluex.liquidbounce.utils.MinecraftInstance.classProvider;

public class hyperGui extends GuiScreen {

    public int mainx = 240,mainy  = 90;

    private int x2;
    private int y2;
    private boolean dragging;

    private String msgtext = "";

    private List<TypeScreen> types = new ArrayList<>();

    private boolean head = true;

    private boolean hideirc = false , textboxKeyTyped;
    private final ResourceLocation hudIcon = new ResourceLocation( "liquidbounce/custom_hud_icon.png");

    private int fixy;

    public Animation ircani = new EaseInOutQuad(150,150, Direction.FORWARDS);

    public hyperGui (){
        int x = 0;
        for(ModuleCategory category : ModuleCategory.values()) {
            types.add(new TypeScreen(category,x));
            x +=  Fonts.SF.SF_18.SF_18.stringWidth(newcatename(category)) + 25;
        }
    }

    private float maxScroll = Float.MAX_VALUE, minScroll = 0, rawScroll;
    private float scroll;
    private Animation scrollAnimation = new SmoothStepAnimation(0, 0, Direction.BACKWARDS);

    @Override
    public void initGui() {
      //  msgtext = new TokenField(1,mc.fontRenderer,mainx + 364,mainy + 271,120,10,"");
        super.initGui();
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
    /**
     * Main Color 24,28,33 imga 4000 1080
     */
    @Override
    public void drawScreen(int mouseX, int mouseY, float partialTicks) {

        ScaledResolution res = new ScaledResolution(mc);

        if (Mouse.isButtonDown(0) && mouseX >= 5 && mouseX <= 50 && mouseY <= height - 5 && mouseY >= height - 50)
            MinecraftInstance.mc.displayGuiScreen(classProvider.wrapGuiScreen(new GuiHudDesigner()));


        if (getSelectedTab() == null){
            if (!types.isEmpty()) {
                types.get(0).setSelected(true);
            }
        }

        //移动面板
        if(dragging) {
            this.mainx = x2 + mouseX;
            this.mainy = y2 + mouseY;
        }

        RenderUtil.drawImage(hudIcon, 9, height - 41, 32, 32);


        RoundedUtil.drawRound(mainx,mainy,!hideirc ? 500 : 350,300,5,new Color(24,28,33));

        RenderUtil.drawImage(new ResourceLocation("liquidbounce/hyper.png"),mainx  , mainy + 4,4000/40,1080/40);

        ircani.setDirection(hideirc ? Direction.BACKWARDS : Direction.FORWARDS);

        RoundedUtil.drawRound(!hideirc ?mainx+ 498 : mainx+ 348 ,mainy + 130 ,2,20,1,new Color(ClickGUI.colorValue.getValue()));

        types.forEach(e -> e.draw(mouseX,mouseY));

        super.drawScreen(mouseX, mouseY, partialTicks);
    }

    @Override
    public void mouseClicked(int mouseX, int mouseY, int mouseButton) throws IOException {
        if (mouseButton == 0) {
            if (RenderUtil.isHovering(!hideirc ?mainx+ 498 : mainx+ 348 ,mainy + 130 ,2,20,mouseX,mouseY)){
                hideirc = !hideirc;
            }
            //选择面板
            for (TypeScreen typeScreen : types) {
                if (typeScreen.isHovered(mouseX, mouseY)) {
                    for (TypeScreen other : types) {
                        //判断是否是当前已经所选
                        other.setSelected(false);
                    }

                    typeScreen.setSelected(true);
                }
            }
        }
        TypeScreen selectedTab = getSelectedTab();
        if(selectedTab != null) selectedTab.mouseClicked(mouseX,mouseY,mouseButton);

        //移动面板
        if(RenderUtil.isHovering(mainx,mainy,500,35,mouseX, mouseY)) {
            this.x2 = (int) (mainx - mouseX);
            this.y2 = (int) (mainy - mouseY);
            this.dragging = true;
        }

        if (mouseButton == 0 && !textboxKeyTyped && RenderUtil.isHovering(mainx + 360,mainy + 266,100,16,mouseX,mouseY) && !hideirc){
            textboxKeyTyped = true;
        }else {
            textboxKeyTyped = false;
        }

        if (RenderUtil.isHovering(mainx + 465,mainy + 266,16,16,mouseX,mouseY)){
            if (!(Objects.equals(msgtext, ""))){
                msgtext = ("");
            }
        }

    }

    @Override
    protected void mouseReleased(int mouseX, int mouseY, int state) {
        types.forEach(e -> e.mouseReleased(mouseX,mouseY,state));
        if(state == 0) {
            this.dragging = false;
        }

        super.mouseReleased(mouseX, mouseY, state);
    }

    @Override
    protected void keyTyped(char typedChar, int keyCode) throws IOException {
        types.forEach(e -> e.keyTyped(typedChar,keyCode));
        //输入
        if (textboxKeyTyped  && !hideirc) {
            if (keyCode == Keyboard.KEY_ESCAPE) {
                textboxKeyTyped = false;
            } else if (!(keyCode == Keyboard.KEY_BACK) && keyCode != Keyboard.KEY_RCONTROL && keyCode != Keyboard.KEY_LCONTROL && keyCode != Keyboard.KEY_RSHIFT && keyCode != Keyboard.KEY_LSHIFT && keyCode != Keyboard.KEY_TAB && keyCode != Keyboard.KEY_CAPITAL && keyCode != Keyboard.KEY_DELETE && keyCode != Keyboard.KEY_HOME && keyCode != Keyboard.KEY_INSERT && keyCode != Keyboard.KEY_UP && keyCode != Keyboard.KEY_DOWN && keyCode != Keyboard.KEY_RIGHT && keyCode != Keyboard.KEY_LEFT && keyCode != Keyboard.KEY_LMENU && keyCode != Keyboard.KEY_RMENU && keyCode != Keyboard.KEY_PAUSE && keyCode != Keyboard.KEY_SCROLL && keyCode != Keyboard.KEY_END && keyCode != Keyboard.KEY_PRIOR && keyCode != Keyboard.KEY_NEXT && keyCode != Keyboard.KEY_APPS && keyCode != Keyboard.KEY_F1 && keyCode != Keyboard.KEY_F2 && keyCode != Keyboard.KEY_F3 && keyCode != Keyboard.KEY_F4 && keyCode != Keyboard.KEY_F5 && keyCode != Keyboard.KEY_F6 && keyCode != Keyboard.KEY_F7 && keyCode != Keyboard.KEY_F8 && keyCode != Keyboard.KEY_F9 && keyCode != Keyboard.KEY_F10 && keyCode != Keyboard.KEY_F11 && keyCode != Keyboard.KEY_F12) {
                msgtext = msgtext + typedChar;
            }

            //删除
            if (textboxKeyTyped && Keyboard.isKeyDown(Keyboard.KEY_BACK) && msgtext.length() >= 1) {
                msgtext = (msgtext.substring(0, msgtext.length() - 1));
            }
        }
        super.keyTyped(typedChar, keyCode);
    }

    public TypeScreen getSelectedTab() {
        return types.stream().filter(TypeScreen::isSelected).findAny().orElse(null);
    }

    //滚轮
    public void onScroll(int ms,int mx ,int my) {
        scroll = (float) (rawScroll - scrollAnimation.getOutput());
        if (RenderUtil.isHovering(mainx + 350,mainy + 35,140,255,mx,my)) {
            rawScroll += Mouse.getDWheel() / 4f;
        }
        rawScroll = Math.max(Math.min(minScroll, rawScroll), -maxScroll);
        scrollAnimation = new SmoothStepAnimation(ms, rawScroll - scroll, Direction.BACKWARDS);
    }
    public float getScroll() {
        scroll = (float) (rawScroll - scrollAnimation.getOutput());
        return scroll - fixy;
    }

}
