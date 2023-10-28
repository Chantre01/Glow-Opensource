/*
 * ColorByte Hacked Client
 * A free half-open source mixin-based injection hacked client for Minecraft using Minecraft Forge.
 * https://github.com/SkidderRyF/ColorByte/
 */
package net.ccbluex.liquidbounce.features.module

import net.ccbluex.liquidbounce.api.MinecraftVersion
import org.lwjgl.input.Keyboard

@Retention(AnnotationRetention.RUNTIME)
annotation class ModuleInfo(
    val name: String,
    val description: String,
    val category: ModuleCategory,
    val keyBind: Int = Keyboard.CHAR_NONE,
    val canEnable: Boolean = true,
    val array: Boolean = true,
    val supportedVersions: Array<MinecraftVersion> = [MinecraftVersion.MC_1_8, MinecraftVersion.MC_1_12],
    val autoDisable: EnumAutoDisableType = EnumAutoDisableType.NONE,
    val triggerType: EnumTriggerType = EnumTriggerType.TOGGLE
)

enum class EnumAutoDisableType {
    NONE,
    RESPAWN,
    FLAG,
    GAME_END
}

enum class EnumTriggerType {
    TOGGLE
}
