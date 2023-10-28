/*
 * LiquidBounce Hacked Client
 * A free open source mixin-based injection hacked client for Minecraft using Minecraft Forge.
 * https://github.com/CCBlueX/LiquidBounce/
 */
package net.ccbluex.liquidbounce.value

import com.google.gson.JsonElement
import com.google.gson.JsonObject
import com.google.gson.JsonPrimitive
import net.ccbluex.liquidbounce.LiquidBounce
import net.ccbluex.liquidbounce.api.minecraft.client.gui.IFontRenderer
import net.ccbluex.liquidbounce.ui.font.Fonts
import net.ccbluex.liquidbounce.utils.ClientUtils
import java.awt.Color
import java.util.*

abstract class Value<T>(val name: String, var value: T) {
    var isSupported = true
    var hide = false
    fun set(newValue: T) {
        if (newValue == value)
            return

        val oldValue = get()

        try {
            onChange(oldValue, newValue)
            changeValue(newValue)
            onChanged(oldValue, newValue)
            LiquidBounce.fileManager.saveConfig(LiquidBounce.fileManager.valuesConfig)
        } catch (e: Exception) {
            ClientUtils.getLogger().error("[ValueSystem ($name)]: ${e.javaClass.name} (${e.message}) [$oldValue >> $newValue]")
        }
    }

    fun get() = value
    private var displayN: () -> String = { "" }
    val displayName: String
        get() = displayN()


    open fun changeValue(value: T) {
        this.value = value
    }

    abstract fun toJson(): JsonElement?
    abstract fun fromJson(element: JsonElement)

    protected open fun onChange(oldValue: T, newValue: T) {}
    protected open fun onChanged(oldValue: T, newValue: T) {}

}
open class ColorValue(name : String, value: Int) : Value<Int>(name, value) {
    open fun getValue(): Int {
        return super.get()
    }

    fun set(newValue: Number) {
        set(newValue.toInt())
    }

    var Expanded = false

    open fun isExpanded(): Boolean {
        return Expanded
    }

    override fun toJson() = JsonPrimitive(getValue())

    override fun fromJson(element: JsonElement) {
        if(element.isJsonPrimitive)
            value = element.asInt
    }

    open fun getHSB(): FloatArray {
        val hsbValues = FloatArray(3)
        val saturation: Float
        val brightness: Float
        var hue: Float
        var cMax: Int = Math.max(getValue() ushr 16 and 0xFF, getValue() ushr 8 and 0xFF)
        if (getValue() and 0xFF > cMax) cMax = getValue() and 0xFF
        var cMin: Int = Math.min(getValue() ushr 16 and 0xFF, getValue() ushr 8 and 0xFF)
        if (getValue() and 0xFF < cMin) cMin = getValue() and 0xFF
        brightness = cMax.toFloat() / 255.0f
        saturation = if (cMax != 0) (cMax - cMin).toFloat() / cMax.toFloat() else 0F
        if (saturation == 0f) {
            hue = 0f
        } else {
            val redC: Float = (cMax - (getValue() ushr 16 and 0xFF)).toFloat() / (cMax - cMin).toFloat()
            // @off
            val greenC: Float = (cMax - (getValue() ushr 8 and 0xFF)).toFloat() / (cMax - cMin).toFloat()
            val blueC: Float = (cMax - (getValue() and 0xFF)).toFloat() / (cMax - cMin).toFloat() // @on
            hue =
                (if (getValue() ushr 16 and 0xFF == cMax) blueC - greenC else if (getValue() ushr 8 and 0xFF == cMax) 2.0f + redC - blueC else 4.0f + greenC - redC) / 6.0f
            if (hue < 0) hue += 1.0f
        }
        hsbValues[0] = hue
        hsbValues[1] = saturation
        hsbValues[2] = brightness
        return hsbValues
    }

    open fun getAwtColor() : Color {
        return Color(value, true);
    }

}

/**
 * Bool value represents a value with a boolean
 */
open class BoolValue(name: String, value: Boolean) : Value<Boolean>(name, value) {

    override fun toJson() = JsonPrimitive(value)
    fun toggle(){
        value=!value;
    }
    override fun fromJson(element: JsonElement) {
        if (element.isJsonPrimitive)
            value = element.asBoolean || element.asString.equals("true", ignoreCase = true)
    }

}

/**
 * Integer value represents a value with a integer
 */
open class IntegerValue(name: String, value: Int, val minimum: Int = 0, val maximum: Int = Integer.MAX_VALUE)
    : Value<Int>(name, value) {

    fun set(newValue: Number) {
        set(newValue.toInt())
    }

    override fun toJson() = JsonPrimitive(value)

    override fun fromJson(element: JsonElement) {
        if (element.isJsonPrimitive)
            value = element.asInt
    }

}

/**
 * Float value represents a value with a float
 */
open class FloatValue(name: String, value: Float, val minimum: Float = 0F, val maximum: Float = Float.MAX_VALUE)
    : Value<Float>(name, value) {

    fun set(newValue: Number) {
        set(newValue.toFloat())
    }

    override fun toJson() = JsonPrimitive(value)

    override fun fromJson(element: JsonElement) {
        if (element.isJsonPrimitive)
            value = element.asFloat
    }

}

/**
 * Text value represents a value with a string
 */
open class TextValue(name: String, value: String) : Value<String>(name, value) {

    var TextHovered = false
    override fun toJson() = JsonPrimitive(value)

    override fun fromJson(element: JsonElement) {
        if (element.isJsonPrimitive)
            value = element.asString
    }
    fun append(o: Any): TextValue {
        set(get() + o)
        return this
    }
}

/**
 * Font value represents a value with a font
 */
class FontValue(valueName: String, value: IFontRenderer) : Value<IFontRenderer>(valueName, value) {

    override fun toJson(): JsonElement? {
        val fontDetails = Fonts.getFontDetails(value) ?: return null
        val valueObject = JsonObject()
        valueObject.addProperty("fontName", fontDetails.name)
        valueObject.addProperty("fontSize", fontDetails.fontSize)
        return valueObject
    }

    override fun fromJson(element: JsonElement) {
        if (!element.isJsonObject) return
        val valueObject = element.asJsonObject
        value = Fonts.getFontRenderer(valueObject["fontName"].asString, valueObject["fontSize"].asInt)
    }
}

/**
 * Block value represents a value with a block
 */
class BlockValue(name: String, value: Int) : IntegerValue(name, value, 1, 197)

/**
 * List value represents a selectable list of values
 */
open class ListValue(name: String, val values: Array<String>, value: String) : Value<String>(name, value) {

    @JvmField
    var openList = false

    init {
        this.value = value
    }

    operator fun contains(string: String?): Boolean {
        return Arrays.stream(values).anyMatch { s: String -> s.equals(string, ignoreCase = true) }
    }
    open fun getModes() : List<String> {
        return this.values.toList()
    }
    fun getModeListNumber(modeName: String) : Int {
        for(i in this.values.indices) {
            if(values[i] == modeName) {
                return i
            }
        }
        return 0
    }

    open fun getModeGet(i: Int): String {
        return values[i]
    }


    override fun changeValue(value: String) {
        for (element in values) {
            if (element.equals(value, ignoreCase = true)) {
                this.value = element
                break
            }
        }
    }

    override fun toJson() = JsonPrimitive(value)

    override fun fromJson(element: JsonElement) {
        if (element.isJsonPrimitive) changeValue(element.asString)
    }


}