/*
 * LiquidBounce Hacked Client
 * A free open source mixin-based injection hacked client for Minecraft using Minecraft Forge.
 * https://github.com/CCBlueX/LiquidBounce/
 */
package net.ccbluex.liquidbounce.features.module.modules.render;

import cn.Client;
import net.ccbluex.liquidbounce.LiquidBounce;
import net.ccbluex.liquidbounce.api.minecraft.network.IPacket;
import net.ccbluex.liquidbounce.event.EventTarget;
import net.ccbluex.liquidbounce.event.PacketEvent;
import net.ccbluex.liquidbounce.features.module.Module;
import net.ccbluex.liquidbounce.features.module.ModuleCategory;
import net.ccbluex.liquidbounce.features.module.ModuleInfo;
import net.ccbluex.liquidbounce.ui.client.clickgui.style.styles.LiquidBounceStyle;
import net.ccbluex.liquidbounce.ui.client.clickgui.style.styles.NullStyle;
import net.ccbluex.liquidbounce.ui.client.clickgui.style.styles.SlowlyStyle;
import net.ccbluex.liquidbounce.utils.render.ColorUtils;
import net.ccbluex.liquidbounce.value.*;
import org.lwjgl.input.Keyboard;

import java.awt.*;

@ModuleInfo(name = "ClickGUI", description = "Opens the ClickGUI.", category = ModuleCategory.RENDER, keyBind = Keyboard.KEY_RSHIFT, canEnable = false)
public class ClickGUI extends Module {
    private final ListValue modeValue = new ListValue("Mode", new String[]{"Style", "OtcV2","Neverlose", "Hyperion", "NewUI"}, "Style");
    private final ListValue styleValue = new ListValue("Style", new String[] {"LiquidBounce", "Null", "Slowly"}, "Slowly") {
        @Override
        protected void onChanged(final String oldValue, final String newValue) {
            updateStyle();
        }
    };
    public final FloatValue scaleValue = new FloatValue("Scale", 1F, 0.7F, 2F);
    public final IntegerValue maxElementsValue = new IntegerValue("MaxElements", 15, 1, 20);

    public static IntegerValue speed = new IntegerValue("NewUI-AstolfoSpeed", 35, 10, 100);
    public static ColorValue colorValue = new ColorValue("Color",new Color(230,38,28).getRGB());

    private static final IntegerValue colorRedValue = new IntegerValue("R", 0, 0, 255);
    private static final IntegerValue colorGreenValue = new IntegerValue("G", 160, 0, 255);
    private static final IntegerValue colorBlueValue = new IntegerValue("B", 255, 0, 255);
    private static final BoolValue colorRainbow = new BoolValue("Rainbow", false);
    public BoolValue disp = new BoolValue("DisplayValue", false);

    public static Color generateColor() {
        return colorRainbow.get() ? ColorUtils.rainbow() : new Color(colorRedValue.get(), colorGreenValue.get(), colorBlueValue.get());
    }

    public void onEnable() {

        switch (modeValue.get()) {
            case "Style":
                updateStyle();
                mc.displayGuiScreen(classProvider.wrapGuiScreen(LiquidBounce.clickGui));
                break;
            /*case "Flux":
                switch (fluxValue.get()) {
                    case "Classic":
                        mc.displayGuiScreen(new FluxClassic());
                        break;
                    case "Otc":
                        mc.displayGuiScreen(new click());
                        break;
                    case "New":
                        mc.displayGuiScreen(Client.instance.flux);
                        break;
                }
                break;

             */
            case "OtcV2":
                mc2.displayGuiScreen(Client.instance.otc);
                break;
            case "Neverlose":
                mc2.displayGuiScreen(Client.instance.skeet);
                break;
            case "Hyperion":
                mc2.displayGuiScreen(Client.instance.nohyp);
                break;

            /*case "EVO":
                mc.displayGuiScreen(Client.instance.eV0);
                break;
            case "Neverlose":
                mc.displayGuiScreen(Client.instance.skeet);
                break;

            case "Fancy":
                mc.displayGuiScreen(Client.instance.fancy);
                break;

            case "Rich":
                mc.displayGuiScreen(Client.instance.rich);
                break;
            case "Alone":
                mc.displayGuiScreen(Client.instance.alone);
                break;
            case "Jello":
                mc.displayGuiScreen(new Jello());
                break;
            case "White":
                mc.displayGuiScreen(new cn.clickgui.white.ClickGui());
                break;

             */
        }


    }



    private void updateStyle() {
        switch(styleValue.get().toLowerCase()) {
            case "liquidbounce":
                LiquidBounce.clickGui.style = new LiquidBounceStyle();
                break;
            case "null":
                LiquidBounce.clickGui.style = new NullStyle();
                break;
            case "slowly":
                LiquidBounce.clickGui.style = new SlowlyStyle();
                break;
        }
    }

    @EventTarget(ignoreCondition = true)
    public void onPacket(final PacketEvent event) {
        final IPacket packet = event.getPacket();

        if (classProvider.isSPacketCloseWindow(packet) && classProvider.isClickGui(mc.getCurrentScreen())) {
            event.cancelEvent();
        }
    }
}
