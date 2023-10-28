package cn.utils.boker;



import net.java.games.input.Mouse;
import net.minecraft.client.Minecraft;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.item.*;
import net.minecraft.network.play.client.CPacketPlayer;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraftforge.client.event.MouseEvent;
import net.minecraftforge.common.MinecraftForge;

import java.awt.*;
import java.awt.datatransfer.StringSelection;
import java.lang.reflect.Field;
import java.nio.ByteBuffer;
import java.util.List;
import java.util.Random;

public class Utils {
	public static Minecraft mc = Minecraft.getMinecraft();
	private static final Random rand = new Random();
	private static Field timerField = null;
	private static Field mouseButton = null;
	private static Field mouseButtonState = null;
	private static Field mouseButtons = null;

	public static List<Entity> getEntityList() {
		return mc.world.getLoadedEntityList();
	}

	public static boolean playerOverAir() {
		double x = mc.player.posX;
		double y = mc.player.posY - 1.0D;
		double z = mc.player.posZ;
		BlockPos p = new BlockPos(MathHelper.floor(x), MathHelper.floor(y), MathHelper.floor(z));
		return mc.world.isAirBlock(p);
	}

	public static int getBlockAmountInCurrentStack(int currentItem) {
		if (mc.player.inventory.getStackInSlot(currentItem) == null) {
			return 0;
		} else {
			ItemStack itemStack = mc.player.inventory.getStackInSlot(currentItem);
			if(itemStack.getItem() instanceof ItemBlock) {
				return itemStack.stackSize;
			} else {
				return 0;
			}
		}
	}



	public static void hotkeyToSlot(int slot) {
		if(!Utils.isPlayerInGame())
			return;

		mc.player.inventory.currentItem = slot;
	}

	public static int getCurrentPlayerSlot() {
		return mc.player.inventory.currentItem;
	}


	public static net.minecraft.util.Timer getTimer() {
		try {
			return (net.minecraft.util.Timer) timerField.get(mc);
		} catch (IndexOutOfBoundsException | IllegalAccessException var1) {
			return null;
		}
	}


	public static float[] gr(Entity q) {
		if (q == null) {
			return null;
		} else {
			double diffX = q.posX - mc.player.posX;
			double diffY;
			if (q instanceof EntityLivingBase) {
				EntityLivingBase en = (EntityLivingBase)q;
				diffY = en.posY + (double)en.getEyeHeight() * 0.9D - (mc.player.posY + (double)mc.player.getEyeHeight());
			} else {
				diffY = (q.getEntityBoundingBox().minY + q.getEntityBoundingBox().maxY) / 2.0D - (mc.player.posY + (double)mc.player.getEyeHeight());
			}

			double diffZ = q.posZ - mc.player.posZ;
			double dist = MathHelper.sqrt(diffX * diffX + diffZ * diffZ);
			float yaw = (float)(Math.atan2(diffZ, diffX) * 180.0D / 3.141592653589793D) - 90.0F;
			float pitch = (float)(-(Math.atan2(diffY, dist) * 180.0D / 3.141592653589793D));
			return new float[]{mc.player.rotationYaw + MathHelper.wrapDegrees(yaw - mc.player.rotationYaw), mc.player.rotationPitch + MathHelper.wrapDegrees(pitch - mc.player.rotationPitch)};
		}
	}
	public static boolean isHyp() {
		if(!Utils.isPlayerInGame()) return false;
		try {
			return !mc.isSingleplayer() && mc.getCurrentServerData().serverIP.toLowerCase().contains("hypixel.net");
		} catch (Exception welpBruh) {
			welpBruh.printStackTrace();
			return false;
		}
	}

	public static Random rand() {
		return rand;
	}

	public static void su() {
		try {
			timerField = Minecraft.class.getDeclaredField("field_71428_T");
		} catch (Exception var4) {
			try {
				timerField = Minecraft.class.getDeclaredField("timer");
			} catch (Exception ignored) {}
		}

		if (timerField != null) {
			timerField.setAccessible(true);
		}

		try {
			mouseButton = MouseEvent.class.getDeclaredField("button");
			mouseButtonState = MouseEvent.class.getDeclaredField("buttonstate");
			mouseButtons = Mouse.class.getDeclaredField("buttons");
		} catch (Exception var2) {
		}

	}

	public enum ClickEvents {
		RENDER,
		TICK
	}

	public static void setMouseButtonState(int mouseButton, boolean held) {
		if (Utils.mouseButton != null && mouseButtonState != null && mouseButtons != null) {
			MouseEvent m = new MouseEvent();

			try {
				Utils.mouseButton.setAccessible(true);
				Utils.mouseButton.set(m, mouseButton);
				mouseButtonState.setAccessible(true);
				mouseButtonState.set(m, held);
				MinecraftForge.EVENT_BUS.post(m);
				mouseButtons.setAccessible(true);
				ByteBuffer bf = (ByteBuffer) mouseButtons.get(null);
				mouseButtons.setAccessible(false);
				bf.put(mouseButton, (byte)(held ? 1 : 0));
			} catch (IllegalAccessException var4) {
			}

		}
	}

	private static final Random RANDOM = new Random();


	public static int random(int min, int max) {
		return RANDOM.nextInt(max - min) + min;
	}
	public static float getDirection() {
		float var1 = mc.player.rotationYaw;
		if (mc.player.moveForward < 0.0F) {
			var1 += 180.0F;
		}
		float forward = 1.0F;
		if (mc.player.moveForward < 0.0F) {
			forward = -0.5F;
		} else if (mc.player.moveForward > 0.0F) {
			forward = 0.5F;
		}
		if (mc.player.moveStrafing > 0.0F) {
			var1 -= 90.0F * forward;
		}
		if (mc.player.moveStrafing < 0.0F) {
			var1 += 90.0F * forward;
		}
		var1 *= 0.017453292F;
		return var1;
	}
	public static boolean nullCheck() {
		return (Minecraft.getMinecraft().player == null || Minecraft.getMinecraft().world == null);
	}

	public static void copy(String content) {
		Toolkit.getDefaultToolkit().getSystemClipboard().setContents(new StringSelection(content), null);
	}

	public static boolean isPlayerInGame() {
		return mc.player != null && mc.world != null;
	}

	public static boolean currentScreenMinecraft() {
		return mc.currentScreen == null;
	}

	public static double fovFromEntity(Entity en) {
		return ((double)(mc.player.rotationYaw - fovToEntity(en)) % 360.0D + 540.0D) % 360.0D - 180.0D;
	}

	public static float fovToEntity(Entity ent) {
		double x = ent.posX - mc.player.posX;
		double z = ent.posZ - mc.player.posZ;
		double yaw = Math.atan2(x, z) * 57.2957795D;
		return (float)(yaw * -1.0D);
	}

	public static boolean fov(Entity entity, float fov) {
		fov = (float)((double)fov * 0.5D);
		double v = ((double)(mc.player.rotationYaw - fovToEntity(entity)) % 360.0D + 540.0D) % 360.0D - 180.0D;
		return v > 0.0D && v < (double)fov || (double)(-fov) < v && v < 0.0D;
	}
}
