package cn.manager

import net.ccbluex.liquidbounce.LiquidBounce
import net.ccbluex.liquidbounce.event.*
import net.minecraft.entity.EntityLivingBase
import net.minecraft.network.handshake.client.C00Handshake
import net.minecraft.network.play.server.SPacketTitle

object CombatListener : Listenable {
    var syncEntity: EntityLivingBase? = null
    var killCounts = 0
    var totalPlayed = 0
    var win = 0
    var startTime = System.currentTimeMillis()

    @EventTarget
    private fun onAttack(event: AttackEvent) { syncEntity = event.targetEntity as EntityLivingBase?
    }

    @EventTarget
    private fun onUpdate(event: UpdateEvent) {
        if(syncEntity != null && syncEntity!!.isDead) {
            ++killCounts
            syncEntity = null
        }
    }

    @EventTarget(ignoreCondition = true)
    private fun onPacket(event: PacketEvent) {
        val packet = event.packet
        if (event.packet is C00Handshake) startTime = System.currentTimeMillis()

        if (packet is SPacketTitle) {
            val title = packet.message.formattedText
            if(title.contains("Winner")){
                win++
            }
            if(title.contains("BedWar")){
                totalPlayed++
            }
            if(title.contains("SkyWar")){
                totalPlayed++
            }
        }
    }

    override fun handleEvents() = true

    init {
        LiquidBounce.eventManager.registerListener(this)
    }
}