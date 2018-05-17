package io.forus.me.helpers

import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import java.io.File
import java.io.FileOutputStream

interface CacheHelper {

    companion object {
        private val bitmapCaches: MutableMap<String, Bitmap> = mutableMapOf()

        /**
         * Add bitmap to cache of device. Returns true if successfully added, false if not.
         */
        fun add(context: Context, name:String, bitmap: Bitmap): Boolean {
            val n = getPreparedName(name)
            try {
                val fileOutput = FileOutputStream("${context.cacheDir}/$n")
                bitmap.compress(Bitmap.CompressFormat.PNG, 100, fileOutput)
                return true
            } catch (e:Exception) {}
            return false
        }

        fun getBitmapCache(context: Context, name:String): Bitmap? {
            val n = getPreparedName(name)
            //File.createTempFile(BITMAP_DIRECTORY, n, context.cacheDir)
            var ret: Bitmap? = null
            if (bitmapCaches.containsKey(n)) {
                ret = bitmapCaches[n]
            } else {
                val file = File(context.cacheDir, n)
                if (file.exists()) {
                    val options = BitmapFactory.Options()
                    options.inPreferredConfig = Bitmap.Config.ARGB_8888
                    ret = BitmapFactory.decodeFile(file.path, options)
                    if (ret != null) {
                        bitmapCaches[n] = ret
                    }
                }
            }
            return ret
        }

        private fun getPreparedName(name:String): String {
            var n = name
            if (!n.endsWith(".png")) {
                n += ".png"
            }
            n = n.replace(Regex("[\"\n]"),"")
            if (n.contains("\"")) throw Exception("Invalide karakter")
            return n
        }
    }
}