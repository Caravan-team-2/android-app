package younesbouhouche.musicplayer.core.presentation.util

import androidx.compose.animation.AnimatedContent
import androidx.compose.foundation.interaction.InteractionSource
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.size
import androidx.compose.material3.ExperimentalMaterial3ExpressiveApi
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.IconButtonColors
import androidx.compose.material3.IconButtonDefaults
import androidx.compose.material3.LoadingIndicator
import androidx.compose.material3.OutlinedIconButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.unit.Dp

@OptIn(ExperimentalMaterial3ExpressiveApi::class)
@Composable
fun ExpressiveIconButton(
    icon: ImageVector,
    modifier: Modifier = Modifier,
    size: Dp = IconButtonDefaults.extraSmallIconSize,
    widthOption: IconButtonDefaults.IconButtonWidthOption = IconButtonDefaults.IconButtonWidthOption.Uniform,
    outlined: Boolean = false,
    colors: IconButtonColors =
        if (outlined) IconButtonDefaults.outlinedIconButtonColors()
        else IconButtonDefaults.iconButtonColors(),
    loading: Boolean = false,
    enabled: Boolean = true,
    iconRotationAngle: Float = 0f,
    interactionSource: MutableInteractionSource? = null,
    onClick: () -> Unit
) {
    val buttonSize = when(size) {
        IconButtonDefaults.extraSmallIconSize -> IconButtonDefaults.extraSmallContainerSize(widthOption)
        IconButtonDefaults.smallIconSize -> IconButtonDefaults.smallContainerSize(widthOption)
        IconButtonDefaults.mediumIconSize -> IconButtonDefaults.mediumContainerSize(widthOption)
        IconButtonDefaults.largeIconSize -> IconButtonDefaults.largeContainerSize(widthOption)
        else -> IconButtonDefaults.extraLargeContainerSize(widthOption)
    }
    val shape = when(size) {
        IconButtonDefaults.extraSmallIconSize -> IconButtonDefaults.extraSmallRoundShape
        IconButtonDefaults.smallIconSize -> IconButtonDefaults.smallRoundShape
        IconButtonDefaults.mediumIconSize -> IconButtonDefaults.mediumRoundShape
        IconButtonDefaults.largeIconSize -> IconButtonDefaults.largeRoundShape
        else -> IconButtonDefaults.extraLargeRoundShape
    }
    val pressedShape = when(size) {
        IconButtonDefaults.extraSmallIconSize -> IconButtonDefaults.extraSmallPressedShape
        IconButtonDefaults.smallIconSize -> IconButtonDefaults.smallPressedShape
        IconButtonDefaults.mediumIconSize -> IconButtonDefaults.mediumPressedShape
        IconButtonDefaults.largeIconSize -> IconButtonDefaults.largePressedShape
        else -> IconButtonDefaults.extraLargePressedShape
    }
    val content = @Composable {
        AnimatedContent(loading) {
            if (it)
                LoadingIndicator(Modifier.size(size))
            else {
                Icon(
                    icon,
                    contentDescription = null,
                    modifier = Modifier.size(size).rotate(iconRotationAngle),
                )
            }
        }
    }
    if (outlined)
        OutlinedIconButton(
            onClick = onClick,
            modifier = modifier.size(buttonSize),
            enabled = enabled,
            shapes = IconButtonDefaults.shapes(shape, pressedShape),
            colors = colors,
            interactionSource = interactionSource,
            content = content
        )
    else
        IconButton(
            onClick = onClick,
            modifier = modifier.size(buttonSize),
            enabled = enabled,
            shapes = IconButtonDefaults.shapes(shape, pressedShape),
            colors = colors,
            interactionSource = interactionSource,
            content = content,
        )
}