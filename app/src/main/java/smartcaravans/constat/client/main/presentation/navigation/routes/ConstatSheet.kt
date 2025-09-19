package smartcaravans.constat.client.main.presentation.navigation.routes

import android.widget.Toast
import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.animation.slideInHorizontally
import androidx.compose.animation.slideOutHorizontally
import androidx.compose.animation.togetherWith
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.interaction.collectIsPressedAsState
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Nfc
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.ButtonGroupDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExperimentalMaterial3ExpressiveApi
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.Text
import androidx.compose.material3.ToggleButton
import androidx.compose.material3.ToggleButtonDefaults
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.scale
import androidx.compose.ui.geometry.center
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.lightspark.composeqr.DotShape
import com.lightspark.composeqr.QrCodeColors
import com.lightspark.composeqr.QrCodeView
import smartcaravans.constat.client.R
import smartcaravans.constat.client.core.presentation.QrCodeCameraView
import smartcaravans.constat.client.main.presentation.MainActivity
import smartcaravans.constat.client.main.presentation.viewmodel.ConstatSheetState
import smartcaravans.constat.client.settings.presentation.util.findActivity
import younesbouhouche.musicplayer.core.presentation.util.ExpressiveButton

@OptIn(ExperimentalMaterial3Api::class, ExperimentalMaterial3ExpressiveApi::class)
@Composable
fun ConstatSheet(
    qrCodeText: String,
    sheetState: ConstatSheetState?,
    owner: Boolean,
    modifier: Modifier = Modifier,
    onSheetStateChange: (ConstatSheetState?) -> Unit,
    onSkip: () -> Unit = {},
    onScanned: (String) -> Unit = { _ -> }
) {
    val context = LocalContext.current
    LaunchedEffect(sheetState) {
        (context.findActivity() as? MainActivity)?.let { activity ->
            if (sheetState == ConstatSheetState.NFC) {
                if (owner)
                    activity.shareTextWithNfc(qrCodeText)
                else
                    activity.readTextWithNfc()
            }
        }
    }
    if (sheetState != null) {
        ModalBottomSheet(
            {
                onSheetStateChange(null)
            },
            modifier,
            sheetState = rememberModalBottomSheetState(true),
            containerColor = MaterialTheme.colorScheme.surfaceContainer
        ) {
            Column(
                Modifier
                    .fillMaxWidth()
                    .padding(24.dp),
                verticalArrangement = Arrangement.spacedBy(16.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Row(
                    Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    ConstatSheetState.entries.forEachIndexed { index, state ->
                        val interactionSource = remember { MutableInteractionSource() }
                        val pressed by interactionSource.collectIsPressedAsState()
                        val weight by animateFloatAsState(if (pressed) 1.5f else 1f)
                        ToggleButton(
                            checked = sheetState == state,
                            onCheckedChange = {
                                onSheetStateChange(state)
                            },
                            modifier = Modifier
                                .weight(weight)
                                .height(ButtonDefaults.MediumContainerHeight),
                            shapes =
                                if (index == 0) ButtonGroupDefaults.connectedLeadingButtonShapes()
                                else ButtonGroupDefaults.connectedTrailingButtonShapes(),
                            contentPadding = ButtonDefaults.contentPaddingFor(ButtonDefaults.MediumContainerHeight),
                            interactionSource = interactionSource,
                            colors = ToggleButtonDefaults.toggleButtonColors(
                                containerColor = MaterialTheme.colorScheme.surfaceContainerLow,
                                checkedContainerColor = MaterialTheme.colorScheme.primary,
                                checkedContentColor = MaterialTheme.colorScheme.onPrimary,
                                contentColor = MaterialTheme.colorScheme.onSurfaceVariant
                            )
                        ) {
                            Icon(
                                state.icon,
                                contentDescription = null,
                                Modifier.size(ButtonDefaults.MediumIconSize)
                            )
                            Spacer(Modifier.width(ButtonDefaults.MediumIconSpacing))
                            Text(
                                stringResource(state.buttonText)
                            )
                        }
                    }
                }
                Box(
                    Modifier
                        .padding(top = 40.dp, bottom = 30.dp)
                        .clip(MaterialTheme.shapes.extraLarge)
                        .background(MaterialTheme.colorScheme.surfaceContainerLow)
                        .size(280.dp),
                    contentAlignment = Alignment.Center
                ) {
                    Box(
                        Modifier.fillMaxSize(),
                        contentAlignment = Alignment.Center
                    ) {
                        AnimatedContent(
                            sheetState,
                            Modifier.fillMaxSize(),
                            contentAlignment = Alignment.Center
                        ) {
                            when(it) {
                                ConstatSheetState.NFC ->
                                    NfcAnimatedIcon(Modifier.size(240.dp))
                                ConstatSheetState.QR_CODE -> {
                                    if (owner)
                                        Box(Modifier.fillMaxSize().padding(20.dp)) {
                                            QrCodeView(
                                                qrCodeText,
                                                Modifier.fillMaxSize(),
                                                colors = QrCodeColors(
                                                    background = MaterialTheme.colorScheme.surfaceContainerLow,
                                                    foreground = MaterialTheme.colorScheme.primary
                                                ),
                                                dotShape = DotShape.Circle
                                            )
                                        }
                                    else
                                        QrCodeCameraView(
                                            Modifier.fillMaxSize(),
                                            onQrCodeDetected = onScanned
                                        )
                                }
                            }
                        }
                    }
                }
                AnimatedContent(
                    sheetState,
                    transitionSpec = {
                        if (initialState.ordinal < targetState.ordinal) {
                            slideInHorizontally { it } togetherWith
                                    slideOutHorizontally { -it }
                        } else {
                            slideInHorizontally { -it } togetherWith
                                    slideOutHorizontally { it }
                        }
                    }
                ) {
                    Column(Modifier.fillMaxWidth().padding(horizontal = 16.dp),
                        verticalArrangement = Arrangement.spacedBy(16.dp)) {
                        Text(
                            stringResource(it.titleText),
                            style = MaterialTheme.typography.headlineLarge,
                            modifier = Modifier.fillMaxWidth(),
                            fontWeight = FontWeight.Medium,
                            textAlign = TextAlign.Center
                        )
                        Text(
                            stringResource(it.descriptionText),
                            style = MaterialTheme.typography.bodyMedium,
                            color = MaterialTheme.colorScheme.onSurfaceVariant,
                            modifier = Modifier.fillMaxWidth(),
                            textAlign = TextAlign.Center
                        )
                    }
                }
                ExpressiveButton(
                    stringResource(R.string.skip_for_now),
                    size = ButtonDefaults.MediumContainerHeight,
                    onClick = onSkip,
                    outlined = true,
                    modifier = Modifier.padding(top = 16.dp)
                )
            }
        }
    }
}

@Composable
fun NfcAnimatedIcon(
    modifier: Modifier = Modifier
) {
    val transition = rememberInfiniteTransition(label = "nfc_animation")
    val color = MaterialTheme.colorScheme.primary

    // Animate the scale of the icon
    val iconScale by transition.animateFloat(
        initialValue = 0.8f,
        targetValue = 1.2f,
        animationSpec = infiniteRepeatable(
            animation = tween(2000),
            repeatMode = RepeatMode.Reverse
        ),
        label = "icon_scale"
    )

    // Animate the scale of the first circle
    val circle1Scale by transition.animateFloat(
        initialValue = 1f,
        targetValue = 1.5f,
        animationSpec = infiniteRepeatable(
            animation = tween(3000),
            repeatMode = RepeatMode.Reverse
        ),
        label = "circle1_scale"
    )

    // Animate the scale of the second circle with offset
    val circle2Scale by transition.animateFloat(
        initialValue = 1.2f,
        targetValue = 1.8f,
        animationSpec = infiniteRepeatable(
            animation = tween(3500),
            repeatMode = RepeatMode.Reverse
        ),
        label = "circle2_scale"
    )

    Box(
        modifier = modifier,
        contentAlignment = Alignment.Center
    ) {
        // Background circles
        Canvas(
            modifier = Modifier.fillMaxSize()
        ) {
            val center = size.center
            val baseRadius = size.minDimension / 6

            // First animated circle
            drawCircle(
                color = color.copy(alpha = 0.2f),
                radius = baseRadius * circle1Scale,
                center = center,
                style = Stroke(width = 4.dp.toPx())
            )

            // Second animated circle
            drawCircle(
                color = color.copy(alpha = 0.1f),
                radius = baseRadius * circle2Scale,
                center = center,
                style = Stroke(width = 2.dp.toPx())
            )
        }

        // NFC Icon in the center
        Icon(
            imageVector = Icons.Default.Nfc,
            contentDescription = "NFC",
            modifier = Modifier
                .size(48.dp)
                .scale(iconScale),
            tint = MaterialTheme.colorScheme.primary
        )
    }
}
