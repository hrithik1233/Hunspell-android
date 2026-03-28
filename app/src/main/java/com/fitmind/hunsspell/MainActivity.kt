package com.fitmind.hunsspell

import android.content.Context
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import com.fitmind.hunsspell.Utils.HunspellLibraryUtils.copyDictionary
import com.fitmind.hunsspell.Utils.SpellChecker
import com.fitmind.hunsspell.ui.theme.HunsSpellTheme
import java.io.File

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            HunsSpellTheme {
                MainScreen()
            }
        }
    }
}

// 📁 Copy dictionary


@OptIn(ExperimentalMaterial3Api::class, ExperimentalLayoutApi::class)
@Composable
fun MainScreen() {

    var inputText by remember { mutableStateOf("") }
    var suggestions by remember { mutableStateOf(listOf<String>()) }
    var selectedWord by remember { mutableStateOf<String?>(null) }
    var correctedSentence by remember { mutableStateOf("") }
    var misspelledWords by remember { mutableStateOf(listOf<String>()) }

    val context = LocalContext.current

    // ✅ init once
    val spellChecker = remember {
        val aff = copyDictionary(context, "en_US.aff")
        val dic = copyDictionary(context, "en_US.dic")

        Log.d("SPELL", "AFF exists: ${File(aff).exists()}")
        Log.d("SPELL", "DIC exists: ${File(dic).exists()}")

        SpellChecker().apply {
            init(aff, dic)
        }
    }

    Scaffold(
        topBar = {
            TopAppBar(title = { Text("Hunspell Playground 🚀") })
        }
    ) { padding ->

        Column(
            modifier = Modifier
                .padding(padding)
                .padding(16.dp)
                .fillMaxSize()
        ) {

            // ✏️ INPUT
            OutlinedTextField(
                value = inputText,
                onValueChange = { inputText = it },
                label = { Text("Enter text") },
                modifier = Modifier.fillMaxWidth()
            )

            Spacer(modifier = Modifier.height(12.dp))

            // 🔘 ACTION BUTTONS
            Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {

                Button(onClick = {
                    suggestions = spellChecker.suggest(inputText).toList()
                }) {
                    Text("Suggest")
                }

                Button(onClick = {
                    inputText = spellChecker.autoCorrect(inputText)
                }) {
                    Text("AutoCorrect")
                }
            }

            Spacer(modifier = Modifier.height(8.dp))

            Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {

                Button(onClick = {
                    correctedSentence = spellChecker.correctSentence(inputText)
                }) {
                    Text("Fix Sentence")
                }

                Button(onClick = {
                    misspelledWords = spellChecker.findMisspelledWords(inputText)
                }) {
                    Text("Find Errors")
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            // 🧠 SUGGESTIONS (CHIPS)
            Text("Suggestions:", style = MaterialTheme.typography.titleMedium)

            FlowRow(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(8.dp),
                verticalArrangement = Arrangement.spacedBy(8.dp)
            )
            {

                suggestions.forEach { word ->

                    SuggestionChip(
                        onClick = {
                            inputText = word
                            selectedWord = word

                            Toast.makeText(
                                context,
                                "Selected: $word",
                                Toast.LENGTH_SHORT
                            ).show()
                        },
                        label = { Text(word) },
                        colors = SuggestionChipDefaults.suggestionChipColors(
                            containerColor = if (word == selectedWord)
                                MaterialTheme.colorScheme.primaryContainer
                            else
                                MaterialTheme.colorScheme.surface
                        )
                    )
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            // ✍️ CORRECTED SENTENCE
            if (correctedSentence.isNotEmpty()) {
                Text(
                    text = "Corrected: $correctedSentence",
                    style = MaterialTheme.typography.bodyLarge
                )
            }

            Spacer(modifier = Modifier.height(12.dp))

            // ❌ MISSPELLED WORDS
            if (misspelledWords.isNotEmpty()) {
                Text(
                    text = "Misspelled: ${misspelledWords.joinToString()}",
                    color = MaterialTheme.colorScheme.error
                )
            }
        }
    }
}