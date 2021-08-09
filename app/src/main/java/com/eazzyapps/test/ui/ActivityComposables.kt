package com.eazzyapps.test.ui

import androidx.compose.material.Icon
import androidx.compose.material.IconButton
import androidx.compose.material.Text
import androidx.compose.material.TopAppBar
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Menu
import androidx.compose.runtime.*
import androidx.navigation.NavController

@Composable
fun AppTopBar(controller: NavController) {

    var title by remember { mutableStateOf("") }

    var canGoBack by remember { mutableStateOf(false) }

    controller.addOnDestinationChangedListener { c, d, _ ->
        canGoBack = c.previousBackStackEntry != null
        title = d.arguments["label"]?.defaultValue as String
    }

    TopAppBar(
        title = { Text(text = title) },
        navigationIcon = {
            if (canGoBack) {
                IconButton(onClick = { controller.popBackStack() }) {
                    Icon(
                        imageVector = Icons.Filled.ArrowBack,
                        contentDescription = "Back",
                    )
                }
            } else {
                IconButton(onClick = {}) {
                    Icon(
                        imageVector = Icons.Filled.Menu,
                        contentDescription = "Menu",
                    )
                }
            }
        }
    )
}