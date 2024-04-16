package com.example.cactiapp

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Done
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.core.text.isDigitsOnly
import com.example.cactiapp.ui.theme.CactiAppTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            CactiAppTheme {
                var expandAddCactusScreen by rememberSaveable { mutableStateOf(false) }

                Scaffold(

                    floatingActionButton = {
                        if (!expandAddCactusScreen) {
                            AddCactusButton(onAddCactusButtonClicked = {
                                expandAddCactusScreen = true
                            })
                        }
                    }

                ) { innerPadding ->

                    Surface(
                        modifier = Modifier
                            .padding(innerPadding),

                        color = MaterialTheme.colorScheme.background
                    ) {
                        if (expandAddCactusScreen) {
                            AddCactusScreen(exitAddCactusScreen = { expandAddCactusScreen = false })
                        } else {
                            CactiGarden()
                        }
                    }
                }

                // A surface container using the 'background' color from the theme


            }
        }
    }

    //
    @Composable
    fun CactiGarden(
        modifier: Modifier = Modifier
    ) {
        LazyRow(modifier.padding(25.dp)) {
            items(CactiManager.getCacti()) {

                    cactus ->
                CactusDisplay(cactus)
            }


        }
    }

    @Composable
    fun CactusDisplay(
        cactus: Cactus,
        modifier: Modifier = Modifier
    ) {
        var expanded by rememberSaveable { mutableStateOf(false) }

        Surface(
            onClick = { expanded = !expanded },
            modifier.padding(25.dp)
        ) {
            Column {
                Text(cactus.name)
                Row {
                    Box(
                        modifier
                            .size(25.dp, 300.dp)
                            .clip(RectangleShape)
                            .background(Color.Green),

                        ) {
                    }
                    if (expanded) {
                        CactusInfo(cactus)
                    }
                }
            }


        }

    }

    @Composable
    fun CactusInfo(cactus: Cactus) {
        Column {
            Text("Species:${cactus.species}")
            Text("Current Height: ${cactus.height}")
            Text("Current Age:${cactus.age}")
        }

    }

    @Composable
    fun AddCactusButton(
        onAddCactusButtonClicked: () -> Unit
    ) {
        FloatingActionButton(
            onClick = onAddCactusButtonClicked
        ) {
            Icon(Icons.Filled.Add, "Add Cactus Button.")
        }

    }

    @OptIn(ExperimentalMaterial3Api::class)
    @Composable
    fun AddCactusScreen(exitAddCactusScreen: () -> Unit) {
        var name by remember { mutableStateOf("") }
        var height by remember { mutableStateOf("") }
        var species by remember { mutableStateOf("") }
        var age by remember { mutableStateOf("") }


        var newCactus by remember { mutableStateOf(Cactus("", "", "", "")) }
        Scaffold(
            topBar = {
                CenterAlignedTopAppBar(
                    title = {
                        Text(
                            text = "Add your Cactus!",
                            maxLines = 1,
                            overflow = TextOverflow.Ellipsis
                        )
                    },
                    navigationIcon = {
                        IconButton(onClick = { exitAddCactusScreen(); }) {
                            Icon(Icons.Filled.Delete, "Discard Cactus Button")
                        }
                    },
                    actions = {
                        IconButton(onClick = {
                            exitAddCactusScreen();CactiManager.addCactus(
                            newCactus
                        )
                        }) {
                            Icon(Icons.Filled.Done, "Save Cactus Button")
                        }

                    },


                    )
            }) { innerPadding ->
            Surface(
                Modifier
                    .background(MaterialTheme.colorScheme.background)
                    .padding(innerPadding)
            ) {
                Column {
                    OutlinedTextField(
                        value = name,
                        onValueChange = { newCactus.name = it; name = it },
                        label = { Text("Custom Name:") }
                    )
                    OutlinedTextField(
                        value = species,
                        onValueChange = { newCactus.species = it; species = it },
                        label = { Text("Species:") }
                    )
                    OutlinedTextField(
                        value = height,
                        onValueChange = {
                            if (it.isDigitsOnly()) {
                                newCactus.height = it
                                height = it
                            }
                        },
                        label = { Text("Current Height:") },
                        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number)
                    )
                    OutlinedTextField(
                        value = age,
                        onValueChange = {
                            if (it.isDigitsOnly()) {
                                newCactus.age = it
                                age = it
                            }
                        },
                        label = { Text("Current Age:") },
                        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number)
                    )

                }
            }

        }


    }


    object CactiManager {
        private var CactiList = mutableListOf<Cactus>()
        fun addCactus(newCactus: Cactus) {

            CactiList.add(newCactus.copy())
            println(CactiList.toString())
        }

        fun removeCactus(cactus: Cactus) {
            CactiList.remove(cactus)
        }

        fun getCacti(): List<Cactus> {
            return CactiList.toList()
        }
    }

    data class Cactus(var name: String, var species: String, var height: String, var age: String)

}

