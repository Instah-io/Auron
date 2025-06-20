@file:Suppress("DEPRECATION")

package com.example

import AppManager
import android.content.Context
import android.graphics.Bitmap
import android.graphics.Canvas
import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.togetherWith
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.*
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.Recomposer
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.platform.ComposeView
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.createLifecycleAwareWindowRecomposer
import androidx.compose.ui.unit.dp
import androidx.core.graphics.createBitmap
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleObserver
import io.instah.auron.appSdk.auronApp
import io.instah.auron.sdk.App
import io.instah.auron.sdk.compose.rememberSaveable
import io.instah.auron.sdk.interaction.onResume
import io.instah.auron.sdk.ui.Center
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlin.coroutines.CoroutineContext

@OptIn(ExperimentalComposeUiApi::class)
fun createRender(context: Context): Bitmap {
    val bitmap = createBitmap(1000, 1000)
    val canvas = Canvas(bitmap)
    val view = ComposeView(context)
    view.setContent {
        Center {
            Button(
                onClick = {}
            ) {
                Text("Example!")
            }
        }
    }

    val windowRecomposer = ComposeView::class.java.getDeclaredField("windowRecomposer")
    windowRecomposer.isAccessible = true
    windowRecomposer.set(view, Recomposer(Dispatchers.Default))

    view.measure(1000, 1000)
    view.layout(0, 0, 1000, 1000)

    return bitmap
}

fun main() = auronApp("Example") {
    val coroutineScope = rememberCoroutineScope()
    val context = LocalContext.current
    //val render by rememberSaveable { mutableStateOf(createRender(context)) }

    DisposableEffect(Unit) {
        onDispose {
            println("hi!")
        }
    }

    onResume {
        println("On resume working!")
    }

    MaterialTheme(
        colorScheme = darkColorScheme()
    ) {
        Surface(Modifier.fillMaxSize()) {
            Center {
                AnimatedContent(
                    targetState = AppManager.state > 0,
                    transitionSpec = { fadeIn() togetherWith fadeOut() }
                ) { state ->
                    if (state) {
                        Center {
                            Column(
                                horizontalAlignment = Alignment.CenterHorizontally,
                                verticalArrangement = Arrangement.spacedBy(20.dp)
                            ) {
                                Text("Permission granted")

                                Button(
                                    onClick = { App.quit() }
                                ) {
                                    Text(ExampleTranslationScheme.exampleTranslationSchemeHolder.quit)
                                }

                                if (AppManager.state != 2) {
                                    Button(
                                        onClick = {
                                            coroutineScope.launch {
                                                AppManager.escalateGrantPermission()
                                            }
                                        }
                                    ) {
                                        Text("Escalate")
                                    }
                                }
                            }
                        }
                    } else {
                        Button(
                            onClick = {
                                coroutineScope.launch { AppManager.attemptGrantPermission() }
                            }
                        ) {
                            Text("Grant Permission")
                        }
                    }
                }
            }
        }

        /*Center {
            Image(
                bitmap = render.asImageBitmap(),
                contentDescription = "Renderować"
            )
        }*/
    }
}