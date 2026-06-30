package com.younesb.securevault.features.main.presentation.components

import androidx.activity.compose.PredictiveBackHandler
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.slideOutVertically
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.interaction.collectIsFocusedAsState
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.text.input.clearText
import androidx.compose.foundation.text.input.rememberTextFieldState
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Clear
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExperimentalMaterial3ExpressiveApi
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.runtime.snapshotFlow
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.younesb.mydesignsystem.presentation.components.ExpressiveIconButton
import com.younesb.securevault.R
import kotlinx.coroutines.CancellationException
import kotlinx.coroutines.FlowPreview
import kotlinx.coroutines.flow.debounce
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlin.time.Duration.Companion.milliseconds

@OptIn(ExperimentalMaterial3Api::class, ExperimentalMaterial3ExpressiveApi::class,
    FlowPreview::class
)
@Composable
fun MainSearchBar(
    modifier: Modifier = Modifier,
    visible: Boolean = true,
    onQueryUpdate: (query: String) -> Unit = {}
) {
    val textFieldState = rememberTextFieldState()
    val interactionSource = remember { MutableInteractionSource() }
    val focusManager = LocalFocusManager.current
    val focused by interactionSource.collectIsFocusedAsState()
    var fraction by remember {
        mutableFloatStateOf(.5f)
    }
    val animatedFraction by animateFloatAsState(fraction)
    LaunchedEffect(focused) {
        fraction = if (focused) 1f else .5f
    }
    LaunchedEffect(textFieldState.text) {
        snapshotFlow { textFieldState.text }
            .debounce(300.milliseconds) // Wait for 300ms of inactivity
            .distinctUntilChanged()
            .collect { query ->
                onQueryUpdate(query.toString())
            }
    }
    AnimatedVisibility(
        visible = visible,
        enter = slideInVertically(animationSpec = MaterialTheme.motionScheme.defaultSpatialSpec()) { -it } + fadeIn(
            animationSpec = MaterialTheme.motionScheme.defaultSpatialSpec()
        ),
        exit = slideOutVertically(animationSpec = MaterialTheme.motionScheme.defaultSpatialSpec()) { -it } + fadeOut(
            animationSpec = MaterialTheme.motionScheme.defaultSpatialSpec()
        ),
        modifier = modifier
            .statusBarsPadding()
            .padding(16.dp)
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(12.dp, Alignment.CenterHorizontally)
        ) {
            BasicTextField(
                state = textFieldState,
                modifier = Modifier
                    .fillMaxWidth(animatedFraction)
                    .height(60.dp),
                interactionSource = interactionSource,
                textStyle = MaterialTheme.typography.bodyLarge,
                cursorBrush = SolidColor(MaterialTheme.colorScheme.primary),
                decorator = { field ->
                    Surface(
                        modifier = Modifier.fillMaxSize(),
                        shape = RoundedCornerShape(100),
                        color = MaterialTheme.colorScheme.surfaceContainer
                    ) {
                        Box(
                            Modifier
                                .padding(16.dp)
                                .fillMaxSize(),
                            contentAlignment = Alignment.Center
                        ) {
                            this@Row.AnimatedVisibility(
                                visible = textFieldState.text.isEmpty(),
                                enter = fadeIn(),
                                exit = fadeOut(),
                            ) {
                                Row(
                                    horizontalArrangement = Arrangement.spacedBy(4.dp),
                                    verticalAlignment = Alignment.CenterVertically
                                ) {
                                    Icon(
                                        Icons.Default.Search,
                                        null,
                                        tint = MaterialTheme.colorScheme.onSurfaceVariant
                                    )
                                    Text(
                                        text = stringResource(R.string.search),
                                        color = MaterialTheme.colorScheme.onSurfaceVariant
                                    )
                                }
                            }
                            Row(
                                Modifier.fillMaxSize(),
                                verticalAlignment = Alignment.CenterVertically,
                                horizontalArrangement = Arrangement.SpaceBetween
                            ) {
                                field()
                                AnimatedVisibility(
                                    textFieldState.text.isNotEmpty(),
                                    Modifier.fillMaxHeight(),
                                    enter = fadeIn(),
                                    exit = fadeOut()
                                ) {
                                    ExpressiveIconButton(
                                        Icons.Default.Clear,
                                        size = IconButtonDefaults.mediumIconSize,
                                        widthOption = IconButtonDefaults.IconButtonWidthOption.Wide,
                                    ) {
                                        textFieldState.clearText()
                                    }
                                }
                            }
                        }
                    }
                }
            )
        }
    }
    PredictiveBackHandler(focused) { progress ->
        try {
            progress.collect {
                fraction = (1f - it.progress) / 2f + .5f
            }
            fraction = .5f
            focusManager.clearFocus()
        } catch (_: CancellationException) {
            fraction = 1f
        }
    }
}
