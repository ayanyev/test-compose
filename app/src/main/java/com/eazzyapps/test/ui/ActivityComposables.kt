package com.eazzyapps.test.ui

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Menu
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.navigation.NavController

@Composable
fun ProgressIndicator(isLoading: Boolean) {
    if (!isLoading) return
    Box(
        modifier = Modifier.fillMaxSize()
    ) {
        CircularProgressIndicator(
            modifier = Modifier.align(Alignment.Center)
        )
    }
}

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