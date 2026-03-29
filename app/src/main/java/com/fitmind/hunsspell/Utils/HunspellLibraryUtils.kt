package com.fitmind.hunsspell.Utils

import android.content.Context
import androidx.annotation.RawRes
import java.io.File

object HunspellLibraryUtils {

    fun copyDictionary(context: Context, name: String): String {
        val file = File(context.filesDir, name)

        if (!file.exists()) {
            context.assets.open("dictionaries/$name").use { input ->
                file.outputStream().use { output ->
                    input.copyTo(output)
                }
            }
        }

        return file.absolutePath
    }


    // 📦 Data holder for paths
    data class DictionaryPaths(
        val affPath: String,
        val dicPath: String
    )

    /**
     * 🔥 Save dictionary (.aff + .dic) into internal storage
     *
     * @param baseName Example: "english", "malayalam"
     * It will save as:
     * english.aff
     * english.dic
     */
    fun saveDicAff(
        context: Context,
        baseName: String,
        affInput: java.io.InputStream,
        dicInput: java.io.InputStream
    ): DictionaryPaths {

        val affFile = File(context.filesDir, "$baseName.aff")
        val dicFile = File(context.filesDir, "$baseName.dic")

        // Save AFF
        if (!affFile.exists()) {
            affInput.use { input ->
                affFile.outputStream().use { output ->
                    input.copyTo(output)
                }
            }
        }

        // Save DIC
        if (!dicFile.exists()) {
            dicInput.use { input ->
                dicFile.outputStream().use { output ->
                    input.copyTo(output)
                }
            }
        }

        return DictionaryPaths(
            affPath = affFile.absolutePath,
            dicPath = dicFile.absolutePath
        )
    }

    /**
     * 📂 Get stored dictionary paths by base name
     */
    fun getFilePathToLib(context: Context, baseName: String): DictionaryPaths {
        val affFile = File(context.filesDir, "$baseName.aff")
        val dicFile = File(context.filesDir, "$baseName.dic")

        return DictionaryPaths(
            affPath = affFile.absolutePath,
            dicPath = dicFile.absolutePath
        )
    }





        /**
         * 🔥 Copies .aff and .dic from res/raw into internal storage (if not already)
         * and returns absolute file paths for Hunspell usage.
         *
         * @param baseName Used for saved file names (e.g., "english")
         * @param affResId Resource ID of .aff file
         * @param dicResId Resource ID of .dic file
         */
        fun prepareDictionaryFromRaw(
            context: Context,
            baseName: String,
            @RawRes affResId: Int,
            @RawRes dicResId: Int
        ): DictionaryPaths {

            val affFile = File(context.filesDir, "$baseName.aff")
            val dicFile = File(context.filesDir, "$baseName.dic")

            // Copy AFF if not exists
            if (!affFile.exists()) {
                context.resources.openRawResource(affResId).use { input ->
                    affFile.outputStream().use { output ->
                        input.copyTo(output)
                    }
                }
            }

            // Copy DIC if not exists
            if (!dicFile.exists()) {
                context.resources.openRawResource(dicResId).use { input ->
                    dicFile.outputStream().use { output ->
                        input.copyTo(output)
                    }
                }
            }

            return DictionaryPaths(
                affPath = affFile.absolutePath,
                dicPath = dicFile.absolutePath
            )
        }

}