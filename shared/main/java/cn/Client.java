package cn;


import cn.clickgui.nohyper.hyperGui;
import cn.novoline.api.FontManager;
import cn.novoline.impl.SimpleFontManager;
import cn.clickgui.otcV2.OtcClickGUi;
import cn.clickgui.skeet.HyperGui;
import cn.utils.ScaleUtils;
import net.minecraft.client.Minecraft;

import java.awt.*;

public class Client {
    public static double fontScaleOffset = 1;//round((double)1600/1080, 1) * s.getScaleFactor();//2.75;
    public static FontManager fontManager = SimpleFontManager.create();
    public static FontManager getFontManager() {
        return fontManager;
    }

    public static String name = "GLOW";
    public static String version = "230622";
    public static int THEME_RGB_COLOR = new Color(36, 240, 0).getRGB();

    public static Client instance = new Client();
    public static ScaleUtils scaleUtils = new ScaleUtils(2);

    public static double deltaTime() {
        return Minecraft.getDebugFPS() > 0 ? (1.0000 / Minecraft.getDebugFPS()) : 1;
    }
    public OtcClickGUi otc;
    public HyperGui skeet;
    public hyperGui nohyp;
//  fxxking evo clickgui

}
