package net.ccbluex.liquidbounce.features.command.commands

import net.ccbluex.liquidbounce.LiquidBounce
import net.ccbluex.liquidbounce.features.command.Command
import net.ccbluex.liquidbounce.features.module.EnumAutoDisableType
import net.ccbluex.liquidbounce.utils.misc.StringUtils

class AutoDisableCommand : Command("autodisable", *arrayOf("ad")) {
    private val modes = EnumAutoDisableType.values().map { it.name.toLowerCase() }.toTypedArray()

    override fun execute(args: Array<String>) {
        if (args.size > 2) {
            val module = LiquidBounce.moduleManager.getModule(args[1])

            if (module == null) {
                chat("Cannot find §7${args[1]} §fModule")
                return
            }

            module.autoDisable = try {
                EnumAutoDisableType.valueOf(args[2].toUpperCase())
            } catch (e: IllegalArgumentException) {
                EnumAutoDisableType.NONE
            }
            playEdit()

            chat("Set §7${module.name} §rAutoDisable mode to §7${module.autoDisable}§r 。")

            return
        }

        chatSyntax("autodisable <module> [${StringUtils.toCompleteString2(modes,0,",")}]")
    }

    override fun tabComplete(args: Array<String>): List<String> {
        if (args.isEmpty()) return emptyList()

        return when (args.size) {
            1 -> LiquidBounce.moduleManager.modules
                .map { it.name }
                .filter { it.startsWith(args[0], true) }
                .toList()

            2 -> modes.filter { it.startsWith(args[1], true) }

            else -> emptyList()
        }
    }
}
