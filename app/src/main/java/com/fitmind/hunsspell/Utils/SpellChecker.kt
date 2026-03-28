package com.fitmind.hunsspell.Utils

import android.util.Log

/**
 * 🔤 SpellChecker
 *
 * A wrapper around native Hunspell library for:
 * - Spell checking
 * - Suggestions
 * - Auto-correction
 * - Sentence correction
 *
 * ⚠️ IMPORTANT:
 * You must call [init] before using any other method.
 *
 * Example:
 * val checker = SpellChecker()
 * checker.init(affPath, dicPath)
 */
class SpellChecker {

    /**
     * 🔧 Initializes the native Hunspell engine
     *
     * @param affPath Path to .aff file (affix rules)
     * @param dicPath Path to .dic file (dictionary words)
     *
     * Must be called before using check/suggest functions.
     */
    external fun init(affPath: String, dicPath: String)

    /**
     * ✅ Checks if a word is correctly spelled
     *
     * @param word Input word
     * @return true if correct, false otherwise
     */
    external fun check(word: String): Boolean

    /**
     * 💡 Returns spelling suggestions for a word
     *
     * @param word Input word
     * @return Array of suggested words
     */
    external fun suggest(word: String): Array<String>

    /**
     * 🧹 Normalizes input text before processing
     *
     * - Trims spaces
     * - Converts to lowercase
     * - Removes special characters (keeps letters and ')
     *
     * Example:
     * " Hello!! " → "hello"
     */
    fun normalize(word: String): String {
        return word
            .trim()
            .lowercase()
            .replace(Regex("[^a-zA-Z']"), "")
    }

    /**
     * 🔍 Checks word and returns suggestions if incorrect
     *
     * @param word Input word
     * @return Pair:
     *   - First → is correct (Boolean)
     *   - Second → suggestions (List<String>)
     *
     * Example:
     * ("helo") → (false, ["hello", "help"])
     */
    fun checkWithSuggestions(word: String): Pair<Boolean, List<String>> {
        val clean = normalize(word)
        val isCorrect = check(clean)

        return if (isCorrect) {
            true to emptyList()
        } else {
            false to suggest(clean).toList()
        }
    }

    companion object {
        init {
            /**
             * 📦 Loads native library
             *
             * This runs once when the class is loaded.
             * Make sure the .so file exists (libhunspell-lib.so)
             */
            System.loadLibrary("hunspell-lib")
            Log.d("SPELL", "Hunspell native library loaded")
        }
    }

    /**
     * ✨ Auto-corrects a single word
     *
     * - Returns the same word if correct
     * - Otherwise returns first suggestion
     * - If no suggestions → returns original word
     *
     * Example:
     * "helo" → "hello"
     */
    fun autoCorrect(word: String): String {
        val clean = normalize(word)

        if (check(clean)) return clean

        val suggestions = suggest(clean)
        return suggestions.firstOrNull() ?: word
    }

    /**
     * 📝 Auto-corrects a full sentence
     *
     * - Splits sentence by spaces
     * - Corrects each word individually
     *
     * ⚠️ Note:
     * - Does NOT preserve punctuation or formatting perfectly
     *
     * Example:
     * "helo wrld" → "hello world"
     */
    fun correctSentence(sentence: String): String {
        return sentence.split(" ").joinToString(" ") { word ->
            autoCorrect(word)
        }
    }

    /**
     * 🚨 Finds all misspelled words in a text
     *
     * @param text Input sentence/text
     * @return List of incorrect words
     *
     * Example:
     * "helo world" → ["helo"]
     */
    fun findMisspelledWords(text: String): List<String> {
        return text.split(" ")
            .map { normalize(it) }
            .filter { it.isNotEmpty() && !check(it) }
    }

    /**
     * 🔄 Switch dictionary at runtime
     *
     * Useful for:
     * - Multi-language support
     * - Dynamic dictionary download
     *
     * @param aff Path to new .aff file
     * @param dic Path to new .dic file
     */
    fun switchDictionary(aff: String, dic: String) {
        init(aff, dic)
    }
}