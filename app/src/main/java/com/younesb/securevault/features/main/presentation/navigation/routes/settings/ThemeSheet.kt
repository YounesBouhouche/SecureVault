package com.younesb.securevault.features.main.presentation.navigation.routes.settings

import android.os.Build
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Switch
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.younesb.securevault.R
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
        var dynamic by remember {
            mutableStateOf(false)
        }
        LaunchedEffect(dynamicColors) {
            dynamic = dynamicColors
        }
        OptionsSheetWrapper(
            options = Theme.entries,
            option = theme,
            label = { stringResource(it.label) },
            onDismissRequest = {
                it?.let {
                    viewModel.setThemeMode(it)
                    viewModel.setDynamicColors(dynamic)
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
                        selected = dynamic
                    ),
                    color =
                        if (dynamic) MaterialTheme.colorScheme.surfaceContainerHigh
                        else MaterialTheme.colorScheme.surfaceContainer
                    ,
                    onClick = {
                        dynamic = !dynamic
                    },
                    modifier = Modifier.padding(8.dp)
                ) {
                    Row(
                        Modifier.fillMaxWidth().padding(12.dp),
                        verticalAlignment = Alignment.CenterVertically,
                    ) {
                        Text(
                            text = stringResource(R.string.dynamic_colors),
                            modifier = Modifier.weight(1f).padding(start = 4.dp)
                        )
                        Switch(
                            checked = dynamic,
                            onCheckedChange = { dynamic = it }
                        )
                    }
                }
            }
        }
    }
}