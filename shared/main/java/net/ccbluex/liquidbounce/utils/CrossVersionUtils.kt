/*
 * ColorByte Hacked Client
 * A free half-open source mixin-based injection hacked client for Minecraft using Minecraft Forge.
 * https://github.com/SkidderRyF/ColorByte/
 */
package net.ccbluex.liquidbounce.utils

import net.ccbluex.liquidbounce.LiquidBounce
import net.ccbluex.liquidbounce.api.enums.WEnumHand
import net.ccbluex.liquidbounce.api.minecraft.network.IPacket
import net.ccbluex.liquidbounce.api.minecraft.network.play.client.ICPacketEntityAction
import net.ccbluex.liquidbounce.injection.backend.WrapperImpl.classProvider
fun createUseItemPacket(hand: WEnumHand): IPacket {
    return classProvider.createCPacketTryUseItem(hand)
}

fun createOpenInventoryPacket(): IPacket {
    return  classProvider.createCPacketEntityAction(
        LiquidBounce.wrapper.minecraft.thePlayer!!,
        ICPacketEntityAction.WAction.OPEN_INVENTORY
    )
}