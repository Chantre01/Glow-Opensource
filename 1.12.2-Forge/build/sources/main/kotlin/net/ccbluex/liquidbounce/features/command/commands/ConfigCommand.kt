package net.ccbluex.liquidbounce.features.command.commands

import com.google.gson.JsonElement
import com.google.gson.JsonNull
import com.google.gson.JsonObject
import com.google.gson.JsonParser
import net.ccbluex.liquidbounce.LiquidBounce
import net.ccbluex.liquidbounce.features.command.Command
import net.ccbluex.liquidbounce.features.module.EnumAutoDisableType
import net.ccbluex.liquidbounce.features.module.Module
import net.ccbluex.liquidbounce.features.special.AntiForge
import net.ccbluex.liquidbounce.features.special.AutoReconnect.delay
import net.ccbluex.liquidbounce.features.special.BungeeCordSpoof
import net.ccbluex.liquidbounce.file.FileManager
import net.ccbluex.liquidbounce.ui.client.GuiBackground.Companion.enabled
import net.ccbluex.liquidbounce.ui.client.GuiBackground.Companion.particles
import net.ccbluex.liquidbounce.ui.client.altmanager.sub.GuiDonatorCape.Companion.capeEnabled
import net.ccbluex.liquidbounce.ui.client.altmanager.sub.GuiDonatorCape.Companion.transferCode
import net.ccbluex.liquidbounce.ui.client.altmanager.sub.altgenerator.GuiTheAltening.Companion.apiKey
import net.ccbluex.liquidbounce.ui.client.hud.Config
import net.ccbluex.liquidbounce.ui.client.hud.element.elements.NotifyType
import net.ccbluex.liquidbounce.utils.ClientUtils
import net.ccbluex.liquidbounce.utils.EntityUtils
import net.ccbluex.liquidbounce.utils.OldConfigSupport
import net.ccbluex.liquidbounce.utils.misc.MiscTransformer
import net.ccbluex.liquidbounce.value.Value
import org.apache.commons.io.FileUtils
import java.awt.Desktop
import java.io.*
import java.util.function.Consumer

/**
@author ChengFeng
@since 2022/11/26
 */
class ConfigCommand : Command("config", "cfg") {
    override fun execute(args: Array<String>) {
        if (args.size >= 2) {

            when (args[1].toLowerCase()) {
                "list" -> {
                    chat("§cConfig list:")

                    val settings = this.getLocalSettings() ?: return

                    for (file in settings)
                        chat(file.name)
                    return
                }

                "delete" -> {
                    if (args.size > 2) {
                        val file = File(LiquidBounce.fileManager.settingsDir, "${args[2]}.json.")
                        if (file.exists()) {
                            try {
                                FileUtils.forceDelete(file)
                                chat("Deleted config " + args[2] + ".json.")
                                return
                            } catch (e: Exception) {
                                chat("Failed to delete config " + args[2] + ".json.")
                                return
                            }
                        }
                        chat("Cannot find config " + args[2] + ".json.")
                        return
                    }
                    chatSyntax("config delete <name>")
                }

                "load" -> {
                    if (args.size > 2) {
                        val file = File(LiquidBounce.fileManager.settingsDir, "${args[2]}.json")
                        if (file.exists()) {
                            val jsonElement = JsonParser().parse(BufferedReader(FileReader(file)))
                            try {
                                if (jsonElement is JsonNull) {
                                    chat("Cannot find config " + args[2] + ".json.")
                                    return
                                }
                                val jsonObject = jsonElement as JsonObject

                                val iterator: Iterator<Map.Entry<String, JsonElement>> =
                                    jsonObject.entrySet().iterator()
                                while (iterator.hasNext()) {
                                    val (key, value) = iterator.next()
                                    if (key.equals("CommandPrefix", ignoreCase = true)) {
                                        LiquidBounce.commandManager.prefix = value.asCharacter
                                    } else if (key.equals("targets", ignoreCase = true)) {
                                        val jsonValue = value as JsonObject
                                        if (jsonValue.has("TargetPlayer")) EntityUtils.targetPlayer =
                                            jsonValue["TargetPlayer"].asBoolean
                                        if (jsonValue.has("TargetMobs")) EntityUtils.targetMobs =
                                            jsonValue["TargetMobs"].asBoolean
                                        if (jsonValue.has("TargetAnimals")) EntityUtils.targetAnimals =
                                            jsonValue["TargetAnimals"].asBoolean
                                        if (jsonValue.has("TargetInvisible")) EntityUtils.targetInvisible =
                                            jsonValue["TargetInvisible"].asBoolean
                                        if (jsonValue.has("TargetDead")) EntityUtils.targetDead =
                                            jsonValue["TargetDead"].asBoolean
                                    } else if (key.equals("features", ignoreCase = true)) {
                                        val jsonValue = value as JsonObject
                                        if (jsonValue.has("AntiForge")) AntiForge.enabled =
                                            jsonValue["AntiForge"].asBoolean
                                        if (jsonValue.has("AntiForgeFML")) AntiForge.blockFML =
                                            jsonValue["AntiForgeFML"].asBoolean
                                        if (jsonValue.has("AntiForgeProxy")) AntiForge.blockProxyPacket =
                                            jsonValue["AntiForgeProxy"].asBoolean
                                        if (jsonValue.has("AntiForgePayloads")) AntiForge.blockPayloadPackets =
                                            jsonValue["AntiForgePayloads"].asBoolean
                                        if (jsonValue.has("BungeeSpoof")) BungeeCordSpoof.enabled =
                                            jsonValue["BungeeSpoof"].asBoolean
                                        if (jsonValue.has("AutoReconnectDelay")) delay =
                                            jsonValue["AutoReconnectDelay"].asInt
                                    } else if (key.equals("thealtening", ignoreCase = true)) {
                                        val jsonValue = value as JsonObject
                                        if (jsonValue.has("API-Key")) apiKey = jsonValue["API-Key"].asString
                                    } else if (key.equals("DonatorCape", ignoreCase = true)) {
                                        val jsonValue = value as JsonObject
                                        if (jsonValue.has("TransferCode")) transferCode =
                                            jsonValue["TransferCode"].asString
                                        if (jsonValue.has("CapeEnabled")) capeEnabled =
                                            jsonValue["CapeEnabled"].asBoolean
                                    } else if (key.equals("Background", ignoreCase = true)) {
                                        val jsonValue = value as JsonObject
                                        if (jsonValue.has("Enabled")) enabled = jsonValue["Enabled"].asBoolean
                                        if (jsonValue.has("Particles")) particles = jsonValue["Particles"].asBoolean
                                    } else {
                                        val module = LiquidBounce.moduleManager.getModule(key)
                                        if (module != null) {
                                            val jsonModule = value as JsonObject
                                            for (moduleValue in module.values) {
                                                val element = jsonModule[moduleValue.name]
                                                if (element != null) moduleValue.fromJson(element)
                                            }
                                        }
                                        if (module != null) {
                                            val jsonModule = value as JsonObject
                                            module.state = jsonModule["State"].asBoolean
                                            module.keyBind = jsonModule["KeyBind"].asInt
                                            module.autoDisable =
                                                EnumAutoDisableType.valueOf(jsonModule["AutoDisable"].asString.toUpperCase())
                                            if (jsonModule.has("Array")) module.array = jsonModule["Array"].asBoolean
                                        }
                                    }
                                }

                                chat("Loaded config " + args[2] + ".json.")
                                MiscTransformer.notificationsTransform("Notifications", "Loaded config ${args[2]}.json.", NotifyType.WARNING,1000)
                                playEdit()
                                return
                            } catch (e: Throwable) {
                                chat("§cFailed to load config " + args[2] + ".json.")
                                ClientUtils.getLogger().error(e)
                                return
                            }
                        }
                    }
                    chatSyntax("config load <name>")
                    return
                }

                "save" -> {
                    if (args.size > 2) {
                        try {

                            val jsonObject = JsonObject()

                            jsonObject.addProperty("CommandPrefix", LiquidBounce.commandManager.prefix)

                            val jsonTargets = JsonObject()
                            jsonTargets.addProperty("TargetPlayer", EntityUtils.targetPlayer)
                            jsonTargets.addProperty("TargetMobs", EntityUtils.targetMobs)
                            jsonTargets.addProperty("TargetAnimals", EntityUtils.targetAnimals)
                            jsonTargets.addProperty("TargetInvisible", EntityUtils.targetInvisible)
                            jsonTargets.addProperty("TargetDead", EntityUtils.targetDead)
                            jsonObject.add("targets", jsonTargets)

                            val jsonFeatures = JsonObject()
                            jsonFeatures.addProperty("AntiForge", AntiForge.enabled)
                            jsonFeatures.addProperty("AntiForgeFML", AntiForge.blockFML)
                            jsonFeatures.addProperty("AntiForgeProxy", AntiForge.blockProxyPacket)
                            jsonFeatures.addProperty("AntiForgePayloads", AntiForge.blockPayloadPackets)
                            jsonFeatures.addProperty("BungeeSpoof", BungeeCordSpoof.enabled)
                            jsonFeatures.addProperty("AutoReconnectDelay", delay)
                            jsonObject.add("features", jsonFeatures)

                            val theAlteningObject = JsonObject()
                            theAlteningObject.addProperty("API-Key", apiKey)
                            jsonObject.add("thealtening", theAlteningObject)

                            LiquidBounce.moduleManager.modules.stream()
                                .forEach { module: Module ->
                                    val jsonModule = JsonObject()
                                    jsonModule.addProperty("State", module.state)
                                    jsonModule.addProperty("KeyBind", module.keyBind)
                                    jsonModule.addProperty("Array", module.array)
                                    jsonModule.addProperty("AutoDisable", module.autoDisable.toString().toUpperCase())

                                    module.values.forEach(Consumer { value: Value<*> ->
                                        jsonModule.add(
                                            value.name,
                                            value.toJson()
                                        )
                                    })
                                    jsonObject.add(module.name, jsonModule)
                                }

                            val printWriter =
                                PrintWriter(FileWriter(File(LiquidBounce.fileManager.settingsDir, "${args[2]}.json")))
                            printWriter.println(FileManager.PRETTY_GSON.toJson(jsonObject))
                            printWriter.close()
                            chat("Saved config " + args[2] + ".json.")
                            return
                        } catch (e: Throwable) {
                            chat("§cFailed to save config " + args[2] + ".json.")
                            return
                        }
                    }
                    chatSyntax("config save <name>")
                    return
                }
                "folder" -> {
                    try {
                        Desktop.getDesktop().open(LiquidBounce.fileManager.settingsDir)
                        chat("Succeed to open the config folder")
                        return
                    } catch (t: Throwable) {
                        ClientUtils.getLogger().error("Error while opening the config folder.", t)
                        chat("${t.javaClass.name}: ${t.message}")
                        return
                    }
                }
                "load-old" -> {
                    if (args.size > 2) {
                        val scriptFile = File(LiquidBounce.fileManager.settingsDir, (args[2] + ".json"))
                        if (scriptFile.exists()) {
                            try {
                                val settings = scriptFile.readText()
                                OldConfigSupport.loadConfig(settings)
                                chat("Loaded config " + args[2] + ".json.")
                                MiscTransformer.notificationsTransform("Notifications", "Loaded config ${args[2]}.json.", NotifyType.WARNING, 1000)
                                playEdit()
                            } catch (e: IOException) {
                                e.printStackTrace()
                            }
                            return
                        }
                        chat("§cCanoot find config " + args[2] + ".json.")

                        return
                    }

                    chatSyntax("config load-old <name>")
                    return
                }
            }
        }
        chatSyntax("config <load/load-old/save/list/delete>")
    }

    override fun tabComplete(args: Array<String>): List<String> {
        if (args.isEmpty()) return emptyList()

        return when (args.size) {
            1 -> listOf("delete", "list", "load", "load-old", "save","folder").filter { it.startsWith(args[0], true) }
            2 -> {
                when (args[0].toLowerCase()) {
                    "delete", "load", "save","load-old" -> {
                        val settings = this.getLocalSettings() ?: return emptyList()

                        return settings
                            .map { it.name.replace(".json","") }
                            .filter { it.startsWith(args[1], true) }
                    }
                }
                return emptyList()
            }
            else -> emptyList()
        }
    }

    private fun getLocalSettings(): Array<File>? = LiquidBounce.fileManager.settingsDir.listFiles()

}