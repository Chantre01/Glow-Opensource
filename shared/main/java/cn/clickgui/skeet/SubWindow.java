package cn.clickgui.skeet;

import cn.Client;
import cn.font.FontLoaders;
import net.ccbluex.liquidbounce.utils.render.RenderUtils;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Gui;
import net.minecraft.client.gui.ScaledResolution;
import net.minecraft.client.renderer.GlStateManager;
import org.lwjgl.input.Mouse;
import org.lwjgl.opengl.GL11;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.ConcurrentModificationException;

public class SubWindow extends Component {
    public static final int MAX_HEIGHT = 350;
    public final ArrayList<Component> components = new ArrayList<>();
    private final String title;
    private final String ch;
    protected boolean hidden;
    protected double x, y;
    private boolean dragging;
    private int scrolled = 0;

    public SubWindow(String title, String ch) {
        this.title = title;
        this.ch = ch;
    }

    public SubWindow(String title, String ch, Component... components) {
        this(title, ch);
        this.components.addAll(Arrays.asList(components));
    }

    public boolean isHidden() {
        return hidden;
    }

    public void toggleHidden() {
        hidden = !hidden;
    }

    public int getX() {
        return (int) (x * RenderUtils.width());
    }

    public void setX(double x) {
        this.x = x / RenderUtils.width();
    }

    public int getY() {
        return (int) (y * RenderUtils.height());
    }

    public void setY(double y) {
        this.y = y / RenderUtils.height();
    }

    public double getX_double() {
        return x * RenderUtils.width();
    }

    public double getY_double() {
        return y * RenderUtils.height();
    }

    public void addComponent(Component component) {
        if (component instanceof SubWindow)
            throw new IllegalArgumentException();
        components.add(component);
    }

    @Override
    public void drawComponent(int mouseX, int mouseY, float partialTicks) {
        ScaledResolution sr = new ScaledResolution(mc);
        if (dragging) {
            double deltaX = Mouse.getDX() / (double) sr.getScaleFactor(),
                    deltaY = -Mouse.getDY() / (double) sr.getScaleFactor();
            this.x += deltaX / sr.getScaledWidth_double();
            this.y += deltaY / sr.getScaledHeight_double();
        }
        int width = getWidth(), height = getHeight();
        this.x = Math.min(Math.max(this.x, 0), 1D - (getWidth() / sr.getScaledWidth_double()));
        this.y = Math.min(Math.max(this.y, 0), 1D - (Math.min(height, MAX_HEIGHT) / sr.getScaledHeight_double()));
        int posX = getX(),
                posY = getY();
        int realHeight = height;
        if (height > MAX_HEIGHT) {
            width += 4;
            height = MAX_HEIGHT;
        }
        Gui.drawRect(posX - 2, posY, posX + width + 2, posY + 20, Client.THEME_RGB_COLOR);
        try {
            FontLoaders.jelloFontMedium16.drawString(title, posX + (width -  FontLoaders.jelloFontMedium16.getStringWidth(title)) / 2, posY + 8, -1);
        } catch (NullPointerException exception) {
            exception.printStackTrace();
        }
        FontLoaders.jelloFontMedium16.drawString(hidden ? "+" : "-", posX + width - 10, posY + 8, -1);
        if (hidden)
            return;
        Gui.drawRect(posX, posY + 20, posX + width, posY + height, 0xAA000000);
        GlStateManager.pushMatrix();
        if (height != realHeight) {
            double f1 = (height) / (double) (realHeight);
            double scrollHeight = (height - 20) * f1;
            int scrollY = (int) (((scrolled / (double) (realHeight - MAX_HEIGHT)) - 0) / (1D - 0D) * (MAX_HEIGHT - scrollHeight - 20 - 1D) + 1D) + posY + 20;
            boolean flag = mouseX > width - 4 && mouseX < width &&
                    mouseY > 20 + scrolled && mouseY < 20 + scrollHeight + scrolled;
            Gui.drawRect(posX + width - 4, scrollY, posX + width, (int) (scrollY + scrollHeight), flag ? 0xFF000000 : 0x55000000);
            GlStateManager.translate(0, -scrolled, 0);
            if (flag && Mouse.isButtonDown(0)) {
                int off = (int) (-Mouse.getDY() * (scrollHeight / MAX_HEIGHT));
                scrolled = Math.max(Math.min(scrolled + off, realHeight - MAX_HEIGHT), 0);
            }
        }
        GL11.glEnable(GL11.GL_SCISSOR_TEST);
        GL11.glScissor(posX * sr.getScaleFactor(), mc.displayHeight - (posY + height) * sr.getScaleFactor(), width * sr.getScaleFactor(), (height - 20) * sr.getScaleFactor());
        GlStateManager.translate(posX, posY + 20, 0);
        int y = 20 - scrolled;
        for (Component component : components) {
            component.drawComponent(mouseX, mouseY - y, partialTicks);
            GlStateManager.translate(0, component.getHeight(), 0);
            y += component.getHeight();
        }
        GlStateManager.popMatrix();
        GL11.glDisable(GL11.GL_SCISSOR_TEST);
    }

    public int getWidth() {
        int width =  FontLoaders.jelloFontMedium16.getStringWidth(title) + 32;
        for (Component component : components)
            width = Math.max(width, component.getWidth());
        return width + 12;
    }

    public int getHeight() {
        int height = 20;
        if (hidden)
            return height;
        for (Component component : components) {
            height += component.getHeight();
        }
        return height;
    }

    public void mouseClicked(int mouseX, int mouseY, int mouseButton) {
        this.dragging = mouseX > getX() && mouseX < getX() + getWidth() &&
                mouseY > getY() && mouseY < getY() + 20;
        if (!hidden && mouseX > getX() && mouseX < getX() + getWidth() &&
                mouseY > getY() + 20 && mouseY < getHeight() + getY()) {
            int y = getY() + 20 - scrolled;
            try {
                for (Component component : components) {
                    if (mouseY > y && mouseY < (y + component.getHeight()) && mouseY < getY() + MAX_HEIGHT)
                        component.mouseClicked(mouseX - getX(), mouseY - y, mouseButton);
                    y += component.getHeight();
                }
            } catch (ConcurrentModificationException e) {
                e.printStackTrace();
            }
        }
        if (dragging) {
            if (mouseButton == 1) {
                this.hidden = !this.hidden;
            }
            if (mouseButton != 0) {
                dragging = false;
            }
        }
    }

    public void mouseReleased(int mouseX, int mouseY, int state) {
        this.dragging = false;
        for (Component component : components) {
            component.mouseReleased(mouseX, mouseY, state);
        }
    }

    public void keyTyped(char typedChar, int keyCode) {
        try {
            for (Component component : components) {
                component.keyTyped(typedChar, keyCode);
            }
        } catch (ConcurrentModificationException e) {
            e.printStackTrace();
        }
    }

    public void handleMouseWheel(int off) {
        int height = getHeight();
        if (height > MAX_HEIGHT) {
            if (off != 0)
                off /= Math.abs(off);
            off *= -8;
            scrolled = Math.max(Math.min(scrolled + off, height - MAX_HEIGHT), 0);
        }
    }
}
