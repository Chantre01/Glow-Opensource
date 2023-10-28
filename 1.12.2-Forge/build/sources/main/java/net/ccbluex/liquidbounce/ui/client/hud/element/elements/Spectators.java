package net.ccbluex.liquidbounce.ui.client.hud.element.elements;

import cn.novoline.impl.Fonts;
import cn.utils.miku.render.round.RoundedUtil;
import net.ccbluex.liquidbounce.api.enums.WDefaultVertexFormats;
import net.ccbluex.liquidbounce.api.minecraft.client.render.ITessellator;
import net.ccbluex.liquidbounce.api.minecraft.client.render.IWorldRenderer;
import net.ccbluex.liquidbounce.features.module.modules.render.UIEffects;
import net.ccbluex.liquidbounce.feng.FontLoaders;
import net.ccbluex.liquidbounce.ui.client.hud.element.Border;
import net.ccbluex.liquidbounce.ui.client.hud.element.Element;
import net.ccbluex.liquidbounce.ui.client.hud.element.ElementInfo;
import net.ccbluex.liquidbounce.utils.render.RenderUtils;
import net.minecraft.client.entity.AbstractClientPlayer;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.ResourceLocation;
import org.lwjgl.opengl.GL11;

import java.awt.*;

import static net.ccbluex.liquidbounce.features.module.modules.render.UIEffects.Hudcolor;

@ElementInfo(name = "PlayerList")
public class Spectators extends Element {
    int playy = 0;
    @Override
    public Border drawElement() {

        switch (UIEffects.hudstyle.get().toLowerCase()) {
            case "customcolor":
            case "customshader":
            case "customoutlined": {
                //if (UIEffects.hudblur.get()) {
                //    UIEffects.renderblur(getRenderX(), getRenderY(), 80f, 17 + gety4(), 6f);
                //}
                UIEffects.render(0, 0, 80, 17 + gety4(), 10);
                colorText();
                break;
            }
            case "onetapv2":{
                RoundedUtil.drawRound(0, 0, 80, 17 + gety4(), 1, new Color(22, 18, 27));
                RoundedUtil.drawRound(0, 0, 80, 15, 0, new Color(28, 30, 40));
                RoundedUtil.drawRound(0, 0 + 16f, 80, 0.7f, 0, new Color(42, 39, 44));
                RoundedUtil.drawRound( -0.5f, -1.7f, 81f, 1.2f, 0.5f, new Color(Hudcolor.getValue()));
                colorText();
                break;
            }
        }


        return new Border(0F, 0F, 120F, 30F);
    }
    private void colorText(){
        int playy = 0;
        Fonts.CsgoIcon.csgoicon_18.csgoicon_18.drawString("F", 3, 6.5f, -1);
        Fonts.SF.SF_22.SF_22.drawString("|", 17, 4f, new Color(80, 80, 80).getRGB());
        Fonts.SF.SF_16.SF_16.drawString("Spectators", 23, 5.5f, -1);

        //循环世界所有玩家
        for (EntityPlayer player:mc2.world.playerEntities){
            if (player == mc2.player)continue;

            quickDrawHead(((AbstractClientPlayer)  player).getLocationSkin(), 3 , 20+ playy,12,12);
            FontLoaders.C14.drawString(player.getName(), 20, 24f + playy, -1);
            playy +=17;

        }
    }
    public void quickDrawHead(ResourceLocation skin, int x, int y, int width, int height) {
        mc.getTextureManager().bindTexture2(skin);
        drawScaledCustomSizeModalRect(x, y, 8F, 8F, 8, 8, width, height, 64F, 64F);
        drawScaledCustomSizeModalRect(x, y, 40F, 8F, 8, 8, width, height, 64F, 64F);
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
