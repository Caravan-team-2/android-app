package smartcaravans.constat.client.auth.presentation.routes

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.ExperimentalMaterial3ExpressiveApi
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import smartcaravans.constat.client.R
import younesbouhouche.musicplayer.core.presentation.util.ExpressiveButton

@OptIn(ExperimentalMaterial3ExpressiveApi::class)
@Composable
fun IdentityTutorial(modifier: Modifier = Modifier, onClick: () -> Unit) {
    val items = listOf(
        Triple(
            R.drawable.id_step_1,
            R.string.id_step_1_title,
            R.string.id_step_1_text
        ),
        Triple(
            R.drawable.id_step_2,
            R.string.id_step_2_title,
            R.string.id_step_2_text
        ),
        Triple(
            R.drawable.id_step_3,
            R.string.id_step_3_title,
            R.string.id_step_3_text
        )
    )
    Column(modifier.fillMaxSize().navigationBarsPadding().padding(bottom = 24.dp)) {
        Column(
            Modifier.fillMaxWidth().fillMaxHeight(.3f).padding(24.dp),
            verticalArrangement = Arrangement.spacedBy(8.dp, Alignment.Bottom)
        ) {
            Text(
                stringResource(R.string.id_tutorial_title),
                style = MaterialTheme.typography.headlineLarge,
                fontWeight = FontWeight.SemiBold,
                color = MaterialTheme.colorScheme.surface,
                modifier = Modifier.fillMaxWidth(),
                textAlign = TextAlign.Center
            )
            Text(
                stringResource(R.string.id_tutorial_subtitle),
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.surface,
                modifier = Modifier.fillMaxWidth(),
                textAlign = TextAlign.Center
            )
        }
        Column(Modifier.fillMaxSize().weight(1f).padding(24.dp, 48.dp)) {
            Column(
                Modifier.fillMaxSize(),
                verticalArrangement = Arrangement.spacedBy(32.dp, Alignment.Top)
            ) {
                items.forEach {
                    TutorialItem(it.first, it.second, it.third)
                }
            }
        }
        ExpressiveButton(
            text = stringResource(R.string.id_tutorial_button),
            size = ButtonDefaults.MediumContainerHeight,
            onClick = onClick,
            modifier = Modifier.fillMaxWidth().padding(horizontal = 24.dp)
        )
    }
}

@Composable
fun TutorialItem(
    icon: Int,
    title: Int,
    text: Int,
    modifier: Modifier = Modifier
) {
    Row(modifier,
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        Image(
            painter = painterResource(id = icon),
            contentDescription = null,
            modifier = Modifier.size(40.dp)
        )
        Column(
            Modifier.fillMaxWidth().weight(1f),
            verticalArrangement = Arrangement.spacedBy(4.dp, Alignment.Top)
        ) {
             Text(
                 stringResource(id = title),
                 style = MaterialTheme.typography.titleMedium,
                 fontWeight = FontWeight.SemiBold,
                 color = MaterialTheme.colorScheme.primary
             )
             Text(
                 stringResource(id = text),
                 style = MaterialTheme.typography.bodyMedium,
                 color = MaterialTheme.colorScheme.onSurfaceVariant,
             )
        }
    }
}