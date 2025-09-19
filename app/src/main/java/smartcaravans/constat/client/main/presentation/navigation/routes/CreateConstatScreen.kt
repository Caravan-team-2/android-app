package smartcaravans.constat.client.main.presentation.navigation.routes

import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.slideInHorizontally
import androidx.compose.animation.slideOutHorizontally
import androidx.compose.animation.togetherWith
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
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Check
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.ExperimentalMaterial3ExpressiveApi
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.geometry.CornerRadius
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import smartcaravans.constat.client.R
import smartcaravans.constat.client.UserInsurrancesQuery
import smartcaravans.constat.client.main.presentation.navigation.routes.forms.InsuranceInfoFormScreen
import smartcaravans.constat.client.main.presentation.navigation.routes.forms.PersonalInfoFormScreen
import smartcaravans.constat.client.main.presentation.navigation.routes.forms.PictureFormScreen
import smartcaravans.constat.client.main.presentation.navigation.routes.forms.VehicleFormScreen
import smartcaravans.constat.client.main.presentation.viewmodel.ConstatFormState
import smartcaravans.constat.client.main.presentation.viewmodel.FormStep
import smartcaravans.constat.client.main.presentation.viewmodel.MainUiAction
import smartcaravans.constat.client.ui.theme.AppTheme
import younesbouhouche.musicplayer.core.presentation.util.ExpressiveButton
import younesbouhouche.musicplayer.core.presentation.util.ExpressiveIconButton

@OptIn(ExperimentalMaterial3ExpressiveApi::class)
@Composable
fun CreateConstatScreen(
    formState: ConstatFormState,
    modifier: Modifier = Modifier,
    vehicles: List<UserInsurrancesQuery.Vehicle> = emptyList(),
    onClose: () -> Unit = {},
    onNext: () -> Unit = {},
    onPrev: () -> Unit = {},
    onStateChange: (ConstatFormState) -> Unit = {},
) {
    Column(modifier.fillMaxSize().statusBarsPadding()) {
        Row(Modifier
            .padding(24.dp)
            .fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            ExpressiveIconButton(
                icon = Icons.AutoMirrored.Filled.ArrowBack,
                size = IconButtonDefaults.mediumIconSize,
                colors = IconButtonDefaults.filledTonalIconButtonColors(),
                onClick = onClose
            )
            Text(
                stringResource(R.string.create_constat),
                style = MaterialTheme.typography.headlineSmall,
                fontWeight = FontWeight.SemiBold,
                color = MaterialTheme.colorScheme.onSurface
            )
        }
        StepsHeader(formState.step)
        AnimatedContent(
            formState.step,
            Modifier.weight(1f).padding(horizontal = 16.dp),
            transitionSpec = {
                if (initialState.ordinal < targetState.ordinal) {
                    slideInHorizontally { it } togetherWith
                            slideOutHorizontally { -it }
                } else {
                    slideInHorizontally { -it } togetherWith
                            slideOutHorizontally { it }
                }
            },
        ) { step ->
            when(step) {
                FormStep.PersonalInfo ->
                    PersonalInfoFormScreen(
                        formState.personalInfoForm,
                        {
                            onStateChange(formState.copy(personalInfoForm = it))
                        },
                        Modifier.weight(1f)
                    )
                FormStep.VehicleInfo ->
                    VehicleFormScreen(
                        formState.vehicleInfo,
                        vehicles,
                        {
                            onStateChange(formState.copy(vehicleInfo = it))
                        },
                        Modifier.weight(1f)
                    )
                FormStep.InsuranceInfo ->
                    InsuranceInfoFormScreen(
                        formState.insuranceFormState,
                        {
                            onStateChange(formState.copy(insuranceFormState = it))
                        },
                        Modifier.weight(1f)
                    )
                FormStep.Pictures ->
                    PictureFormScreen(
                        formState.pictureFormState,
                        {
                            onStateChange(formState.copy(pictureFormState = it))
                        },
                        Modifier.weight(1f)
                    )
            }
        }

        Row(
            Modifier
                .background(MaterialTheme.colorScheme.background)
                .fillMaxWidth().padding(16.dp).navigationBarsPadding(),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            repeat(2) { index ->
                val interactionSource = remember { MutableInteractionSource() }
                val pressed by interactionSource.collectIsPressedAsState()
                val weight by animateFloatAsState(
                    if (pressed) 1.5f else 1f
                )
                ExpressiveButton(
                    text =
                        if (index == 0) stringResource(R.string.previous)
                        else stringResource(R.string.next),
                    size = ButtonDefaults.MediumContainerHeight,
                    modifier = Modifier
                        .fillMaxWidth()
                        .weight(weight),
                    outlined = index == 0,
                    interactionSource = interactionSource,
                    onClick = if (index == 0) onPrev else onNext
                )
            }
        }
    }
}

@Composable
fun StepsHeader(step: FormStep, modifier: Modifier = Modifier) {
    val primary = MaterialTheme.colorScheme.primary
    val outline = MaterialTheme.colorScheme.surfaceVariant
    Box(
        modifier
            .padding(16.dp, 8.dp, 16.dp, 32.dp)
            .fillMaxWidth()
            .height(80.dp)
            .drawBehind {
                val trackHeight = 4.dp.toPx()
                val horizontalPadding = 40.dp.toPx()
                val trackWidth = size.width - (horizontalPadding * 2)
                val trackY = (size.height / 2) - 16.dp.toPx()
                val progressPercentage = step.ordinal.toFloat() / (FormStep.entries.size - 1).toFloat()
                val activeWidth = trackWidth * progressPercentage

                // Draw inactive track (full width)
                drawRoundRect(
                    color = outline,
                    topLeft = Offset(horizontalPadding, trackY - trackHeight / 2),
                    size = Size(trackWidth, trackHeight),
                    cornerRadius = CornerRadius(trackHeight / 2)
                )

                // Draw active track (progress)
                drawRoundRect(
                    color = primary,
                    topLeft = Offset(horizontalPadding, trackY - trackHeight / 2),
                    size = Size(activeWidth, trackHeight),
                    cornerRadius = CornerRadius(trackHeight / 2)
                )
            }
    ) {
        // Step indicators positioned over the custom track
        Row(
            Modifier
                .fillMaxWidth()
                .align(Alignment.Center),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            FormStep.entries.forEach { formStep ->
                StepIndicator(
                    step = formStep,
                    isCompleted = formStep.ordinal < step.ordinal,
                    isCurrent = step == formStep,
                    modifier = Modifier.weight(1f)
                )
            }
        }
    }
}

@Composable
private fun StepIndicator(
    step: FormStep,
    isCompleted: Boolean,
    isCurrent: Boolean,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier,
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        // Circle with check mark or step number
        Box(
            modifier = Modifier
                .size(40.dp)
                .clip(CircleShape)
                .background(
                    when {
                        isCompleted -> MaterialTheme.colorScheme.primary
                        isCurrent -> MaterialTheme.colorScheme.primary
                        else -> MaterialTheme.colorScheme.surfaceVariant
                    }
                ),
            contentAlignment = Alignment.Center
        ) {
            if (isCompleted) {
                Icon(
                    imageVector = Icons.Default.Check,
                    contentDescription = "Completed",
                    tint = MaterialTheme.colorScheme.onPrimary,
                    modifier = Modifier.size(20.dp)
                )
            } else {
                Text(
                    text = (step.ordinal + 1).toString(),
                    color = if (isCurrent)
                        MaterialTheme.colorScheme.onPrimary
                    else
                        MaterialTheme.colorScheme.onSurfaceVariant,
                    fontSize = 14.sp,
                    fontWeight = FontWeight.Medium
                )
            }
        }

        Spacer(modifier = Modifier.height(8.dp))

        // Step label
        Text(
            text = stringResource(step.titleRes),
            fontSize = 12.sp,
            textAlign = TextAlign.Center,
            color = if (isCurrent || isCompleted)
                MaterialTheme.colorScheme.onSurface
            else
                MaterialTheme.colorScheme.onSurfaceVariant,
            fontWeight = if (isCurrent) FontWeight.Medium else FontWeight.Normal
        )
    }
}

@Preview
@Composable
private fun CreateConstatScreenPreview() {
    AppTheme {
        CreateConstatScreen(ConstatFormState(step = FormStep.VehicleInfo))
    }
}