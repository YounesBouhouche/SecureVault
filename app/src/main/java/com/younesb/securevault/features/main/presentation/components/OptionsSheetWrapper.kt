package com.younesb.securevault.features.main.presentation.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.add
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.BottomSheetDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.RadioButton
import androidx.compose.material3.Surface
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
import com.younesb.securevault.core.presentation.components.ButtonsRow
import com.younesb.securevault.core.presentation.theme.ThemeViewModel
import com.younesb.securevault.core.presentation.utils.expressiveListItemShape
import org.koin.compose.viewmodel.koinViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun<T> OptionsSheetWrapper(
    options: List<T>,
    option: T,
    label: @Composable (T) -> String,
    modifier: Modifier = Modifier,
    onDismissRequest: (T?) -> Unit = { }
) {
    var selectedOption by remember {
        mutableStateOf(option)
    }
    LaunchedEffect(option) {
        selectedOption = option
    }
    ModalBottomSheet(
        onDismissRequest = {
            onDismissRequest(null)
        },
        modifier = modifier,
        contentWindowInsets = {
            BottomSheetDefaults.modalWindowInsets.add(WindowInsets(bottom = 16.dp))
        },
    ) {
        Column(
            modifier =
                Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 8.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(8.dp),
        ) {
            TitleText(
                stringResource(R.string.choose_theme),
                Modifier.padding(bottom = 12.dp),
            )
            Column(
                modifier =
                    Modifier
                        .fillMaxWidth()
                        .padding(bottom = 8.dp)
                        .padding(horizontal = 8.dp),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.spacedBy(4.dp)
            ) {
                options.forEachIndexed { index, entry ->
                    val isSelected = selectedOption == entry
                    Surface(
                        shape = expressiveListItemShape(index, options.size),
                        color =
                            if (isSelected) MaterialTheme.colorScheme.surfaceContainerHigh
                            else MaterialTheme.colorScheme.surfaceContainer
                        ,
                        onClick = {
                            selectedOption = entry
                        }
                    ) {
                        Row(
                            Modifier
                                .fillMaxWidth().padding(12.dp),
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.spacedBy(8.dp)
                        ) {
                            RadioButton(
                                selected = isSelected,
                                onClick = { selectedOption = entry }
                            )
                            Text(
                                text = label(entry),
                                modifier = Modifier.padding(start = 8.dp)
                            )
                        }
                    }
                }
            }
            ButtonsRow(
                count = 2,
                onClick = { index ->
                    onDismissRequest(
                        selectedOption.takeIf { index == 1 }
                    )
                },
                text = { index ->
                    when (index) {
                        0 -> stringResource(R.string.cancel)
                        1 -> stringResource(R.string.ok)
                        else -> ""
                    }
                },
                icon = { null },
                outlined = { index -> index == 0 },
                modifier = Modifier.fillMaxWidth()
            )
        }
    }
}