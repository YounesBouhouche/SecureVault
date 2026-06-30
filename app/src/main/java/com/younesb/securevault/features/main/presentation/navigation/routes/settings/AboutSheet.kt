package com.younesb.securevault.features.main.presentation.navigation.routes.settings

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.add
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyListScope
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.TextAutoSize
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Link
import androidx.compose.material.icons.filled.Person
import androidx.compose.material3.BottomSheetDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExperimentalMaterial3ExpressiveApi
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialShapes
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.SheetValue
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.rememberBottomSheetState
import androidx.compose.material3.toShape
import androidx.compose.runtime.Composable
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.younesb.securevault.R
import com.younesb.securevault.core.presentation.components.Image
import com.younesb.securevault.core.presentation.utils.GlobalEvent
import com.younesb.securevault.core.presentation.utils.GlobalEventsBus
import com.younesb.securevault.core.presentation.utils.getAppVersion
import com.younesb.securevault.features.auth.presentation.components.AnimatedAppLogo
import com.younesb.securevault.features.main.presentation.components.TitleText
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class, ExperimentalMaterial3ExpressiveApi::class)
@Composable
fun AboutSheet(
    visible: Boolean,
    onDismissRequest: () -> Unit,
    modifier: Modifier = Modifier,
) {
    val context = LocalContext.current
    if (visible) {
        ModalBottomSheet(
            onDismissRequest = onDismissRequest,
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
                TitleText(stringResource(R.string.about), Modifier.padding(bottom = 12.dp))
                InfoCard(
                    title = stringResource(R.string.app_name),
                    text = stringResource(R.string.app_description),
                    leadingIcon = {
                        AnimatedAppLogo(Modifier.size(120.dp))
                    }
                ) {
                    item {
                        InfoChip(
                            context.getAppVersion(),
                        )
                    }
                    item {
                        LinkChip(
                            text = stringResource(R.string.github),
                            link = "https://github.com/YounesBouhouche/SecureVault",
                        )
                    }
                }
                InfoCard(
                    title = stringResource(R.string.developer_name),
                    text = stringResource(R.string.developer_bio),
                    leadingIcon = {
                        Image(
                            model = null,
                            icon = Icons.Default.Person,
//                            contentDescription = stringResource(R.string.developer_name),
                            modifier = Modifier.size(120.dp),
                            shape = MaterialShapes.Cookie12Sided.toShape(),
                            background = MaterialTheme.colorScheme.secondaryContainer,
                            iconTint = MaterialTheme.colorScheme.secondary,
                        )
                    }
                ) {
                    item {
                        LinkChip(
                            text = stringResource(R.string.github),
                            link = "https://github.com/YounesBouhouche/",
                        )
                    }
                    item {
                        LinkChip(
                            text = stringResource(R.string.portfolio),
                            link = "https://ybcoding.netlify.app/"
                        )
                    }
                    item {
                        LinkChip(
                            text = stringResource(R.string.x_formerly_twitter),
                            link = "twitter://user?screen_name=younesbouh_05",
                            alternative = "https://twitter.com/younesbouh_05"
                        )
                    }
                    item {
                        LinkChip(
                            text = stringResource(R.string.instagram),
                            link = "instagram://user?username=younesb_05",
                            alternative = "https://instagram.com/younesb_05"
                        )
                    }
                }
            }
        }
    }
}

@Composable
internal fun InfoCard(
    title: String,
    text: String,
    leadingIcon: @Composable () -> Unit,
    chips: LazyListScope.() -> Unit = {  }
) {
    Surface(
        color = MaterialTheme.colorScheme.surfaceContainerHigh,
        shape = MaterialTheme.shapes.extraLarge,
    ) {
        Column(
            Modifier
                .fillMaxWidth()
                .padding(vertical = 24.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            Row(
                Modifier.fillMaxWidth().padding(horizontal = 24.dp),
                horizontalArrangement = Arrangement.spacedBy(8.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                leadingIcon()
                Column(
                    Modifier.weight(1f),
                    verticalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    Text(
                        text = title,
                        style = MaterialTheme.typography.headlineLarge,
                        softWrap = false,
                        autoSize = TextAutoSize.StepBased(
                            minFontSize = 10.sp,
                            maxFontSize = 100.sp
                        ),
                        color = MaterialTheme.colorScheme.primary
                    )
                    Text(
                        text = text,
                        style = MaterialTheme.typography.bodyMedium,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }
            }
            LazyRow(
                Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(8.dp),
                contentPadding = PaddingValues(horizontal = 24.dp),
                content = chips
            )
        }
    }
}


@Composable
internal fun InfoChip(
    text: String,
    modifier: Modifier = Modifier,
    icon: ImageVector? = null,
    color: Color = MaterialTheme.colorScheme.tertiaryContainer
) {
    Surface(
        modifier = modifier,
        color = color,
        shape = RoundedCornerShape(100)
    ) {
        Row(
            Modifier.padding(16.dp, 12.dp),
            horizontalArrangement = Arrangement.spacedBy(6.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            icon?.let {
                Icon(it, null, Modifier.size(20.dp))
            }
            Text(text, style = MaterialTheme.typography.labelLarge)
        }
    }
}

@Composable
internal fun LinkChip(
    text: String,
    link: String,
    modifier: Modifier = Modifier,
    alternative: String = link,
    icon: ImageVector? = Icons.Default.Link,
) {
    val scope = rememberCoroutineScope()
    Surface(
        modifier = modifier,
        color = Color.Transparent,
        contentColor = MaterialTheme.colorScheme.primary,
        shape = RoundedCornerShape(100),
        border = BorderStroke(1.dp, MaterialTheme.colorScheme.outline),
        onClick = {
            scope.launch {
                GlobalEventsBus.sendEvent(GlobalEvent.LaunchExternalLink(link, alternative))
            }
        }
    ) {
        Row(
            Modifier.padding(16.dp, 12.dp),
            horizontalArrangement = Arrangement.spacedBy(6.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            icon?.let {
                Icon(it, null, Modifier.size(20.dp))
            }
            Text(text, style = MaterialTheme.typography.labelLarge)
        }
    }
}
