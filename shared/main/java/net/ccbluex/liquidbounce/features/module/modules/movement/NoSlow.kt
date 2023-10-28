/*
 * LiquidBounce Hacked Client
 * A free open source mixin-based injection hacked client for Minecraft using Minecraft Forge.
 * https://github.com/CCBlueX/LiquidBounce/
 */
package net.ccbluex.liquidbounce.features.module.modules.movement
// Oh god Thank U Recall:)


import me.aquavit.liquidsense.utils.Debug
import net.ccbluex.liquidbounce.LiquidBounce
import net.ccbluex.liquidbounce.api.enums.EnumFacingType
import net.ccbluex.liquidbounce.api.enums.WEnumHand
import net.ccbluex.liquidbounce.api.minecraft.item.IItem
import net.ccbluex.liquidbounce.api.minecraft.item.IItemStack
import net.ccbluex.liquidbounce.api.minecraft.network.play.client.*
import net.ccbluex.liquidbounce.api.minecraft.util.IEnumFacing
import net.ccbluex.liquidbounce.api.minecraft.util.WBlockPos
import net.ccbluex.liquidbounce.event.*
import net.ccbluex.liquidbounce.features.module.Module
import net.ccbluex.liquidbounce.features.module.ModuleCategory
import net.ccbluex.liquidbounce.features.module.ModuleInfo
import net.ccbluex.liquidbounce.features.module.modules.combat.KillAura
import net.ccbluex.liquidbounce.injection.backend.unwrap
import net.ccbluex.liquidbounce.utils.ClientUtils
import net.ccbluex.liquidbounce.utils.MovementUtils
import net.ccbluex.liquidbounce.utils.PacketUtils

import net.ccbluex.liquidbounce.utils.createUseItemPacket

//import net.ccbluex.liquidbounce.utils.render.BlockAnimationUtils.thePlayerisBlocking
import net.ccbluex.liquidbounce.utils.timer.MSTimer
import net.ccbluex.liquidbounce.value.BoolValue
import net.ccbluex.liquidbounce.value.FloatValue
import net.ccbluex.liquidbounce.value.IntegerValue
import net.ccbluex.liquidbounce.value.ListValue
import net.minecraft.client.gui.inventory.GuiContainer
import net.minecraft.item.*
import net.minecraft.network.Packet
import net.minecraft.network.play.INetHandlerPlayServer
import net.minecraft.network.play.client.*
import net.minecraft.network.play.server.SPacketOpenWindow
import net.minecraft.util.EnumFacing
import net.minecraft.util.EnumHand
import net.minecraft.util.math.BlockPos
import java.util.*

@ModuleInfo(name = "NoSlow", description = "Glow",
    category = ModuleCategory.MOVEMENT)
class NoSlow : Module() {

    private val modeValue = ListValue("PacketMode", arrayOf("None",  "GrimAC", "Grim",     "Vanilla","NoPacket","AAC","AAC5", "Matrix", "Vulcan", "Custom","Spoof"), "AntiCheat")
    private val blockForwardMultiplier = FloatValue("BlockForwardMultiplier", 1.0F, 0.2F, 1.0F)
    private val blockStrafeMultiplier = FloatValue("BlockStrafeMultiplier", 1.0F, 0.2F, 1.0F)
    private val consumeForwardMultiplier = FloatValue("ConsumeForwardMultiplier", 1.0F, 0.2F, 1.0F)
    private val consumeStrafeMultiplier = FloatValue("ConsumeStrafeMultiplier", 1.0F, 0.2F, 1.0F)
    private val bowForwardMultiplier = FloatValue("BowForwardMultiplier", 1.0F, 0.2F, 1.0F)
    private val bowStrafeMultiplier = FloatValue("BowStrafeMultiplier", 1.0F, 0.2F, 1.0F)
    private val customOnGround = BoolValue("CustomOnGround", false)
    private val customDelayValue = IntegerValue("CustomDelay",60,10,200)
    // real block fixer
    private val fixerValue = BoolValue("Fixer", true)
    private val debugValue = BoolValue("FixerDebuger", false)
    // Soulsand
    val soulsandValue = BoolValue("Soulsand", true)

    val timer = MSTimer()
    private val Timer = MSTimer()
    private var pendingFlagApplyPacket = false
    private val msTimer = MSTimer()
    private var sendBuf = false
    private var packetBuf = LinkedList<Packet<INetHandlerPlayServer>>()
    private var nextTemp = false
    private var waitC03 = false
    private var lastBlockingStat = false
    private var count = 0
    private var lastItem: ItemStack? = null

    val killAura = LiquidBounce.moduleManager[KillAura::class.java] as KillAura


    fun isBlock(): Boolean {
        return Debug.thePlayerisBlocking || killAura.blockingStatus
    }

    private fun OnPre(event : MotionEvent): Boolean {
        return event.eventState == EventState.PRE
    }

    private fun OnPost(event : MotionEvent): Boolean {
        return event.eventState == EventState.POST
    }

    private val isBlocking: Boolean
        get() = (mc.thePlayer!!.isUsingItem || (LiquidBounce.moduleManager[KillAura::class.java] as KillAura).blockingStatus) && mc.thePlayer!!.heldItem != null && mc.thePlayer!!.heldItem!!.item is ItemSword

    override fun onDisable() {
        Timer.reset()
        msTimer.reset()
        pendingFlagApplyPacket = false
        sendBuf = false
        packetBuf.clear()
        nextTemp = false
        waitC03 = false
    }

    private fun sendPacket(Event : MotionEvent,SendC07 : Boolean, SendC08 : Boolean,Delay : Boolean,DelayValue : Long,onGround : Boolean,Hypixel : Boolean = false) {
        val digging = classProvider.createCPacketPlayerDigging(ICPacketPlayerDigging.WAction.RELEASE_USE_ITEM, WBlockPos(-1,-1,-1),EnumFacing.DOWN as IEnumFacing)
        val blockPlace = classProvider.createCPacketPlayerBlockPlacement(mc.thePlayer!!.inventory.currentItem as IItemStack)
        val blockMent = classProvider.createCPacketPlayerBlockPlacement(WBlockPos(-1, -1, -1), 255, mc.thePlayer!!.inventory.currentItem as IItemStack, 0f, 0f, 0f)
        if(onGround && !mc.thePlayer!!.onGround) {
            return
        }

        if(SendC07 && OnPre(Event)) {
            if(Delay && Timer.hasTimePassed(DelayValue)) {
                mc.netHandler.addToSendQueue(digging)
            } else if(!Delay) {
                mc.netHandler.addToSendQueue(digging)
            }
        }
        if(SendC08 && OnPost(Event)) {
            if(Delay && Timer.hasTimePassed(DelayValue) && !Hypixel) {
                mc.netHandler.addToSendQueue(blockPlace)
                Timer.reset()
            } else if(!Delay && !Hypixel) {
                mc.netHandler.addToSendQueue(blockPlace)
            } else if(Hypixel) {
                mc.netHandler.addToSendQueue(blockMent)
            }
        }
    }
    private var needCancel = false
    @EventTarget
    fun onMotion(event: MotionEvent) {
        val thePlayer = mc.thePlayer ?: return
        val heldItem2 = thePlayer.heldItem ?: return
        if (this.fixerValue.get()) {
            if (classProvider.isItemSword(heldItem2.item) &&
                (mc.gameSettings.keyBindUseItem.pressed || (LiquidBounce.moduleManager[KillAura::class.java] as KillAura).target != null)
            ) {

                this.needCancel = true
            }
            if (classProvider.isItemSword(heldItem2.item) && !mc.gameSettings.keyBindUseItem.pressed
                && this.needCancel && (LiquidBounce.moduleManager[KillAura::class.java] as KillAura).target == null
            ) {

                val digging = classProvider.createCPacketPlayerDigging(
                    ICPacketPlayerDigging.WAction.RELEASE_USE_ITEM,
                    WBlockPos(0, 0, 0),
                    classProvider.getEnumFacing(EnumFacingType.DOWN)
                )
                mc.netHandler.addToSendQueue(digging)

                mc.thePlayer!!.stopUsingItem()

                if (this.debugValue.get())
                    thePlayer.addChatMessage(classProvider.createChatComponentText("[Ls+ Fix] Stop Blocking -- " + mc.thePlayer!!.ticksExisted))
                this.needCancel = false
            }
        }
        if (!MovementUtils.isMoving) {
            return
        }

        when(modeValue.get().toLowerCase()) {
            "custom" -> {
                sendPacket(event,true,true,true,customDelayValue.get().toLong(),customOnGround.get())
            }
            "spoof"->{
                if((event.eventState == EventState.PRE && mc.thePlayer!!.itemInUse != null && mc.thePlayer!!.itemInUse!!.item != null) && !mc.thePlayer!!.isBlocking && classProvider.isItemFood(mc.thePlayer!!.heldItem!!.item) || classProvider.isItemPotion(mc.thePlayer!!.heldItem!!.item)){
                    if(mc.thePlayer!!.isUsingItem && mc.thePlayer!!.itemInUseCount >= 1){
                        mc2.connection!!.sendPacket(CPacketHeldItemChange((mc2.player.inventory.currentItem+1)%9))
                        mc2.connection!!.sendPacket(CPacketHeldItemChange(mc2.player.inventory.currentItem))
                    }
                }
                if (event.eventState == EventState.PRE && classProvider.isItemSword(mc.thePlayer!!.heldItem!!.item)) {
                    mc.netHandler.addToSendQueue(classProvider.createCPacketPlayerDigging(ICPacketPlayerDigging.WAction.RELEASE_USE_ITEM,
                        WBlockPos.ORIGIN, classProvider.getEnumFacing(EnumFacingType.DOWN)))
                    mc.netHandler.addToSendQueue(classProvider.createCPacketPlayerBlockPlacement(mc.thePlayer!!.inventory.getCurrentItemInHand() as IItemStack))
                }
            }

            "vanilla" -> {
                mc.thePlayer!!.motionX=mc.thePlayer!!.motionX
                mc.thePlayer!!.motionY=mc.thePlayer!!.motionY
                mc.thePlayer!!.motionZ= mc.thePlayer!!.motionZ
            }
            /*
            "grim" -> {
                if (event.eventState == EventState.PRE) {
                    mc.netHandler.addToSendQueue(classProvider.createCPacketPlayerDigging(ICPacketPlayerDigging.WAction.RELEASE_USE_ITEM,
                        WBlockPos.ORIGIN, classProvider.getEnumFacing(EnumFacingType.DOWN)))
                    mc.netHandler.addToSendQueue(classProvider.createCPacketPlayerBlockPlacement(mc.thePlayer!!.inventory.getCurrentItemInHand() as IItemStack))
                }


                if((event.eventState == EventState.PRE && mc.thePlayer!!.itemInUse != null && mc.thePlayer!!.itemInUse!!.item != null) &&
                    !mc.thePlayer!!.isBlocking && classProvider.isItemFood
                        (mc.thePlayer!!.heldItem!!.item) || classProvider.isItemPotion(mc.thePlayer!!.heldItem!!.item)){
                    if(mc.thePlayer!!.isUsingItem && mc.thePlayer!!.itemInUseCount >= 1){
                        mc2.connection!!.sendPacket(CPacketHeldItemChange((mc2.player.inventory.currentItem+1)%9))
                        mc2.connection!!.sendPacket(CPacketHeldItemChange(mc2.player.inventory.currentItem))
                    }
                }
                if (event.eventState == EventState.PRE && mc2.player.getHeldItem(EnumHand.MAIN_HAND).item is ItemSword && isBlocking) {
                    mc2.connection!!.sendPacket(CPacketPlayerDigging(CPacketPlayerDigging.Action.RELEASE_USE_ITEM,
                        BlockPos.ORIGIN, EnumFacing.DOWN))
                    mc2.connection!!.sendPacket(CPacketPlayerTryUseItem(EnumHand.MAIN_HAND))
                }
                if((event.eventState == EventState.PRE && mc.thePlayer!!.itemInUse != null && mc.thePlayer!!.itemInUse!!.item != null) && !mc.thePlayer!!.isBlocking && classProvider.isItemFood(mc.thePlayer!!.heldItem!!.item) || classProvider.isItemPotion(mc.thePlayer!!.heldItem!!.item)){
                    if(mc.thePlayer!!.isUsingItem && mc.thePlayer!!.itemInUseCount >= 1){
                        mc2.connection!!.sendPacket(CPacketHeldItemChange((mc2.player.inventory.currentItem+1)%9))
                        mc2.connection!!.sendPacket(CPacketHeldItemChange(mc2.player.inventory.currentItem))
                    }
                }


            }

             */
            "aac" -> {
                if (mc.thePlayer!!.ticksExisted % 3 == 0) {
                    sendPacket(event, true, false, false, 0, false)
                } else {
                    sendPacket(event, false, true, false, 0, false)
                }
            }
            "aac5" -> {
                if (mc.thePlayer!!.isUsingItem || mc.thePlayer!!.isBlocking || isBlock()) {
                    mc.netHandler.addToSendQueue(createUseItemPacket(WEnumHand.MAIN_HAND))
                    mc.netHandler.addToSendQueue(createUseItemPacket(WEnumHand.OFF_HAND))
                }
            }
        }


    }

    @EventTarget
    fun onPacket(event: PacketEvent) {
        val packet = event.packet
        if(modeValue.equals("Matrix") || modeValue.equals("Vulcan") || modeValue.equals("Grim") && nextTemp) {
            if((packet is CPacketPlayerDigging || classProvider.isCPacketPlayerBlockPlacement(packet)) && isBlocking) {
                event.cancelEvent()
            }
            event.cancelEvent()
        }else if (packet is CPacketPlayer || packet is CPacketAnimation || packet is CPacketEntityAction || packet is CPacketUseEntity || packet is CPacketPlayerDigging || packet is ICPacketPlayerBlockPlacement) {
            if (modeValue.equals("Vulcan") && waitC03 && packet is ICPacketPlayer) {
                waitC03 = false
                return
            }
            packetBuf.add(packet as Packet<INetHandlerPlayServer>)
        }

    }

    @EventTarget
    fun onUpdate(event: UpdateEvent) {
        val heldItem = mc.thePlayer!!.heldItem!!
        if((modeValue.equals("Matrix") || modeValue.equals("Vulcan") || modeValue.equals("GrimAC")) && (lastBlockingStat || isBlocking)) {
            if(msTimer.hasTimePassed(230) && nextTemp) {
                nextTemp = false
                if(modeValue.equals("GrimAC")) {
                    PacketUtils.sendPacketNoEvent(CPacketHeldItemChange((mc2.player.inventory.currentItem + 1) % 9))
                    PacketUtils.sendPacketNoEvent(CPacketHeldItemChange(mc2.player.inventory.currentItem))
                } else {
                    PacketUtils.sendPacketNoEvent(CPacketPlayerDigging(CPacketPlayerDigging.Action.RELEASE_USE_ITEM, BlockPos(-1, -1, -1), EnumFacing.DOWN))
                }
                classProvider.createCPacketPlayerDigging(ICPacketPlayerDigging.WAction.RELEASE_USE_ITEM, WBlockPos(-1, -1, -1), EnumFacing.DOWN as IEnumFacing)
                if(packetBuf.isNotEmpty()) {
                    var canAttack = false
                    for(packet in packetBuf) {
                        if(packet is CPacketPlayer) {
                            canAttack = true
                        }
                        if(!((packet is ICPacketUseEntity || packet is ICPacketAnimation) && !canAttack)) {
                            PacketUtils.sendPacketNoEvent(packet)
                        }
                    }
                    packetBuf.clear()
                }
            }
            if(!nextTemp) {
                lastBlockingStat = isBlocking
                if (!isBlocking) {
                    return
                }
                classProvider.createCPacketPlayerBlockPlacement(WBlockPos(-1, -1, -1), 255, mc.thePlayer!!.inventory.currentItem as IItemStack, 0f, 0f, 0f)
                nextTemp = true
                waitC03 = modeValue.equals("Vulcan")
                msTimer.reset()
            }
        }
    }

    @EventTarget
    fun onSlowDown(event: SlowDownEvent) {
        val heldItem = mc.thePlayer!!.heldItem?.item

        event.forward = getMultiplier(heldItem, true)
        event.strafe = getMultiplier(heldItem, false)
    }
    @EventTarget
    fun onClick(event: ClickUpdateEvent) {
        val player = mc.thePlayer ?: return
        val currentItem = player.heldItem as ItemStack
        if(modeValue.get().toLowerCase() == "grim"){
            if (currentItem != null && (player.isUsingItem || isBlocking) && (player.moveForward != 0.0f || player.moveStrafing != 0.0f) && currentItem.item !is ItemBow) {
                if (lastItem != null && lastItem!! != currentItem) {
                    count = 0
                }
                val state = if (currentItem.item is ItemSword) 1 else 3
                if (count != state) {
                    event.cancelEvent()
                    mc.playerController.windowClick(mc.thePlayer!!.openContainer!!.windowId, 20, 1, 1,
                        mc.thePlayer!!
                    )
                    player.stopUsingItem()
                    player.closeScreen()
                    count = state
                }
                if (event.isCancelled) mc2.sendClickBlockToController(mc.currentScreen == null && mc.gameSettings.keyBindAttack.isKeyDown && mc2.inGameHasFocus)
                lastItem = currentItem
            } else {
                count = 0
            }
        }


    }


    private fun getMultiplier(item: IItem?, isForward: Boolean): Float {
        return when {
            classProvider.isItemFood(item) || classProvider.isItemPotion(item) || classProvider.isItemBucketMilk(item) -> {
                if (isForward) this.consumeForwardMultiplier.get() else this.consumeStrafeMultiplier.get()
            }
            classProvider.isItemSword(item) -> {
                if (isForward) this.blockForwardMultiplier.get() else this.blockStrafeMultiplier.get()
            }
            classProvider.isItemBow(item) -> {
                if (isForward) this.bowForwardMultiplier.get() else this.bowStrafeMultiplier.get()
            }
            else -> 0.2F
        }
    }

    override val tag: String?
        get() = modeValue.get()

}
