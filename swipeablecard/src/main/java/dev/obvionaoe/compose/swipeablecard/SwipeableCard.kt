package dev.obvionaoe.compose.swipeablecard

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.gestures.Orientation
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxScope
import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.foundation.layout.IntrinsicSize
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.FractionalThreshold
import androidx.compose.material.SwipeableState
import androidx.compose.material.rememberSwipeableState
import androidx.compose.material.swipeable
import androidx.compose.material3.Card
import androidx.compose.material3.CardColors
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CardElevation
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.dp
import dev.obvionaoe.compose.swipeablecard.Util.isNotNull
import dev.obvionaoe.compose.swipeablecard.Util.isNull
import kotlinx.coroutines.launch
import kotlin.math.roundToInt

private enum class SwipeableType {
    Start,
    End,
    Both;

    fun anchors(sizePx: Float): Map<Float, Int> = when (this) {
        Start -> mapOf(0f to 0, sizePx to 1)
        End -> mapOf(0f to 0, -sizePx to 1)
        Both -> mapOf(0f to 0, -sizePx to 1, sizePx to -1)
    }
}

private object Util {
    fun Any?.isNull() = this == null
    fun Any?.isNotNull() = !this.isNull()
}

@OptIn(
    ExperimentalMaterialApi::class,
    ExperimentalMaterial3Api::class
)
@Composable
fun SwipeableCard(
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    enabled: Boolean = true,
    shape: Shape = RoundedCornerShape(16.dp),
    colors: CardColors = CardDefaults.cardColors(),
    elevation: CardElevation = CardDefaults.cardElevation(),
    border: BorderStroke? = null,
    interactionSource: MutableInteractionSource = remember { MutableInteractionSource() },
    swipeableState: SwipeableState<Int> = rememberSwipeableState(0),
    startAction: @Composable (BoxScope.() -> Unit)? = null,
    endAction: @Composable (BoxScope.() -> Unit)? = null,
    content: @Composable ColumnScope.() -> Unit
) {
    val sizePx = with(LocalDensity.current) { dimensionResource(R.dimen.swipeable_size).toPx() }
    val coroutineScope = rememberCoroutineScope()
    val type = when {
        startAction.isNotNull() && endAction.isNull() -> SwipeableType.Start
        startAction.isNull() && endAction.isNotNull() -> SwipeableType.End
        startAction.isNotNull() && endAction.isNotNull() -> SwipeableType.Both
        else -> null
    }
    Box(
        modifier = Modifier
            .let {
                when (type) {
                    null -> it
                    else -> it.swipeable(
                        state = swipeableState,
                        anchors = type.anchors(sizePx),
                        thresholds = { _, _ ->
                            FractionalThreshold(
                                fraction = 0.3f
                            )
                        },
                        orientation = Orientation.Horizontal
                    )
                }
            }
            .height(IntrinsicSize.Max)
            .background(
                color = MaterialTheme.colorScheme.inverseOnSurface,
                shape = shape
            )
    ) {
        startAction?.let {
            Box(
                modifier = Modifier
                    .align(Alignment.CenterStart),
                content = it
            )
        }
        endAction?.let {
            Box(
                modifier = Modifier
                    .align(Alignment.CenterEnd),
                content = it
            )
        }
        Card(
            onClick = {
                coroutineScope.launch {
                    swipeableState.animateTo(0)
                }
                onClick()
            },
            modifier = modifier
                .fillMaxHeight()
                .offset {
                    IntOffset(swipeableState.offset.value.roundToInt(), 0)
                },
            enabled = enabled,
            shape = shape,
            colors = colors,
            elevation = elevation,
            border = border,
            interactionSource = interactionSource,
            content = content
        )
    }
}
