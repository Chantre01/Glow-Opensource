package cn.font;
import cn.utils.miku.render.RenderUtil;
import cn.Client;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.util.StringUtils;
import org.lwjgl.opengl.GL11;
import org.newdawn.slick.UnicodeFont;
import org.newdawn.slick.font.effects.ColorEffect;

import java.awt.*;
import java.io.IOException;
import java.util.*;
import java.util.List;

/**
 * Made by MemesValkyrie for Faint.
 * <p>
 * Just a little fontrenderer for minecraft I wrote. Should work with any font
 * size without any graphical glitches, but because of this setup takes forever.
 * Feel free to make any edits.
 * <p>
 * Created by Zeb on 12/19/2016.
 */
public class TTFFontRenderer {
	public final int FONT_HEIGHT = 9;
	private final int[] colorCodes = new int[32];
	private final float kerning;
	private final Map<String, Float> cachedStringWidth = new HashMap<String, Float>();
	private float antiAliasingFactor;
	private UnicodeFont unicodeFont;

	public static TTFFontRenderer getFontOnPC(String name, int size) {
		return TTFFontRenderer.getFontOnPC(name, size, 0);
	}

	public static TTFFontRenderer getFontOnPC(String name, int size, int fontType) {
		return TTFFontRenderer.getFontOnPC(name, size, fontType, 0.0f);
	}

	public static TTFFontRenderer getFontOnPC(String name, int size, int fontType, float kerning) {
		return TTFFontRenderer.getFontOnPC(name, size, fontType, kerning, 3.0f);
	}

	public static TTFFontRenderer getFontOnPC(String name, int size, int fontType, float kerning,
			float antiAliasingFactor) {
		return new TTFFontRenderer(new Font(name, fontType, size), kerning, antiAliasingFactor);
	}

	public static TTFFontRenderer getFontFromAssets(String name, int size) {
		return TTFFontRenderer.getFontOnPC(name, size, 0);
	}

	public static TTFFontRenderer getFontFromAssets(String name, int size, int fontType) {
		return TTFFontRenderer.getFontOnPC(name, fontType, size, 0.0f);
	}

	public static TTFFontRenderer getFontFromAssets(String name, int size, float kerning, int fontType) {
		return TTFFontRenderer.getFontFromAssets(name, size, fontType, kerning, 3.0f);
	}

	public static TTFFontRenderer getFontFromAssets(String name, int size, int fontType, float kerning,
			float antiAliasingFactor) {
		return new TTFFontRenderer(name, fontType, size, kerning, antiAliasingFactor);
	}

	public TTFFontRenderer(String fontName, int fontType, float fontSize, float kerning, float antiAliasingFactor) {
		this.antiAliasingFactor = antiAliasingFactor;
		try {
			this.unicodeFont = new UnicodeFont(
					this.getFontByName(fontName).deriveFont(fontSize * this.antiAliasingFactor));
		} catch (FontFormatException | IOException e2) {
			e2.printStackTrace();
		}
		this.kerning = kerning;
		this.unicodeFont.addAsciiGlyphs();
		this.unicodeFont.getEffects().add(new ColorEffect(Color.WHITE));
		try {
			this.unicodeFont.loadGlyphs();
		} catch (Exception e3) {
			e3.printStackTrace();
		}
		for (int i2 = 0; i2 < 32; ++i2) {
			int shadow = (i2 >> 3 & 1) * 85;
			int red = (i2 >> 2 & 1) * 170 + shadow;
			int green = (i2 >> 1 & 1) * 170 + shadow;
			int blue = (i2 & 1) * 170 + shadow;
			if (i2 == 6) {
				red += 85;
			}
			if (i2 >= 16) {
				red /= 4;
				green /= 4;
				blue /= 4;
			}
			this.colorCodes[i2] = (red & 0xFF) << 16 | (green & 0xFF) << 8 | blue & 0xFF;
		}
	}

	public TTFFontRenderer(Font font, float kerning, float antiAliasingFactor) {
		this.antiAliasingFactor = antiAliasingFactor;
		this.unicodeFont = new UnicodeFont(
				new Font(font.getName(), font.getStyle(), (int) ((float) font.getSize() * antiAliasingFactor)));
		this.kerning = kerning;
		this.unicodeFont.addAsciiGlyphs();
		this.unicodeFont.getEffects().add(new ColorEffect(Color.WHITE));
		try {
			this.unicodeFont.loadGlyphs();
		} catch (Exception e2) {
			e2.printStackTrace();
		}
		for (int i2 = 0; i2 < 32; ++i2) {
			int shadow = (i2 >> 3 & 1) * 85;
			int red = (i2 >> 2 & 1) * 170 + shadow;
			int green = (i2 >> 1 & 1) * 170 + shadow;
			int blue = (i2 & 1) * 170 + shadow;
			if (i2 == 6) {
				red += 85;
			}
			if (i2 >= 16) {
				red /= 4;
				green /= 4;
				blue /= 4;
			}
			this.colorCodes[i2] = (red & 0xFF) << 16 | (green & 0xFF) << 8 | blue & 0xFF;
		}
	}

	private Font getFontByName(String name) throws IOException, FontFormatException {
		return this.getFontFromInput("/assets/minecraft/liquidbounce/fonts/" + name + ".ttf");
	}

	private Font getFontFromInput(String path) throws IOException, FontFormatException {
		return Font.createFont(0, Objects.requireNonNull(Client.class.getResourceAsStream(path)));
	}

	public void drawStringScaled(String text, double givenX, double givenY, int color, double givenScale) {
		GL11.glPushMatrix();
		GL11.glTranslated(givenX, givenY, 0.0);
		GL11.glScaled(givenScale, givenScale, givenScale);
		this.drawString(text, 0.0f, 0.0f, color);
		GL11.glPopMatrix();
	}

	public int drawString(String text, float x, float y, int color, boolean dropShadow) {
		int i;
		if (dropShadow) {
			i = this.drawString(text, x + 1.0F, y + 1.0F, color);
			i = Math.max(i, this.drawString(text, x, y, color));
		} else {
			i = this.drawString(text, x, y, color, false);
		}
		return i;
	}

	public int drawString(String text, float x2, float y2, int color) {
		if (text == null) {
			return 0;
		}
		y2 *= 2.0f;
		float originalX = x2 *= 2.0f;
		GL11.glPushMatrix();
		GlStateManager.scale(1.0f / this.antiAliasingFactor, 1.0f / this.antiAliasingFactor,
				1.0f / this.antiAliasingFactor);
		GL11.glScaled(0.5, 0.5, 0.5);
		x2 *= this.antiAliasingFactor;
		y2 *= this.antiAliasingFactor;
		float red = (float) (color >> 16 & 0xFF) / 255.0f;
		float green = (float) (color >> 8 & 0xFF) / 255.0f;
		float blue = (float) (color & 0xFF) / 255.0f;
		float alpha = (float) (color >> 24 & 0xFF) / 255.0f;
		GlStateManager.color(red, green, blue, alpha);
		boolean blend = GL11.glIsEnabled(GL11.GL_BLEND);
		boolean lighting = GL11.glIsEnabled(GL11.GL_LIGHTING);
		boolean texture = GL11.glIsEnabled(GL11.GL_TEXTURE_2D);
		if (!blend) {
			GL11.glEnable(GL11.GL_BLEND);
		}
		if (lighting) {
			GL11.glDisable(GL11.GL_LIGHTING);
		}
		if (texture) {
			GL11.glDisable(GL11.GL_TEXTURE_2D);
		}
		int currentColor = color;
		char[] characters = text.toCharArray();
		int index = 0;
		for (char c2 : characters) {
			if (c2 == '\r') {
				x2 = originalX;
			}
			if (c2 == '\n') {
				y2 += this.getHeight(Character.toString(c2)) * 2.0f;
			}
			if (c2 != '\u00a7' && (index == 0 || index == characters.length - 1 || characters[index - 1] != '\u00a7')) {
				this.unicodeFont.drawString(x2, y2, Character.toString(c2), new org.newdawn.slick.Color(currentColor));
				x2 += this.getWidth(Character.toString(c2)) * 2.0f * this.antiAliasingFactor;
			} else if (c2 == ' ') {
				x2 += (float) this.unicodeFont.getSpaceWidth();
			} else if (c2 == '\u00a7' && index != characters.length - 1) {
				int codeIndex = "0123456789abcdefg".indexOf(text.charAt(index + 1));
				if (codeIndex < 0)
					continue;
				currentColor = this.colorCodes[codeIndex];

			}
			++index;
		}
		GL11.glScaled(2.0, 2.0, 2.0);
		if (!blend) {
			GL11.glDisable(GL11.GL_BLEND);
		}
		if (lighting) {
			GL11.glEnable(GL11.GL_LIGHTING);
		}
		if (texture) {
			GL11.glEnable(GL11.GL_TEXTURE_2D);
		}
		GlStateManager.color(1.0f, 1.0f, 1.0f, 1.0f);
		GL11.glPopMatrix();
		return (int) x2 / 2;
	}

	public int drawStringWithShadow(String text, float x2, float y2, int color) {
		this.drawString(StringUtils.stripControlCodes(text), x2 + 0.5f, y2 - 1 + 0.5f, 0);
		return this.drawString(text, x2, y2 - 1, color);
	}

	public void drawCenteredString(String text, float x2, float y2, int color) {
		this.drawString(text, x2 - (float) ((int) this.getWidth(text) / 2), y2 - 1, color);
	}
	public void drawCenteredStringScaled(String text, float x2, float y2, int color, float f) {
		GL11.glPushMatrix();
		GL11.glTranslated(x2, y2, 0.0);
		GL11.glScaled(f, f, f);
		this.drawString(text, 0 - (float) ((int) this.getWidth(text) / 2), 0 - 1, color);
		GL11.glPopMatrix();
	}

	public void drawBlurredStringWithShadow(String text, double x, double y, int blurRadius, Color blurColor, int color) {
		GlStateManager.resetColor();
		RenderUtil.drawBlurredShadow((int) x, (int) y, (int) getStringWidth(text), (int) getHeight(), blurRadius, blurColor);
		drawStringWithShadow(text, (float) x, (float) y, color);
	}
	public void drawBlurredString(String text, double x, double y, int blurRadius, Color blurColor, int color) {
		GlStateManager.resetColor();
		RenderUtil.drawBlurredShadow((int) x, (int) y, (int) getStringWidth(text), (int) getHeight(), blurRadius, blurColor);
		drawString(text, (float) x, (float) y, color);
	}
	public void drawCenteredBlurredString(String text, double x, double y, int blurRadius, Color blurColor, int color) {
		GlStateManager.resetColor();
		RenderUtil.drawBlurredShadow((int) ((int) x - (float)this.getStringWidth(text) / 2.0f), (int) y, (int) getStringWidth(text), (int) getHeight(), blurRadius, blurColor);
		drawString(text, (float) (x - this.getStringWidth(text) / 2F), (float) y, color);
	}


	public void drawCenteredTextScaled(String text, float givenX, float givenY, int color, double givenScale) {
		GL11.glPushMatrix();
		GL11.glTranslated(givenX, givenY, 0.0);
		GL11.glScaled(givenScale, givenScale, givenScale);
		this.drawCenteredString(text, 0.5f, 0.5f, new Color(0, 0, 0, 169).getRGB());

		this.drawCenteredString(text, 0.0f, 0.0f, color);
		GL11.glPopMatrix();

	}

	public void drawCenteredStringWithShadow(String text, float x2, float y2, int color) {
		this.drawCenteredString(StringUtils.stripControlCodes(text), x2 + 0.5f, y2 + 0.5f,
				new Color(0, 0, 0, 169).getRGB());
		this.drawCenteredString(text, x2, y2, color);
	}

	public float getWidth(String s2) {
		if (this.cachedStringWidth.size() > 1000) {
			this.cachedStringWidth.clear();
		}
		return this.cachedStringWidth.computeIfAbsent(s2, e2 -> {
			float width = 0.0f;
			String str = StringUtils.stripControlCodes(s2);
			for (char c2 : str.toCharArray()) {
				width += (float) this.unicodeFont.getWidth(Character.toString(c2)) + this.kerning;
			}
			return Float.valueOf(width / 2.0f / this.antiAliasingFactor);
		}).floatValue();
	}

	public int getStringWidth(String text) {
		/*if (text == null) {
			return 0;
		} else {
			
			float f = 0.0F;
			boolean flag = false;
			
			for (int i = 0; i < text.length(); ++i) {
				
				char c0 = text.charAt(i);
				float f1 = this.getCharWidth(c0);
				
				if (f1 < 0.0F && i < text.length() - 1) {
					c0 = text.charAt(++i);
					
					if (c0 != 108 && c0 != 76) {
						if (c0 == 114 || c0 == 82) {
							flag = false;
						}
					} else {
						flag = true;
					}
					f1 = 0.0F;
				}
				f += f1;
				if (!flag && !(f1 > 0.0F)) continue;
					++f;
				
			}
			return (int)f;
		}*/
		
		if (text == null) {
			return 0;
		} else {
			int i2 = 0;
			boolean flag = false;
			for (int j2 = 0; j2 < text.length(); ++j2) {
				char c0 = text.charAt(j2);
				float k2 = this.getWidth(String.valueOf(c0));
				if (k2 < 0.0f && j2 < text.length() - 1) {
					if ((c0 = text.charAt(++j2)) != 108 && c0 != 76/*(c0 = text.charAt(++j2)) != 'l' && c0 != 'L'*/) {
						if (c0 == 114 || c0 == 82/*c0 == 'r' || c0 == 'R'*/) {
							 
							flag = false;
						}
					} else {
						flag = true;
					}
					k2 = 0.0f;
				}
				i2 = (int) ((float) i2 + k2);
				if (!flag || !(k2 > 0.0f))
					continue;
				++i2;
			}
			
			return i2;
		}
	}

	public float getCharWidth(char c2) {
		return this.unicodeFont.getWidth(String.valueOf(c2));
	}

	public float getHeight(String s2) {
		return (float) this.unicodeFont.getHeight(s2) / 2.0f;
	}

	public float getHeight() {
		return getHeight("I");
	}

	public UnicodeFont getFont() {
		return this.unicodeFont;
	}

	public String trimStringToWidth(String par1Str, int par2) {
		StringBuilder var4 = new StringBuilder();
		float var5 = 0.0f;
		int var6 = 0;
		int var7 = 1;
		boolean var8 = false;
		boolean var9 = false;
		for (int var10 = var6; var10 >= 0 && var10 < par1Str.length() && var5 < (float) par2; var10 += var7) {
			char var11 = par1Str.charAt(var10);
			float var12 = this.getCharWidth(var11);
			if (var8) {
				var8 = false;
				if (var11 != 'l' && var11 != 'L') {
					if (var11 == 'r' || var11 == 'R') {
						var9 = false;
					}
				} else {
					var9 = true;
				}
			} else if (var12 < 0.0f) {
				var8 = true;
			} else {
				var5 += var12;
				if (var9) {
					var5 += 1.0f;
				}
			}
			if (var5 > (float) par2)
				break;
			var4.append(var11);
		}
		return var4.toString();
	}

	public void drawSplitString(ArrayList<String> lines, int x2, int y2, int color) {
		this.drawString(String.join((CharSequence) "\n\r", lines), x2, y2, color);
	}

	public List<String> splitString(String text, int wrapWidth) {
		ArrayList<String> lines = new ArrayList<String>();
		String[] splitText = text.split(" ");
		StringBuilder currentString = new StringBuilder();
		for (String word : splitText) {
			String potential = currentString + " " + word;
			if (this.getWidth(potential) >= (float) wrapWidth) {
				lines.add(currentString.toString());
				currentString = new StringBuilder();
			}
			currentString.append(word).append(" ");
		}
		lines.add(currentString.toString());
		return lines;

	}
}