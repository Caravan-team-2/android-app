package smartcaravans.constat.client.main.presentation.navigation.routes.forms

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowForward
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Photo
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.FloatingActionButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import smartcaravans.constat.client.R
import smartcaravans.constat.client.core.presentation.Dialog
import smartcaravans.constat.client.core.presentation.IconContainer
import smartcaravans.constat.client.core.presentation.MyImage
import smartcaravans.constat.client.core.presentation.SimpleCameraView
import smartcaravans.constat.client.ui.theme.AppTheme
import java.io.File

@Composable
fun PictureFormScreen(
    formState: PictureFormState,
    onState: (PictureFormState) -> Unit,
    modifier: Modifier = Modifier
) {
    var preview by remember { mutableStateOf<File?>(null) }
    var photosDialog by remember { mutableStateOf(false) }
    Column(
        modifier
            .fillMaxSize()
            .padding(16.dp, 0.dp, 16.dp, 16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        SimpleCameraView(
            Modifier
                .clip(MaterialTheme.shapes.extraLarge)
                .weight(1f)
                .fillMaxSize()
        ) {
            preview = it
        }
        Surface(
            Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(100),
            color = MaterialTheme.colorScheme.primaryContainer,
            contentColor = MaterialTheme.colorScheme.onPrimaryContainer
        ) {
            Row(
                Modifier
                    .clickable {
                        photosDialog = true
                    }.padding(24.dp),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Text(
                    stringResource(R.string.pictures),
                    style = MaterialTheme.typography.titleMedium
                )
                Icon(
                    Icons.AutoMirrored.Filled.ArrowForward,
                    null,
                    tint = MaterialTheme.colorScheme.onPrimaryContainer
                )
            }
        }
    }
    Dialog(
        preview != null,
        {
            preview = null
        },
        title = stringResource(R.string.confirm),
        okListener = {
            preview?.let {
                onState(formState.copy(pictures = formState.pictures + it))
                preview = null
            }
        },
        cancelListener = {
            preview = null
        }
    ) {
        MyImage(
            preview!!.path,
            Icons.Default.Photo,
            Modifier
                .fillMaxWidth()
                .aspectRatio(1f)
                .padding(24.dp)
        )
    }
    Dialog(
        photosDialog,
        {
            photosDialog = false
        },
        title = stringResource(R.string.pictures),
        okListener = {
            photosDialog = false
        }
    ) {
        if (formState.pictures.isEmpty()) {
            Column(
                Modifier
                    .fillMaxWidth()
                    .height(240.dp),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.spacedBy(8.dp, Alignment.CenterVertically)
            ) {
                IconContainer(
                    Icons.Default.Photo,
                    Modifier.size(120.dp),
                )
                Text(
                    stringResource(R.string.no_pictures_yet),
                    style = MaterialTheme.typography.bodyLarge,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }
        } else {
            LazyVerticalGrid(
                columns = GridCells.Fixed(2),
                Modifier.weight(1f, false).fillMaxWidth().heightIn(300.dp),
                horizontalArrangement = Arrangement.spacedBy(8.dp),
                verticalArrangement = Arrangement.spacedBy(8.dp),
                contentPadding = PaddingValues(16.dp)
            ) {
                items(formState.pictures, { it.path }) { photo ->
                    Box(
                        modifier = Modifier
                            .animateItem()
                            .fillMaxWidth()
                            .aspectRatio(1f)
                    ) {
                        Card(
                            modifier = Modifier.fillMaxSize(),
                            shape = RoundedCornerShape(12.dp),
                            elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
                        ) {
                            MyImage(
                                photo.path,
                                Icons.Default.Photo,
                                modifier = Modifier
                                    .fillMaxSize()
                                    .clip(RoundedCornerShape(12.dp))
                            )
                        }

                        FloatingActionButton(
                            onClick = {
                                onState(formState.copy(pictures = formState.pictures - photo))
                            },
                            modifier = Modifier
                                .align(Alignment.TopEnd)
                                .offset(x = (-8).dp, y = 8.dp)
                                .size(32.dp),
                            containerColor = MaterialTheme.colorScheme.error,
                            contentColor = MaterialTheme.colorScheme.onError,
                            elevation = FloatingActionButtonDefaults.elevation(
                                defaultElevation = 6.dp,
                                pressedElevation = 8.dp
                            )
                        ) {
                            Icon(
                                Icons.Default.Close,
                                contentDescription = stringResource(R.string.delete_photo),
                                modifier = Modifier.size(16.dp)
                            )
                        }
                    }
                }
            }
        }
    }
}

@Preview
@Composable
private fun PicturesFormPreview() {
    var state by remember { mutableStateOf(PictureFormState()) }
    AppTheme {
        PictureFormScreen(state, { state = it })
    }
}