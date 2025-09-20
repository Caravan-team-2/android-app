package smartcaravans.constat.client.core.presentation

import android.graphics.Bitmap
import android.graphics.Canvas
import android.graphics.Paint
import android.graphics.RectF
import android.location.Geocoder
import android.os.Build
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.LocationOn
import androidx.compose.material.icons.filled.MyLocation
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.mapbox.geojson.Point
import com.mapbox.maps.CameraOptions
import com.mapbox.maps.Style
import com.mapbox.maps.extension.compose.MapEffect
import com.mapbox.maps.extension.compose.MapboxMap
import com.mapbox.maps.extension.compose.animation.viewport.rememberMapViewportState
import com.mapbox.maps.extension.compose.style.MapStyle
import com.mapbox.maps.plugin.annotation.annotations
import com.mapbox.maps.plugin.annotation.generated.PointAnnotationManager
import com.mapbox.maps.plugin.annotation.generated.PointAnnotationOptions
import com.mapbox.maps.plugin.annotation.generated.createPointAnnotationManager
import com.mapbox.maps.plugin.gestures.gestures
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import java.util.Locale
import smartcaravans.constat.client.R

@Composable
fun MapLocationPicker(
    currentLocation: Location,
    onLocationSelected: (Location) -> Unit,
    onDismiss: () -> Unit,
    modifier: Modifier = Modifier
) {
    val context = LocalContext.current
    var selectedLocation by remember { mutableStateOf(currentLocation) }
    var pointAnnotationManager by remember { mutableStateOf<PointAnnotationManager?>(null) }
    val pinBitmap = remember { createPinBitmap() }

    val initialLat = currentLocation.latitude ?: 48.8566 // Paris default
    val initialLng = currentLocation.longitude ?: 2.3522

    val mapViewportState = rememberMapViewportState {
        setCameraOptions(
            CameraOptions.Builder()
                .center(Point.fromLngLat(initialLng, initialLat))
                .zoom(15.0)
                .build()
        )
    }

    // Reverse geocode whenever lat/lng changes (and address is empty or changed)
    LaunchedEffect(selectedLocation.latitude, selectedLocation.longitude) {
        val lat = selectedLocation.latitude
        val lng = selectedLocation.longitude
        if (lat != null && lng != null) {
            val geocoder = Geocoder(context, Locale.getDefault())
            try {
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
                    geocoder.getFromLocation(lat, lng, 1) { list ->
                        val address = list.firstOrNull()?.getAddressLine(0) ?: ""
                        if (address.isNotEmpty()) {
                            selectedLocation = selectedLocation.copy(address = address)
                        }
                    }
                } else {
                    val list = withContext(Dispatchers.IO) { geocoder.getFromLocation(lat, lng, 1) }
                    val address = list?.firstOrNull()?.getAddressLine(0) ?: ""
                    if (address.isNotEmpty()) {
                        selectedLocation = selectedLocation.copy(address = address)
                    }
                }
            } catch (_: Exception) {
            }
        }
    }

    Column(
        modifier = modifier.padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        // Header
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = "Choisir l'emplacement",
                style = MaterialTheme.typography.headlineSmall,
                color = MaterialTheme.colorScheme.onSurface
            )
            IconButton(onClick = onDismiss) {
                Icon(
                    Icons.Default.Close,
                    contentDescription = "Fermer"
                )
            }
        }

        // Map container
        Card(
            modifier = Modifier
                .fillMaxWidth()
                .height(400.dp),
            elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
        ) {
            Box(modifier = Modifier.fillMaxSize()) {
                MapboxMap(
                    modifier = Modifier.fillMaxSize(),
                    mapViewportState = mapViewportState,
                    style = { MapStyle(Style.MAPBOX_STREETS) }
                ) {
                    // Set up gestures listener and add style image + manager once
                    MapEffect(Unit) { mapView ->
                        // create manager if null
                        if (pointAnnotationManager == null) {
                            pointAnnotationManager = mapView.annotations.createPointAnnotationManager()
                        }

                        // click listener to update selection
                        mapView.gestures.addOnMapClickListener { point ->
                            selectedLocation = selectedLocation.copy(
                                latitude = point.latitude(),
                                longitude = point.longitude()
                            )
                            mapViewportState.setCameraOptions(
                                CameraOptions.Builder()
                                    .center(Point.fromLngLat(point.longitude(), point.latitude()))
                                    .build()
                            )
                            true
                        }
                    }

                    // Update marker when selection changes
                    MapEffect(selectedLocation) { _ ->
                        val lat = selectedLocation.latitude
                        val lng = selectedLocation.longitude
                        val manager = pointAnnotationManager
                        if (lat != null && lng != null && manager != null) {
                            manager.deleteAll()
                            val options = PointAnnotationOptions()
                                .withPoint(Point.fromLngLat(lng, lat))
                                .apply {
                                    withIconImage(pinBitmap)
                                }
                            manager.create(options)
                        }
                    }
                }


                // Current location FAB (TODO: wire to actual device location)
                FloatingActionButton(
                    onClick = {

                    },
                    modifier = Modifier
                        .align(Alignment.BottomEnd)
                        .padding(16.dp),
                    containerColor = MaterialTheme.colorScheme.primary
                ) {
                    Icon(
                        Icons.Default.MyLocation,
                        contentDescription = "Ma position"
                    )
                }
            }
        }

        // Selected location info
        if (selectedLocation.address.isNotEmpty()) {
            Card(
                modifier = Modifier.fillMaxWidth(),
                colors = CardDefaults.cardColors(
                    containerColor = MaterialTheme.colorScheme.primaryContainer
                )
            ) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(16.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Icon(
                        Icons.Default.LocationOn,
                        contentDescription = null,
                        tint = MaterialTheme.colorScheme.onPrimaryContainer,
                        modifier = Modifier.size(24.dp)
                    )
                    Text(
                        text = selectedLocation.address,
                        modifier = Modifier
                            .weight(1f)
                            .padding(start = 12.dp),
                        style = MaterialTheme.typography.bodyMedium,
                        color = MaterialTheme.colorScheme.onPrimaryContainer
                    )
                }
            }
        }

        // Action buttons
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            OutlinedButton(
                onClick = onDismiss,
                modifier = Modifier.weight(1f)
            ) {
                Text(stringResource(R.string.cancel))
            }

            androidx.compose.material3.Button(
                onClick = { onLocationSelected(selectedLocation) },
                modifier = Modifier.weight(1f),
                enabled = selectedLocation.latitude != null && selectedLocation.longitude != null
            ) {
                Icon(
                    Icons.Default.Check,
                    contentDescription = null,
                    modifier = Modifier.size(18.dp)
                )
                Text(
                    text = stringResource(R.string.confirm),
                    modifier = Modifier.padding(start = 8.dp)
                )
            }
        }
    }
}

private fun createPinBitmap(): Bitmap {
    val size = 96
    val bitmap = Bitmap.createBitmap(size, size, Bitmap.Config.ARGB_8888)
    val canvas = Canvas(bitmap)

    val paint = Paint(Paint.ANTI_ALIAS_FLAG).apply {
        color = 0xFFE53935.toInt() // red
        style = Paint.Style.FILL
    }
    val shadow = Paint(Paint.ANTI_ALIAS_FLAG).apply {
        color = 0x33000000
        style = Paint.Style.FILL
    }
    // draw shadow ellipse
    val shadowRect = RectF(size * 0.25f, size * 0.82f, size * 0.75f, size * 0.95f)
    canvas.drawOval(shadowRect, shadow)
    // draw circle
    val centerX = size / 2f
    val centerY = size / 2.8f
    val radius = size * 0.28f
    canvas.drawCircle(centerX, centerY, radius, paint)
    // draw pointer triangle
    val path = android.graphics.Path()
    path.moveTo(centerX - radius * 0.35f, centerY + radius * 0.6f)
    path.lineTo(centerX + radius * 0.35f, centerY + radius * 0.6f)
    path.lineTo(centerX, size * 0.9f)
    path.close()
    canvas.drawPath(path, paint)

    // inner white circle
    val inner = Paint(Paint.ANTI_ALIAS_FLAG).apply { color = 0xFFFFFFFF.toInt() }
    canvas.drawCircle(centerX, centerY, radius * 0.45f, inner)

    return bitmap
}
