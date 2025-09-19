package smartcaravans.constat.client.core.presentation

import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.AnimatedContentTransitionScope
import androidx.compose.animation.ContentTransform
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Error
import androidx.compose.material3.CircularWavyProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3ExpressiveApi
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import smartcaravans.constat.client.R
import smartcaravans.constat.client.core.domain.util.SimpleError
import smartcaravans.constat.client.core.util.Resource
import soup.compose.material.motion.animation.materialSharedAxisZ


@OptIn(ExperimentalMaterial3ExpressiveApi::class)
@Composable
fun<D> ResourceContainer(
    resource: Resource<D>,
    modifier: Modifier = Modifier,
    successTransitionSpec:  AnimatedContentTransitionScope<Boolean>.() -> ContentTransform
    = { materialSharedAxisZ(true) },
    resourceTransitionSpec:  AnimatedContentTransitionScope<Resource<D>>.() -> ContentTransform
    = { materialSharedAxisZ(true) },
    idleContent: @Composable () -> Unit = {
        Box(Modifier.heightIn(200.dp).fillMaxSize(), contentAlignment = Alignment.Center) {
            Text(
                stringResource(R.string.no_data_available),
                Modifier.fillMaxWidth(),
                MaterialTheme.colorScheme.onSurfaceVariant,
                textAlign = TextAlign.Center
            )
        }
    },
    loadingContent: @Composable () -> Unit = {
        Box(Modifier.heightIn(200.dp).fillMaxSize(), contentAlignment = Alignment.Center) {
            CircularWavyProgressIndicator(Modifier.size(100.dp))
        }
    },
    errorContent: @Composable (Resource.Error) -> Unit = { error ->
        Column(
            Modifier.heightIn(200.dp).fillMaxSize(),
            verticalArrangement = Arrangement.spacedBy(16.dp, Alignment.CenterVertically),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            IconContainer(icon = Icons.Default.Error, Modifier.size(100.dp))
            Text(
                stringResource(R.string.something_went_wrong),
                Modifier.fillMaxWidth(),
                MaterialTheme.colorScheme.onSurfaceVariant,
                textAlign = TextAlign.Center
            )
        }
    },
    content: @Composable (D) -> Unit,
) {
    AnimatedContent(
        resource is Resource.Success,
        modifier,
        successTransitionSpec
    ) { isSuccess ->
        if (isSuccess)
            (resource as? Resource.Success)?.data?.let { data ->
                content(data)
            }
        else {
            AnimatedContent(
                resource,
                modifier,
                resourceTransitionSpec
            ) {
                when(it) {
                    is Resource.Error -> errorContent(it)
                    Resource.Loading -> loadingContent()
                    else -> idleContent()
                }
            }
        }
    }
}