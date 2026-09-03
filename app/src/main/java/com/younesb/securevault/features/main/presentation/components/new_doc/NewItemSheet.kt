package com.younesb.securevault.features.main.presentation.components.new_doc

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ExperimentalGridApi
import androidx.compose.foundation.layout.Grid
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.text.TextAutoSize
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.unit.dp
import com.materialkolor.ktx.harmonize
import com.younesb.securevault.R
import com.younesb.securevault.features.main.presentation.components.TitleText
import com.younesb.securevault.features.main.presentation.util.NewItemType

@OptIn(ExperimentalGridApi::class)
@Composable
fun NewItemSheet(
    modifier: Modifier = Modifier,
    onClick: (NewItemType) -> Unit
) {
    Surface(
        modifier = modifier,
        shape = MaterialTheme.shapes.extraLarge,
        color = MaterialTheme.colorScheme.surfaceContainerLow,
        shadowElevation = 4.dp
    ) {
        Column(
            Modifier.fillMaxWidth().padding(12.dp),
            verticalArrangement = Arrangement.spacedBy(6.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            TitleText(
                text = stringResource(id = R.string.new_item),
                modifier = Modifier.padding(16.dp),
            )
            Grid(
                modifier = Modifier.fillMaxWidth(),
                config = {
                    repeat(3) {
                        column(1/3f)
                    }
                    gap(4.dp)
                }
            ) {
                NewItemType.entries.forEachIndexed { index, action ->
                    Surface(
                        modifier = Modifier.fillMaxWidth(),
                        color = MaterialTheme.colorScheme.surfaceContainerHigh,
//                        shape = expressiveGridItemShape(
//                            col = index % 3,
//                            row = index / 3,
//                            colsCount = 3,
//                            rowsCount = (NewItemType.entries.size + 2) / 3
//                        ),
                        shape = MaterialTheme.shapes.extraLarge,
                        onClick = {
                            onClick(action)
                        },
                    ) {
                        Column(
                            horizontalAlignment = Alignment.CenterHorizontally,
                            verticalArrangement = Arrangement.spacedBy(8.dp),
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(8.dp, 16.dp)
                        ) {
                            Icon(
                                imageVector = ImageVector.vectorResource(action.icon),
                                contentDescription = null,
                                modifier = Modifier.size(32.dp),
                                tint = action.color.harmonize(MaterialTheme.colorScheme.primary)
                            )
                            Text(
                                text = stringResource(action.textRes),
                                softWrap = false,
                                autoSize = TextAutoSize.StepBased(
                                    maxFontSize = MaterialTheme.typography.bodyLarge.fontSize
                                )
                            )
                        }
                    }
                }
            }
        }
    }
}