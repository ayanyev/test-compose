package com.eazzyapps.test

import android.os.Bundle
import androidx.activity.compose.setContent
import androidx.appcompat.app.AppCompatActivity
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Menu
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.lifecycle.lifecycleScope
import androidx.navigation.compose.rememberNavController
import com.eazzyapps.test.common.ActivityDelegate
import com.eazzyapps.test.navigation.AppNavHost
import com.eazzyapps.test.navigation.Screen
import com.eazzyapps.test.navigation.navigate
import com.eazzyapps.test.ui.composables.AppTopBar
import com.eazzyapps.test.ui.composables.ProgressIndicator
import com.eazzyapps.test.ui.theme.AppTheme
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch
import org.koin.android.ext.android.inject

class MainActivity : AppCompatActivity() {

    private val delegate by inject<ActivityDelegate>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {

            val isLoading by delegate.loadingFlow.collectAsState()

            val controller = rememberNavController()

            val scaffoldState = rememberScaffoldState()

            val scope = remember { lifecycleScope }

            AppTheme {
                Surface {
                    Box(modifier = Modifier.fillMaxSize()) {
                        Scaffold(
                            topBar = { AppTopBar(controller = controller) },
                            scaffoldState = scaffoldState,
                            content = { AppNavHost(controller) }
                        )
                        ProgressIndicator(isLoading)
                    }
                }
            }

            scope.launchWhenResumed {
                delegate.msgFlow.collect {
                    launch {
                        // launch in another coroutine
                        // to avoid waiting showSnackbar returning result
                        scaffoldState.snackbarHostState.apply {
                            // dismiss current snackbar first. if any
                            currentSnackbarData?.dismiss()
                            showSnackbar(it.getText(this@MainActivity))
                        }
                    }
                }
            }

            scope.launchWhenResumed {
                delegate.navFlow.collect {
                    // dismiss current snackbar before navigate
                    scaffoldState.snackbarHostState.currentSnackbarData?.dismiss()
                    if (it is Screen.Previous) controller.navigateUp()
                    else controller.navigate(route = it.route, parcelableArgs = it.args) {
                        if (it.popBackStack) {
                            controller.popBackStack()
                        }
                    }
                }
            }

        }
    }
}