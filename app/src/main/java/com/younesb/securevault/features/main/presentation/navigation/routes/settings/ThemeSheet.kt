package com.younesb.securevault.features.main.presentation.navigation.routes.settings

import android.os.Build
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.expandVertically
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.shrinkVertically
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Switch
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.younesb.securevault.R
import com.younesb.securevault.core.domain.models.preferences.ColorScheme
import com.younesb.securevault.core.domain.models.preferences.Theme
import com.younesb.securevault.core.presentation.theme.ThemeViewModel
import com.younesb.securevault.core.presentation.utils.expressiveListItemShape
import com.younesb.securevault.features.main.presentation.components.OptionsSheetWrapper
import org.koin.compose.viewmodel.koinViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ThemeSheet(
    visible: Boolean,
    modifier: Modifier = Modifier,
    onDismissRequest: () -> Unit = { }
) {
    if (visible) {
        val viewModel = koinViewModel<ThemeViewModel>()
        val theme by viewModel.themeMode.collectAsState()
        val dynamicColors by viewModel.dynamicColors.collectAsState()
        val colorScheme by viewModel.colorScheme.collectAsState()
        var tempDynamicColors by remember {
            mutableStateOf(dynamicColors)
        }
        var tempColorScheme by remember {
            mutableStateOf(colorScheme)
        }
        val dynamicColorsUnavailable by remember {
            derivedStateOf {
                (Build.VERSION.SDK_INT < Build.VERSION_CODES.S) or !tempDynamicColors
            }
        }
        LaunchedEffect(dynamicColors) {
            tempDynamicColors = dynamicColors
        }
        LaunchedEffect(colorScheme) {
            tempColorScheme = colorScheme
        }
        OptionsSheetWrapper(
            options = Theme.entries,
            option = theme,
            label = { stringResource(it.label) },
            onDismissRequest = {
                it?.let {
                    viewModel.setThemeMode(it)
                    viewModel.setDynamicColors(tempDynamicColors)
                    viewModel.setColorScheme(tempColorScheme)
                }
                onDismissRequest()
            },
            modifier = modifier
        ) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
                Surface(
                    shape = expressiveListItemShape(
                        index = 0,
                        count = 1,
                        largeShape = MaterialTheme.shapes.extraLarge,
                        smallShape = MaterialTheme.shapes.medium,
                        selected = tempDynamicColors
                    ),
                    color =
                        if (tempDynamicColors) MaterialTheme.colorScheme.surfaceContainerHigh
                        else MaterialTheme.colorScheme.surfaceContainer,
                    onClick = {
                        tempDynamicColors = !tempDynamicColors
                    },
                    modifier = Modifier.padding(8.dp)
                ) {
                    Row(
                        Modifier
                            .fillMaxWidth()
                            .padding(12.dp),
                        verticalAlignment = Alignment.CenterVertically,
                    ) {
                        Text(
                            text = stringResource(R.string.dynamic_colors),
                            modifier = Modifier
                                .weight(1f)
                                .padding(start = 4.dp)
                        )
                        Switch(
                            checked = tempDynamicColors,
                            onCheckedChange = { tempDynamicColors = it }
                        )
                    }
                }
            }
            AnimatedVisibility(
                visible = dynamicColorsUnavailable,
                enter = expandVertically(expandFrom = Alignment.Top) + fadeIn(),
                exit = shrinkVertically(shrinkTowards = Alignment.Top) + fadeOut()
            ) {
                LazyRow(
                    modifier = Modifier.fillMaxWidth(),
                    contentPadding = PaddingValues(horizontal = 8.dp, vertical = 4.dp)
                ) {
                    items(ColorScheme.entries, { it.name }) { scheme ->
                        ColorPreview(
                            scheme = scheme,
                            selected = tempColorScheme == scheme,
                            onClick = {
                                tempColorScheme = scheme
                            },
                            modifier = Modifier.padding(8.dp)
                        )
                    }
                }
            }
        }
    }
}


@Composable
internal fun ColorPreview(
    scheme: ColorScheme,
    modifier: Modifier = Modifier,
    size: Dp = 100.dp,
    borderColor: Color = MaterialTheme.colorScheme.outline,
    selected: Boolean = false,
    onClick: () -> Unit = {},
) {
    val color by animateColorAsState(
        if (selected) borderColor else Color.Transparent,
    )
    Surface(
        modifier
            .size(size)
            .clip(MaterialTheme.shapes.large)
            .clickable(onClick = onClick),
        shape = MaterialTheme.shapes.large,
        color = MaterialTheme.colorScheme.surfaceContainer,
        border =
            BorderStroke(
                2.dp,
                if (selected) {
                    MaterialTheme.colorScheme.primary
                } else {
                    MaterialTheme.colorScheme.outline
                },
            ),
    ) {
        Box(
            Modifier.fillMaxSize(),
            contentAlignment = Alignment.Center,
        ) {
            Box(
                modifier =
                    Modifier
                        .size(size / 1.5f)
                        .border(3.dp, color, CircleShape)
                        .padding(6.dp),
                contentAlignment = Alignment.Center,
            ) {
                Column(
                    Modifier
                        .fillMaxSize()
                        .clip(CircleShape),
                ) {
                    repeat(2) { i ->
                        Row(
                            Modifier
                                .fillMaxSize()
                                .weight(1f),
                        ) {
                            repeat(2) { j ->
                                Surface(
                                    Modifier
                                        .fillMaxSize()
                                        .weight(1f),
                                    color =
                                        when (i * 2 + j) {
                                            0 -> scheme.lightScheme.primary
                                            1 -> scheme.lightScheme.secondary
                                            2 -> scheme.lightScheme.tertiary
                                            else -> scheme.lightScheme.surface
                                        },
                                ) {
                                }
                            }
                        }
                    }
                }
            }
        }
    }
}
