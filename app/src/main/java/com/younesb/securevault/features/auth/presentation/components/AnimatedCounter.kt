package com.younesb.securevault.features.auth.presentation.components

import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.AnimatedContentTransitionScope
import androidx.compose.animation.ContentTransform
import androidx.compose.animation.SizeTransform
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.slideOutVertically
import androidx.compose.animation.togetherWith
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier

enum class CounterAnimationDirection {
    Up, Down, Dynamic
}

@Composable
fun AnimatedCounter(
    counter: String,
    digits: Int,
    modifier: Modifier = Modifier,
    direction: CounterAnimationDirection = CounterAnimationDirection.Dynamic,
) {
    val formattedCounter = counter.padStart(digits, '0')
    val transitionSpec: AnimatedContentTransitionScope<Char>.() -> ContentTransform = remember(direction) {
        {
            val up =
                slideInVertically { height -> height } + fadeIn() togetherWith
                        slideOutVertically { height -> -height } + fadeOut()
            val down =
                slideInVertically { height -> -height } + fadeIn() togetherWith
                        slideOutVertically { height -> height } + fadeOut()
            when(direction) {
                CounterAnimationDirection.Up -> up
                CounterAnimationDirection.Down -> down
                CounterAnimationDirection.Dynamic ->
                    if (targetState > initialState) up
                    else down
            }.using(SizeTransform(clip = false))
        }
    }
    Row(
        modifier.fillMaxWidth(),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.Center
    ) {
        repeat(formattedCounter.length) { index ->
            AnimatedContent(
                formattedCounter.getOrElse(index, { '0' }),
                transitionSpec = transitionSpec
            ) {
                Text(text = it.toString())
            }
        }
    }
}