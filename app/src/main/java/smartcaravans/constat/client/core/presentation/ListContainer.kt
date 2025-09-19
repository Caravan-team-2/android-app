package smartcaravans.constat.client.core.presentation

import androidx.compose.animation.AnimatedContent
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AllInbox
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import smartcaravans.constat.client.R

@Composable
fun<T> ListContainer(
    items: List<T>,
    modifier: Modifier = Modifier,
    shape: Shape = MaterialTheme.shapes.large,
    itemContent: @Composable (item: T) -> Unit
) {
    if (items.isEmpty())
        Column(
            modifier.fillMaxWidth(),
            verticalArrangement = Arrangement.spacedBy(16.dp, Alignment.CenterVertically),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            IconContainer(Icons.Default.AllInbox, Modifier.size(100.dp))
            Text(
                stringResource(R.string.no_items_found),
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
        }
    else
        ShadowContainer(modifier, shape) {
            Column(Modifier.fillMaxWidth()) {
                items.forEach {
                    itemContent(it)
                }
            }
        }
}