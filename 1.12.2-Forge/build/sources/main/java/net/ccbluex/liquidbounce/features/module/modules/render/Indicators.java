package net.ccbluex.liquidbounce.features.module.modules.render;

import cn.font.FontLoaders;
import cn.utils.AnimationHelper;

import cn.novoline.impl.Fonts;
import cn.utils.TimerUtil;
import cn.utils.miku.render.RenderUtil;
import cn.utils.miku.render.round.RoundedUtil;
import cn.utils.render.VisualBase;
import net.ccbluex.liquidbounce.LiquidBounce;
import net.ccbluex.liquidbounce.api.enums.WDefaultVertexFormats;
import net.ccbluex.liquidbounce.api.minecraft.client.render.ITessellator;
import net.ccbluex.liquidbounce.api.minecraft.client.render.IWorldRenderer;
import net.ccbluex.liquidbounce.api.minecraft.util.IScaledResolution;
import net.ccbluex.liquidbounce.event.EventTarget;
import net.ccbluex.liquidbounce.event.Render2DEvent;
import net.ccbluex.liquidbounce.features.module.Module;
import net.ccbluex.liquidbounce.features.module.ModuleCategory;
import net.ccbluex.liquidbounce.features.module.ModuleInfo;
import net.ccbluex.liquidbounce.features.module.modules.render.HUD;
import net.ccbluex.liquidbounce.utils.render.RenderUtils;
import net.ccbluex.liquidbounce.utils.timer.TimeUtils;
import net.ccbluex.liquidbounce.value.BoolValue;
import net.ccbluex.liquidbounce.value.IntegerValue;
import net.ccbluex.liquidbounce.value.ListValue;
import net.minecraft.client.Minecraft;
import net.minecraft.client.entity.AbstractClientPlayer;
import net.minecraft.client.gui.Gui;
import net.minecraft.client.gui.ScaledResolution;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.vertex.DefaultVertexFormats;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.ResourceLocation;
import org.lwjgl.opengl.GL11;

import java.awt.*;

import static net.ccbluex.liquidbounce.features.module.modules.render.UIEffects.Hudcolor;

@ModuleInfo(name = "Indicators", description = "www", category = ModuleCategory.RENDER)
public class Indicators extends Module {



    //private Animation open = new EaseInOutQuad(300, 12, Direction.FORWARDS) ;

    public BoolValue key = new BoolValue("KeyBinds",true);
    public ListValue indType = new ListValue("IndicatorsStyle", new String[]{"Basic", "Akrien", "Akrien2"}, "Basic");
    public ListValue akrienType = new ListValue("Akrien2-Theme", new String[]{"White", "Dark", "Blue"}, "White");

    public BoolValue ind = new BoolValue("Indicators",false);
    public BoolValue akrienGlow = new BoolValue("Indicators-AkrienGlow", false);
    public IntegerValue akrienGlowBlur = new IntegerValue("Indicators-AkrienGlowBlur", 1,0,5);

    public BoolValue spe = new BoolValue("Spectators",false);

    //Save ConFig
    public IntegerValue keyx = new IntegerValue("x",20,0,1000);

    public IntegerValue keyy = new IntegerValue("y",80,0,1000);

    public IntegerValue indx = new IntegerValue("x2",120,0,1000);

    public IntegerValue indy = new IntegerValue("y2",80,0,1000);

    public IntegerValue spex = new IntegerValue("x3",220,0,1000);

    public IntegerValue spey = new IntegerValue("y3",80,0,1000);

    public int x = keyx.get(), y = keyy.get();

    public int x2 = indx.get(), y3 = indy.get();

    public int x3 = spex.get(), y4 = spey.get();

    public boolean keybool,indbool,spebool;
    private double armorBarWidth;
    private double armorHeight;

    private double hurttimeBarWidth;
    private double hurtHeight;

    private double bpsBarWidth;
    private double bpsHeight;

    private double hpBarWidth;
    private double healthBarWidth;

    ScaledResolution sr = new ScaledResolution(Minecraft.getMinecraft());
    final float scaledWidth = sr.getScaledWidth();
    final float scaledHeight = sr.getScaledHeight();

    TimerUtil timerHelper = new TimerUtil();

    @EventTarget
    public void onRender(Render2DEvent event){
        x = keyx.get();
        y = keyy.get();

        x2 = indx.get();
        y3 = indy.get();

        x3 = spex.get();
        y4 = spey.get();

        //KeyBinds
        if (key.get()) {
            renderkey();
        }

        //Indicators
        if (ind.get()) {
            renderind();
        }

        //Spectators
        if (spe.get()) {
            renderspe();
        }
    }
    public void renderkey(){
        if (!key.get())return;
        int y2 =0;
        RoundedUtil.drawRound(x, y, 80, 17 + getmoduley(), 1, new Color(22, 18, 27));
        RoundedUtil.drawRound(x, y, 80, 15, 0, new Color(28, 30, 40));
        RoundedUtil.drawRound(x, y + 16f, 80, 0.7f, 0, new Color(42, 39, 44));
        RoundedUtil.drawRound(x - 0.5f, y - 1.7f, 81f, 1.2f, 0.5f, new Color(Hudcolor.getValue()));

        //文字
        Fonts.CsgoIcon.csgoicon_18.csgoicon_18.drawString("a", x + 3, y + 6.5f, -1);
        Fonts.SFTHIN.SFTHIN_22.SFTHIN_22.drawString("|", x + 17, y + 4f, new Color(47, 51, 52).getRGB());
        Fonts.SFTHIN.SFTHIN_16.SFTHIN_16.drawString("KeyBinds", x + 23, y + 5.5f, -1);

        for (Module module : LiquidBounce.moduleManager.getModules()) {
            if (module.getKeyBind() == 0) continue;
            if (!module.getState()) continue;
            Fonts.Tahoma.Tahoma_14.Tahoma_14.drawString(module.getName(), x + 3, y + y2 + 21f, -1);

            Fonts.Tahoma.Tahoma_14.Tahoma_14.drawString("[toggled]", x + 78 - Fonts.Tahoma.Tahoma_14.Tahoma_14.stringWidth("[toggled]"), y + y2 + 21f, new Color(100, 100, 100).getRGB());

            RoundedUtil.drawRound(x + 5, y + y2 + 28f, 70, 0.3f, 0, new Color(100, 100, 100));

            y2 += 12;
        }
    }

    public void renderind(){
        if (!ind.get())return;

        switch (indType.get()) {
            case "Basic":
                RoundedUtil.drawRound(x2, y3, 80, 17 + 17, 1, new Color(22, 18, 27));
                RoundedUtil.drawRound(x2, y3, 80, 15, 0, new Color(28, 30, 40));
                RoundedUtil.drawRound(x2, y3 + 16f, 80, 0.7f, 0, new Color(42, 39, 44));
                RoundedUtil.drawRound(x2 - 0.5f, y3 - 1.7f, 81f, 1.2f, 0.5f, new Color(Hudcolor.getValue()));

                //文字
                Fonts.CsgoIcon.csgoicon_18.csgoicon_18.drawString("l", x2 + 3, y3 + 6.5f, -1);
                Fonts.SFTHIN.SFTHIN_22.SFTHIN_22.drawString("|", x2 + 17, y3 + 4f, new Color(47, 51, 52).getRGB());
                Fonts.SFTHIN.SFTHIN_16.SFTHIN_16.drawString("Indicators", x2 + 23, y3 + 5.5f, -1);

                double speed = calculateBPS();

                speed = (float) RenderUtil.animate(8, Math.min(9, speed), 0.05);

                Fonts.Tahoma.Tahoma_14.Tahoma_14.drawString("Speed", x2 + 20, y3 + 24f, -1);

                Fonts.Tahoma.Tahoma_14.Tahoma_14.drawString("[ " + String.format("%.2f", speed) + "b/s ]", x2 + 76 - Fonts.Tahoma.Tahoma_14.Tahoma_14.stringWidth("[ " + String.format("%.2f", speed) + "b/s ]"), y3 + 24f, new Color(100, 100, 100).getRGB());

                GL11.glPopMatrix();
                RenderUtil.drawArc(x2 + 9, y3 + 26, 3.5, new Color(60, 60, 60).darker().getRGB(), 180, 360 * 2, 2);
                RenderUtil.drawArc(x2 + 9, y3 + 26, 3.5, Hudcolor.getValue(), 180 * 2, 340 + speed * 45, 2);
                RenderUtil.resetColor();
                GL11.glPushMatrix();
                break;
            case "Akrien":
                double prevZ = mc2.player.posZ - mc2.player.prevPosZ;
                double prevX = mc2.player.posX - mc2.player.prevPosX;
                double lastDist = Math.sqrt(prevX * prevX + prevZ * prevZ);
                double currSpeed = lastDist * 15.3571428571D / 4;

                final float xX = scaledWidth / 2.0f - x2;
                final float yX = scaledHeight / 2.0f + y3;
                VisualBase.drawNewRect(xX + 4.5, yX  + 196.5 - 405, xX + 100.5, yX + 246.5 - 408, new Color(11, 11, 11, 255).getRGB());
                VisualBase.drawNewRect(xX + 5, yX + 198 - 405, xX + 100, yX + 246 - 408, new Color(28, 28, 28, 255).getRGB());
                VisualBase.drawNewRect(xX + 5, yX  + 198 - 405, xX + 100, yX + 208 - 408, new Color(21, 19, 20, 255).getRGB());
                VisualBase.drawNewRect(xX + 44, yX + 210 - 406, xX + 95, yX + 213.5 - 406, new Color(41, 41, 41, 255).getRGB());
                VisualBase.drawNewRect(xX + 44, yX + 219 - 406, xX + 95, yX + 222.5 - 406, new Color(41, 41, 41, 255).getRGB());
                VisualBase.drawNewRect(xX + 44, yX + 228 - 406, xX + 95, yX + 231.5 - 406, new Color(41, 41, 41, 255).getRGB());
                VisualBase.drawNewRect(xX + 44, yX + 237 - 406, xX + 95, yX + 240.5 - 406, new Color(41, 41, 41, 255).getRGB());
                VisualBase.drawNewRect(xX + 5, yX + 197 - 405, xX + 100, yX + 198 - 405, Hudcolor.get());

                if(!akrienGlow.get()) {
                    Fonts.SFTHIN.SFTHIN_14.SFTHIN_14.drawString("Indicators", xX + 37, yX + 202 - 406, -1);
                } else {
                    FontLoaders.jelloFontMedium14.drawBlurredString("Indicators", xX + 37, yX + 202 - 406, akrienGlowBlur.get(), new Color(255, 255, 255, 80), -1);
                }

                // armor
                final float armorValue = mc2.player.getTotalArmorValue();
                double armorPercentage = armorValue / 20;
                armorPercentage = MathHelper.clamp(armorPercentage, 0.0, 1.0);

                final double armorWidth = 51 * armorPercentage;
                this.armorBarWidth = AnimationHelper.animate(armorWidth, this.armorBarWidth, 0.0229999852180481);
                RenderUtil.drawRect(xX + 44, yX + 210 - 406, xX + 44 + this.armorBarWidth, yX + 213.5 - 406, Hudcolor.get());

                if(!akrienGlow.get()) {
                    Fonts.SFTHIN.SFTHIN_14.SFTHIN_14.drawString("Armor", xX + 8, yX + 211 - 406, -1);
                } else {
                    FontLoaders.jelloFontMedium14.drawBlurredString("Armor", xX + 8, yX + 211 - 406, akrienGlowBlur.get(), new Color(255, 255, 255, 80), -1);
                }

                // HurtTime
                double hurttimePercentage = MathHelper.clamp(mc2.player.hurtTime, 0.0, 0.6);
                final double hurttimeWidth = 51.0 * hurttimePercentage;
                this.hurttimeBarWidth = AnimationHelper.animate(hurttimeWidth, this.hurttimeBarWidth, 0.0429999852180481);
                RenderUtil.drawRect(xX + 44, yX + 219 - 406, xX + 44 + this.hurttimeBarWidth, yX + 222.5 - 406, Hudcolor.get());

                if(!akrienGlow.get()) {
                    Fonts.SFTHIN.SFTHIN_14.SFTHIN_14.drawString("HurtTime", xX + 8, yX + 220 - 406, -1);
                } else {
                    FontLoaders.jelloFontMedium14.drawBlurredString("HurtTime", xX + 8, yX + 220 - 406,akrienGlowBlur.get(),  new Color(255, 255, 255, 80), -1);
                }

                // HurtTime
                double bpsPercentage = MathHelper.clamp(currSpeed, 0.0, 1.0);
                final double bpsBarWidth = 51.0 * bpsPercentage;
                this.bpsBarWidth = AnimationHelper.animate(bpsBarWidth, this.bpsBarWidth, 0.0329999852180481);

                RenderUtil.drawRect(xX + 44, yX + 228 - 406, xX + 44 + this.bpsBarWidth, yX + 231.5 - 406, Hudcolor.get());

                if(!akrienGlow.get()) {
                    Fonts.SFTHIN.SFTHIN_14.SFTHIN_14.drawString("BPS", xX + 8, yX + 229 - 406, -1);
                } else {
                    FontLoaders.jelloFontMedium14.drawBlurredString("BPS", xX + 8, yX + 229 - 406, akrienGlowBlur.get(),  new Color(255, 255, 255, 80), -1);
                }

                // HurtTime
                final float health = mc2.player.getHealth();
                double hpPercentage = health / mc2.player.getMaxHealth();
                hpPercentage = MathHelper.clamp(hpPercentage, 0.0, 1.0);
                final double hpWidth = 51.0 * hpPercentage;
                final String healthStr = String.valueOf((int) mc2.player.getHealth() / 2.0f);

                if (timerHelper.hasReached(15L)) {
                    this.healthBarWidth = AnimationHelper.animate(hpWidth, this.healthBarWidth, 0.2029999852180481);
                    timerHelper.reset();
                }

                RenderUtil.drawRect(xX + 44, yX + 237 - 406, xX + 44 + this.healthBarWidth, yX + 240.5 - 406, Hudcolor.get());

                if(!akrienGlow.get()) {
                    Fonts.SFTHIN.SFTHIN_14.SFTHIN_14.drawString("HP", xX + 8, yX + 238 - 406, -1);
                } else {
                    FontLoaders.jelloFontMedium14.drawBlurredString("HP", xX + 8, yX + 238 - 406, akrienGlowBlur.get(),  new Color(255, 255, 255, 80), -1);
                }

                break;
            case "Akrien2":
                final float scaledWidth = sr.getScaledWidth();
                final float scaledHeight = sr.getScaledHeight();

                final float xR = scaledWidth / 2.0f - x2;
                final float yR = scaledHeight / 2.0f + y3;
                int colortext = -1;
                int colortext1 = -1;
                int color = -1;
                int colorrect = -1;
                int colorrectnext = -1;

                if(akrienType.get().equalsIgnoreCase("White")) {
                    color = new Color(60, 61, 60).getRGB();
                    colortext = new Color(245, 245, 245, 155).getRGB();
                    colortext1 = new Color(245, 245, 245, 145).getRGB();
                    colorrect = new Color(91, 91, 91, 255).getRGB();
                    colorrectnext = new Color(131, 131, 131).getRGB();
                } else if(akrienType.get().equalsIgnoreCase("Dark")) {
                    color = -1;
                    colortext = new Color(33, 33, 33, 215).getRGB();
                    colortext1 = new Color(33, 33, 33, 145).getRGB();
                    colorrect = new Color(194, 194, 194, 255).getRGB();
                    colorrectnext = new Color(133, 133, 133).getRGB();
                } else if(akrienType.get().equalsIgnoreCase("Blue")) {
                    color = -1;
                    colortext = new Color(9, 144, 255, 215).getRGB();
                    colortext1 = new Color(60, 135, 250, 145).getRGB();
                    colorrect = new Color(19, 113, 253, 255).getRGB();
                    colorrectnext = new Color(0, 165, 255).getRGB();
                }

                VisualBase.drawNewRect(xR + 4.5, yR + 196.5 - 403, xR + 100.5, yR + 246.5 - 445, colortext);
                VisualBase.drawNewRect(xR + 5, yR + 199.5 - 398, xR + 100, yR + 246 - 424, colortext1);
                VisualBase.drawNewRect(xR + 46, yR + 210 - 404, xR + 97, yR + 213.5 - 404, new Color(41, 41, 41, 255).getRGB());
                VisualBase.drawNewRect(xR + 46, yR + 219 - 404, xR + 97, yR + 222.5 - 404, new Color(41, 41, 41, 255).getRGB());
                RenderUtil.drawGradientRect(xR + 5, yR + 246.5 - 445, xR + 100, yR + 248.5 - 445, new Color(1, 1, 1, 90).getRGB(), new Color(0,0,0,0).getRGB());

                Fonts.SFTHIN.SFTHIN_14.SFTHIN_14.drawString("Indicators", xR + 6, yR + 202 - 406, color);

                final float healthR = mc2.player.getHealth();
                double hpPercentageR = healthR / mc2.player.getMaxHealth();
                hpPercentage = MathHelper.clamp(hpPercentageR, 0.0, 1.0);
                final double hpWidthR = 51.0 * hpPercentage;

                if (timerHelper.hasReached(15L)) {
                    this.healthBarWidth = AnimationHelper.animate(hpWidthR, this.healthBarWidth, 0.2029999852180481);
                    timerHelper.reset();
                }

                Fonts.SFTHIN.SFTHIN_14.SFTHIN_14.drawString("Health", xR + 8, yR + 211 - 405, color);
                VisualBase.drawGradientRect(xR + 46, yR + 210 - 404, xR + 46 + this.healthBarWidth, yR + 213.5 - 404, colorrect, colorrectnext);

                // HurtTime.
                double hurttimePercentageR = MathHelper.clamp(mc2.player.hurtTime, 0.0, 0.6);
                final double hurttimeWidthR = 51.0 * hurttimePercentageR;
                this.hurttimeBarWidth = AnimationHelper.animate(hurttimeWidthR, this.hurttimeBarWidth, 0.0429999852180481);
                VisualBase.drawGradientRect(xR + 46, yR + 219 - 404, xR + 46 + this.hurttimeBarWidth, yR + 222.5 - 404, colorrect, colorrectnext);
                Fonts.SFTHIN.SFTHIN_14.SFTHIN_14.drawString("HurtTime", xR + 8, yR + 220 - 405, color);
                break;
        }

    }

    public void renderspe(){
        if (!spe.get())return;

        int playy = 0;
        RoundedUtil.drawRound(x3, y4, 80, 17 + gety4(), 1, new Color(22, 18, 27));
        RoundedUtil.drawRound(x3, y4, 80, 15, 0, new Color(28, 30, 40));
        RoundedUtil.drawRound(x3, y4 + 16f, 80, 0.7f, 0, new Color(42, 39, 44));
        RoundedUtil.drawRound(x3 - 0.5f, y4 - 1.7f, 81f, 1.2f, 0.5f, new Color(Hudcolor.getValue()));

        //文字
        Fonts.CsgoIcon.csgoicon_18.csgoicon_18.drawString("F", x3 + 3, y4 + 6.5f, -1);
        Fonts.SFTHIN.SFTHIN_22.SFTHIN_22.drawString("|", x3 + 17, y4 + 4f, new Color(47, 51, 52).getRGB());
        Fonts.SFTHIN.SFTHIN_16.SFTHIN_16.drawString("Spectators", x3 + 23, y4 + 5.5f, -1);

        //循环世界所有玩家
        for (EntityPlayer  player:mc2.world.playerEntities){
            if (player == mc2.player)continue;
            //大于7就不再添加 防止超出屏幕
        //   if (mc2.world.playerEntities.size() >5)continue;
            quickDrawHead(((AbstractClientPlayer)  player).getLocationSkin(), x3+ 3 , y4 +20+ playy,12,12);
            net.ccbluex.liquidbounce.feng.FontLoaders.C14.drawString(player.getName(), x3 + 20, y4 + 24f + playy, -1);
            playy +=17;
         //   if (mc2.world.playerEntities.size() > 5){
          //      playy += 17;
           //     Fonts.Tahoma.Tahoma_14.Tahoma_14.drawString("Mouse shows all here", x3 + 3, y4 + 24f + playy, -1);
           // }
        }
    }

    public static void drawScaledCustomSizeModalRect(int x, int y, float u, float v, int uWidth, int vHeight, int width,
                                                     int height, float tileWidth, float tileHeight) {
        float f = 1.0F / tileWidth;
        float f1 = 1.0F / tileHeight;
        ITessellator tessellator = classProvider.getTessellatorInstance();
        IWorldRenderer worldrenderer = tessellator.getWorldRenderer();
        worldrenderer.begin(GL11.GL_QUADS, classProvider.getVertexFormatEnum(WDefaultVertexFormats.POSITION_TEX));
        worldrenderer.pos(x, y + height, 0.0D).tex(u * f, (v + (float) vHeight) * f1).endVertex();
        worldrenderer.pos(x + width, y + height, 0.0D).tex((u + (float) uWidth) * f, (v + (float) vHeight) * f1)
                .endVertex();
        worldrenderer.pos(x + width, y, 0.0D).tex((u + (float) uWidth) * f, v * f1).endVertex();
        worldrenderer.pos(x, y, 0.0D).tex(u * f, v * f1).endVertex();
        tessellator.draw();
    }

    public void quickDrawHead(ResourceLocation skin, int x, int y, int width, int height) {
        mc.getTextureManager().bindTexture2(skin);
        drawScaledCustomSizeModalRect(x, y, 8F, 8F, 8, 8, width, height, 64F, 64F);
        drawScaledCustomSizeModalRect(x, y, 40F, 8F, 8, 8, width, height, 64F, 64F);
    }
    private double calculateBPS() {
        double bps = (Math.hypot(mc2.player.posX - mc2.player.prevPosX, mc2.player.posZ - mc2.player.prevPosZ) * mc.getTimer().getTimerSpeed()) * 20;
        return Math.round(bps * 100.0) / 100.0;
    }

    public int getmoduley(){
        int y=0;
        for (Module module: LiquidBounce.moduleManager.getModules()) {
            if (module.getKeyBind() == 0) continue;
            if (!module.getState())continue;
            y+=12;
        }
        return y;
    }

    public int gety4() {
        int y = 0;
        for (EntityPlayer player : mc2.world.playerEntities){
            if (player == mc2.player)continue;
           // if (mc2.world.playerEntities.size() >5)continue;
            y+=17;
        }
        return y;
    }


}

