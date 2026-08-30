package com.younesb.securevault.features.auth.presentation.components

import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.AnimatedContentScope
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.SharedTransitionScope
import androidx.compose.animation.SizeTransform
import androidx.compose.animation.expandVertically
import androidx.compose.animation.shrinkVertically
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.text.TextAutoSize
import androidx.compose.material3.ExperimentalMaterial3ExpressiveApi
import androidx.compose.material3.MaterialShapes
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.toShape
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.younesb.securevault.core.presentation.components.IconContainer
import com.younesb.securevault.core.presentation.theme.TitleFont
import soup.compose.material.motion.animation.materialSharedAxisZ

@OptIn(ExperimentalMaterial3ExpressiveApi::class)
@Composable
context(transitionScope: SharedTransitionScope, animatedVisibilityScope: AnimatedContentScope)
fun SetupScreenWrapper(
    icon: ImageVector,
    title: String,
    modifier: Modifier = Modifier,
    leadingText: String? = null,
    supportingText: String? = null,
    surfaceColor: Color = MaterialTheme.colorScheme.surfaceContainer,
    surfaceVisible: Boolean = true,
    content: @Composable () -> Unit
) {
    Surface(
        modifier = modifier.fillMaxSize(),
        color = Color.Transparent
    ) {
        Column(
            modifier = Modifier.fillMaxSize(),
            verticalArrangement = Arrangement.spacedBy(32.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Box(Modifier.weight(1f).fillMaxSize()) {
                Column(
                    modifier = Modifier
                        .statusBarsPadding()
                        .padding(24.dp, 24.dp)
                        .fillMaxSize(),
                    verticalArrangement = Arrangement.spacedBy(16.dp, Alignment.CenterVertically),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    with(transitionScope) {
                        IconContainer(
                            icon = icon,
                            modifier = Modifier
                                .heightIn(max = 200.dp)
                                .aspectRatio(1f, true)
                                .weight(1f, false)
                                .sharedBounds(
                                    sharedContentState = rememberSharedContentState(key = "onboarding_icon"),
                                    animatedVisibilityScope = animatedVisibilityScope,
                                    resizeMode = SharedTransitionScope.ResizeMode.RemeasureToBounds
                                ),
                            shape = MaterialShapes.PixelCircle.toShape(),
                            backgroundColor = MaterialTheme.colorScheme.surfaceContainerLowest.copy(
                                alpha = 0.5f
                            ),
                            color = MaterialTheme.colorScheme.primary
                        )
                    }
                    leadingText?.let {
                        Text(
                            text = it,
                            style = MaterialTheme.typography.titleLarge,
                            fontWeight = FontWeight.Medium,
                            modifier = Modifier.fillMaxWidth(),
                            textAlign = TextAlign.Center,
                            softWrap = false,
                            color = MaterialTheme.colorScheme.onBackground
                        )
                    }
                    AnimatedContent(
                        targetState = title,
                        transitionSpec = {
                            materialSharedAxisZ(true).using(SizeTransform(clip = false))
                        }
                    ) {
                        Text(
                            text = it,
                            style = MaterialTheme.typography.headlineLarge,
                            fontFamily = TitleFont,
                            softWrap = false,
                            autoSize = TextAutoSize.StepBased(
                                minFontSize = 10.sp,
                                maxFontSize = 100.sp
                            ),
                            modifier = Modifier.fillMaxWidth(),
                            color = MaterialTheme.colorScheme.primary
                        )
                    }
                    supportingText?.let {
                        Text(
                            text = it,
                            style = MaterialTheme.typography.titleLarge,
                            modifier = Modifier.fillMaxWidth(),
                            textAlign = TextAlign.Center,
                            softWrap = false,
                            color = MaterialTheme.colorScheme.onSurface
                        )
                    }
                }
            }
            AnimatedVisibility(
                visible = surfaceVisible,
                enter = expandVertically(expandFrom = Alignment.Bottom),
                exit = shrinkVertically(shrinkTowards = Alignment.Bottom)
            ) {
                with(animatedVisibilityScope) {
                    SetupSurface(content = content, color = surfaceColor.copy(alpha = .5f))
                }
            }
        }
    }
}