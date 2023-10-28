package net.ccbluex.liquidbounce.injection.forge.mixins.gui;

import cn.utils.EaseUtils;

import net.ccbluex.liquidbounce.LiquidBounce;
import net.ccbluex.liquidbounce.features.module.modules.combat.KillAura;
import net.ccbluex.liquidbounce.features.module.modules.player.InvManager;
import net.ccbluex.liquidbounce.features.module.modules.render.Animations;

import net.ccbluex.liquidbounce.features.module.modules.world.Stealer;
import net.ccbluex.liquidbounce.injection.implementations.IMixinGuiContainer;
import net.ccbluex.liquidbounce.utils.render.RenderUtils;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.gui.inventory.GuiChest;
import net.minecraft.client.gui.inventory.GuiContainer;
import net.minecraft.inventory.ClickType;
import net.minecraft.inventory.Slot;
import org.lwjgl.opengl.GL11;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.List;

@Mixin(GuiContainer.class)
public abstract class MixinGuiContainer extends MixinGuiScreen implements IMixinGuiContainer {

    private GuiButton invManagerButton;
    private GuiButton killAuraButton;

    private float progress = 0.0f;
    private long lastMS = 0;

    @Shadow
    protected abstract void handleMouseClick(Slot p_handleMouseClick_1_, int p_handleMouseClick_2_, int p_handleMouseClick_3_, ClickType p_handleMouseClick_4_);

    @Shadow protected int xSize;

    @Shadow protected int ySize;

    @Inject(method = {"initGui"}, at = {@At("HEAD")}, cancellable = true)
    public void injectInitGui(CallbackInfo callbackInfo) {
        GuiScreen guiScreen = Minecraft.getMinecraft().currentScreen;
        List list = this.buttonList;
        GuiButton guiButton = new GuiButton(1024576, 5, 5, 140, 20, "Disable KillAura");
        this.killAuraButton = guiButton;
        list.add(guiButton);
        int firstY = 0 + 20;
        List list2 = this.buttonList;
        GuiButton guiButton2 = new GuiButton(321123, 5, 10 + firstY, 140, 20, "Disable InvManager");
        this.invManagerButton = guiButton2;
        list2.add(guiButton2);
        this.lastMS = System.currentTimeMillis();
        this.progress = 0.0f;
    }
    @Inject(method = {"drawScreen"}, at = {@At("HEAD")}, cancellable = true)
    protected void drawScreenHead(CallbackInfo callbackInfo) {
        if (this.progress >= 1.0f) {
            this.progress = 1.0f;
        } else {
            this.progress = ((float) (System.currentTimeMillis() - this.lastMS)) / 300.0f;
        }

        double trueAnim = EaseUtils.easeOutQuart(this.progress);
        Stealer stealer = (Stealer) LiquidBounce.moduleManager.getModule(Stealer.class);
        Minecraft mc = Minecraft.getMinecraft();
        GuiScreen guiScreen = mc.currentScreen;
        if (stealer.getState() && stealer.getSlienceValue().get() && guiScreen instanceof GuiChest) {
            GuiChest chest = (GuiChest) guiScreen;
            if (!(stealer.getSlienceValue().get() && (chest.lowerChestInventory == null ))) {
                // mouse focus
                mc.setIngameFocus();
                mc.currentScreen = guiScreen;
                // hide GUI
                if (stealer.getSlienceValue().get()) {
                    String tipString = "Stealing the Chest...";

                    mc.fontRenderer.drawString(tipString,
                            (width / 2F) - (mc.fontRenderer.getStringWidth(tipString) / 2F) - 0.5F,
                            (height / 2F) + 30, 0, false);
                    mc.fontRenderer.drawString(tipString,
                            (width / 2F) - (mc.fontRenderer.getStringWidth(tipString) / 2F) + 0.5F,
                            (height / 2F) + 30, 0, false);
                    mc.fontRenderer.drawString(tipString,
                            (width / 2F) - (mc.fontRenderer.getStringWidth(tipString) / 2F),
                            (height / 2F) + 29.5F, 0, false);
                    mc.fontRenderer.drawString(tipString,
                            (width / 2F) - (mc.fontRenderer.getStringWidth(tipString) / 2F),
                            (height / 2F) + 30.5F, 0, false);
                    mc.fontRenderer.drawString(tipString,
                            (width / 2F) - (mc.fontRenderer.getStringWidth(tipString) / 2F),
                            (height / 2F) + 30, 0xffffffff, false);
                }
                callbackInfo.cancel();
            }
        } else {
//        GL11.glTranslated((1.0d - trueAnim) * (this.width / 2.0d), (1.0d - trueAnim) * (this.height / 2.0d), 0.0d);
//        GL11.glScaled(trueAnim, trueAnim, trueAnim);
            switch (Animations.guiAnimations.get()) {
                case "Zoom":
                    GL11.glTranslated((1 - trueAnim) * (width / 2D), (1 - trueAnim) * (height / 2D), 0D);
                    GL11.glScaled(trueAnim, trueAnim, trueAnim);
                    break;
                case "HSlide":
                    GL11.glTranslated((1 - trueAnim) * -width, 0D, 0D);
                    break;
                case "VSlide":
                    GL11.glTranslated(0D, (1 - trueAnim) * -height, 0D);
                    break;
                case "HVSlide":
                    GL11.glTranslated((1 - trueAnim) * -width, (1 - trueAnim) * -height, 0D);
                    break;
            }
            RenderUtils.drawGradientSideways(0, 0, this.xSize, this.ySize, 0, 0);
            GL11.glPushMatrix();
        }
    }

    @Inject(method = {"drawScreen"}, at = {@At("RETURN")}, cancellable = true)
    protected void drawScreenReturn(CallbackInfo callbackInfo) {
        GL11.glPopMatrix();
    }

    @Inject(method = "mouseClicked", at = @At("RETURN"))
    private void mouseClicked(int mouseX, int mouseY, int mouseButton,CallbackInfo callbackInfo) {
        for (Object aButtonList : this.buttonList) {
            GuiButton var52 = (GuiButton) aButtonList;
            if (var52.mousePressed(this.mc, mouseX, mouseY) && var52.id == 1024576) {
                LiquidBounce.moduleManager.getModule(KillAura.class).setState(false);
            }
            if (var52.mousePressed(this.mc, mouseX, mouseY) && var52.id == 321123) {
                LiquidBounce.moduleManager.getModule(InvManager.class).setState(false);
            }
        }
    }

    @Override
    public void publicHandleMouseClick(Slot slot, int slotNumber, int clickedButton, ClickType clickType) {

        this.handleMouseClick(slot, slotNumber, clickedButton, clickType);
    }

}
