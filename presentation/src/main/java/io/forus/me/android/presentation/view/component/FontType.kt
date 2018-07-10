package io.forus.me.android.presentation.view.component

import java.util.stream.Stream


public enum class FontType(public val prefix: String) {
    Bold ("bold"),
    BoldItalic("bolditalic"),
    Italic("italic"),
    Medium("medium"),
    MediumItalic("mediumitalic"),
    Regular("regular");


    fun getFontPath(): String{
        val prefix = "googlesans_"

        if (this == FontType.Bold)
            return "fonts/${prefix}bold.ttf"


        if (this == FontType.BoldItalic)
            return "fonts/${prefix}bolditalic.ttf"


        if (this == FontType.Italic)
            return "fonts/${prefix}italic.ttf"


        if (this == FontType.Medium)
            return "fonts/${prefix}medium.ttf"


        if (this == FontType.MediumItalic)
            return "fonts/${prefix}mediumitalic.ttf"


        return "fonts/${prefix}regular.ttf"
    }

    companion object {

        fun getFromString(value: String, default: FontType) : FontType {
            val items = FontType.values();
            for ( item in items) {
                if (item.prefix == value)
                    return item
            }

            return default
        }
    }
}