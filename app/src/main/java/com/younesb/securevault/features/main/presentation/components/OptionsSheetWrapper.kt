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
import androidx.compose.material3.SheetValue
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.rememberBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.younesb.securevault.R
import com.younesb.securevault.core.presentation.components.ButtonsRow
import com.younesb.securevault.core.presentation.utils.expressiveListItemShape

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun<T> OptionsSheetWrapper(
    options: List<T>,
    option: T,
    label: @Composable (T) -> String,
    modifier: Modifier = Modifier,
    onDismissRequest: (T?) -> Unit = { },
    trailingContent: @Composable () -> Unit = { }
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
        sheetState = rememberBottomSheetState(
            SheetValue.Expanded,
            enabledValues = setOf(SheetValue.Hidden, SheetValue.Expanded)
        )
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
                        shape = expressiveListItemShape(
                            index = index,
                            count = options.size,
                            largeShape = MaterialTheme.shapes.extraLarge,
                            smallShape = MaterialTheme.shapes.medium,
                            selected = isSelected
                        ),
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
            trailingContent()
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