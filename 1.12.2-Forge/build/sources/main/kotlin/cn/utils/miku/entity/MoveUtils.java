/*
 * LiquidBounce+ Hacked Client
 * A free open source mixin-based injection hacked client for Minecraft using Minecraft Forge.
 * https://github.com/WYSI-Foundation/LiquidBouncePlus/
 */
package cn.utils.miku.entity;

import net.ccbluex.liquidbounce.utils.MinecraftInstance;
import net.ccbluex.liquidbounce.event.MoveEvent;

import org.lwjgl.util.vector.Vector2f;

public class MoveUtils extends MinecraftInstance {

    private static double lastX = -999999.0;

    private static double lastY = -999999.0;

    private static double lastZ = -999999.0;

    public static float getSpeed() {
        return (float) getSpeed(mc2.player.motionX, mc2.player.motionZ);
    }

    public static double getSpeed(double motionX, double motionZ) {
        return Math.sqrt(motionX * motionX + motionZ * motionZ);
    }

    public static boolean isOnGround() {
        return mc2.player.onGround && mc2.player.collidedVertically;
    }





    public static void accelerate() {
        accelerate(getSpeed());
    }

    public static void accelerate(final float speed) {
        if (!isMoving())
            return;

        final double yaw = getDirection();
        mc2.player.motionX += -Math.sin(yaw) * speed;
        mc2.player.motionZ += Math.cos(yaw) * speed;
    }

    public static void strafe() {
        strafe(getSpeed());
    }

    public static boolean isMoving() {
        return mc2.player != null && (mc2.player.movementInput.moveForward != 0F || mc2.player.movementInput.moveStrafe != 0F);
    }

    public static boolean hasMotion() {
        return mc2.player.motionX != 0D && mc2.player.motionZ != 0D && mc2.player.motionY != 0D;
    }

    public static void strafe(final float speed) {
        if (!isMoving())
            return;

        final double yaw = getDirection();
        mc2.player.motionX = -Math.sin(yaw) * speed;
        mc2.player.motionZ = Math.cos(yaw) * speed;
    }

    public static void strafeCustom(final float speed, final float cYaw, final float strafe, final float forward) {
        if (!isMoving())
            return;

        final double yaw = getDirectionRotation(cYaw, strafe, forward);
        mc2.player.motionX = -Math.sin(yaw) * speed;
        mc2.player.motionZ = Math.cos(yaw) * speed;
    }

    public static void forward(final double length) {
        final double yaw = Math.toRadians(mc2.player.rotationYaw);
        mc2.player.setPosition(mc2.player.posX + (-Math.sin(yaw) * length), mc2.player.posY, mc2.player.posZ + (Math.cos(yaw) * length));
    }

    public static double getDirection() {
        float rotationYaw = mc2.player.rotationYaw;

        if (mc2.player.moveForward < 0F)
            rotationYaw += 180F;

        float forward = 1F;
        if (mc2.player.moveForward < 0F)
            forward = -0.5F;
        else if (mc2.player.moveForward > 0F)
            forward = 0.5F;

        if (mc2.player.moveStrafing > 0F)
            rotationYaw -= 90F * forward;

        if (mc2.player.moveStrafing < 0F)
            rotationYaw += 90F * forward;

        return Math.toRadians(rotationYaw);
    }

    public static float getRawDirection() {
        return getRawDirectionRotation(mc2.player.rotationYaw, mc2.player.moveStrafing, mc2.player.moveForward);
    }

    public static float getRawDirection(float yaw) {
        return getRawDirectionRotation(yaw, mc2.player.moveStrafing, mc2.player.moveForward);
    }

    public static double[] getXZDist(float speed, float cYaw) {
        double[] arr = new double[2];
        final double yaw = getDirectionRotation(cYaw, mc2.player.moveStrafing, mc2.player.moveForward);
        arr[0] = -Math.sin(yaw) * speed;
        arr[1] = Math.cos(yaw) * speed;
        return arr;
    }

    public static float getPredictionYaw(double x, double z) {
        if (mc2.player == null) {
            lastX = -999999.0;
            lastZ = -999999.0;
            return 0F;
        }

        if (lastX == -999999.0)
            lastX = mc2.player.prevPosX;

        if (lastZ == -999999.0)
            lastZ = mc2.player.prevPosZ;

        float returnValue = (float) (Math.atan2(z - lastZ, x - lastX) * 180F / Math.PI);

        lastX = x;
        lastZ = z;

        return returnValue;
    }

    public static double getDirectionRotation(float yaw, float pStrafe, float pForward) {
        float rotationYaw = yaw;

        if (pForward < 0F)
            rotationYaw += 180F;

        float forward = 1F;
        if (pForward < 0F)
            forward = -0.5F;
        else if (pForward > 0F)
            forward = 0.5F;

        if (pStrafe > 0F)
            rotationYaw -= 90F * forward;

        if (pStrafe < 0F)
            rotationYaw += 90F * forward;

        return Math.toRadians(rotationYaw);
    }

    public static float getRawDirectionRotation(float yaw, float pStrafe, float pForward) {
        float rotationYaw = yaw;

        if (pForward < 0F)
            rotationYaw += 180F;

        float forward = 1F;
        if (pForward < 0F)
            forward = -0.5F;
        else if (pForward > 0F)
            forward = 0.5F;

        if (pStrafe > 0F)
            rotationYaw -= 90F * forward;

        if (pStrafe < 0F)
            rotationYaw += 90F * forward;

        return rotationYaw;
    }

    public static float getScaffoldRotation(float yaw, float strafe) {
        float rotationYaw = yaw;

        rotationYaw += 180F;

        float forward = -0.5F;

        if (strafe < 0F)
            rotationYaw -= 90F * forward;

        if (strafe > 0F)
            rotationYaw += 90F * forward;

        return rotationYaw;
    }



    public static void setMotion(MoveEvent event, double speed, double motion, boolean smoothStrafe) {
        double forward = mc2.player.movementInput.moveForward;
        double strafe = mc2.player.movementInput.moveStrafe;
        double yaw = mc2.player.rotationYaw;
        int direction = smoothStrafe ? 45 : 90;

        if ((forward == 0.0) && (strafe == 0.0)) {
            event.setX(0.0);
            event.setZ(0.0);
        } else {
            if (forward != 0.0) {
                if (strafe > 0.0) {
                    yaw += (forward > 0.0 ? -direction : direction);
                } else if (strafe < 0.0) {
                    yaw += (forward > 0.0 ? direction : -direction);
                }
                strafe = 0.0;
                if (forward > 0.0) {
                    forward = 1.0;
                } else if (forward < 0.0) {
                    forward = -1.0;
                }
            }

            double cos = Math.cos(Math.toRadians(yaw + 90.0f));
            double sin = Math.sin(Math.toRadians(yaw + 90.0f));
            event.setX((forward * speed * cos + strafe * speed * sin) * motion);
            event.setZ((forward * speed * sin - strafe * speed * cos) * motion);
        }
    }

    public static void setMotion(double speed, boolean smoothStrafe) {
        double forward = mc2.player.movementInput.moveForward;
        double strafe = mc2.player.movementInput.moveStrafe;
        float yaw = mc2.player.rotationYaw;
        int direction = smoothStrafe ? 45 : 90;

        if (forward == 0.0 && strafe == 0.0) {
            mc2.player.motionX = 0.0;
            mc2.player.motionZ = 0.0;
        } else {
            if (forward != 0.0) {
                if (strafe > 0.0) {
                    yaw += (float) (forward > 0.0 ? -direction : direction);
                } else if (strafe < 0.0) {
                    yaw += (float) (forward > 0.0 ? direction : -direction);
                }
                strafe = 0.0;
                if (forward > 0.0) {
                    forward = 1.0;
                } else if (forward < 0.0) {
                    forward = -1.0;
                }
            }

            mc2.player.motionX = forward * speed * (-Math.sin(Math.toRadians(yaw))) + strafe * speed * Math.cos(Math.toRadians(yaw));
            mc2.player.motionZ = forward * speed * Math.cos(Math.toRadians(yaw)) - strafe * speed * (-Math.sin(Math.toRadians(yaw)));
        }
    }

    public static void setSpeed(MoveEvent moveEvent, double moveSpeed) {
        setSpeed(moveEvent, moveSpeed, mc2.player.rotationYaw, (double) mc2.player.movementInput.moveStrafe, (double) mc2.player.movementInput.moveForward);
    }

    public static void setSpeed(final MoveEvent moveEvent, final double moveSpeed, final float pseudoYaw, final double pseudoStrafe, final double pseudoForward) {
        double forward = pseudoForward;
        double strafe = pseudoStrafe;
        float yaw = pseudoYaw;

        if (forward == 0.0 && strafe == 0.0) {
            moveEvent.setZ(0);
            moveEvent.setX(0);
        } else {
            if (forward != 0.0) {
                if (strafe > 0.0) {
                    yaw += ((forward > 0.0) ? -45 : 45);
                } else if (strafe < 0.0) {
                    yaw += ((forward > 0.0) ? 45 : -45);
                }
                strafe = 0.0;
                if (forward > 0.0) {
                    forward = 1.0;
                } else if (forward < 0.0) {
                    forward = -1.0;
                }
            }
            if (strafe > 0.0D) {
                strafe = 1.0D;
            } else if (strafe < 0.0D) {
                strafe = -1.0D;
            }
            final double cos = Math.cos(Math.toRadians(yaw + 90.0f));
            final double sin = Math.sin(Math.toRadians(yaw + 90.0f));

            moveEvent.setX((forward * moveSpeed * cos + strafe * moveSpeed * sin));
            moveEvent.setZ((forward * moveSpeed * sin - strafe * moveSpeed * cos));
        }
    }

    public static void resetMotion(boolean y) {
        mc2.player.motionX = 0.0;
        mc2.player.motionZ = 0.0;
        if(y) mc2.player.motionY = 0.0;
    }
    public static void updateBlocksPerSecond() {
        double bps = 0.0;
        if (mc2.player == null || mc2.player.ticksExisted < 1) {
            bps = 0.0;
        }
        double distance = mc2.player.getDistance(lastX, lastY, lastZ);
        lastX = mc2.player.posX;
        lastY = mc2.player.posY;
        lastZ = mc2.player.posZ;
        bps = distance * (20 * mc.getTimer().getTimerSpeed());
    }
    public static float getMoveYaw(float yaw) {
        Vector2f from = new Vector2f((float) mc2.player.lastTickPosX, (float) mc2.player.lastTickPosZ),
                to = new Vector2f((float) mc2.player.posX, (float) mc2.player.posZ),
                diff = new Vector2f(to.x - from.x, to.y - from.y);

        double x = diff.x, z = diff.y;
        if (x != 0 && z != 0) {
            yaw = (float) Math.toDegrees((Math.atan2(-x, z) + Math.PI * 2F) % Math.PI * 2F);
        }
        return yaw;
    }
}