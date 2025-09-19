package smartcaravans.constat.client.auth.presentation.util

import android.content.Context
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Camera
import androidx.compose.material.icons.filled.LocationOn
import androidx.compose.material.icons.filled.Notifications
import androidx.compose.material.icons.filled.Storage
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.core.content.ContextCompat
import smartcaravans.constat.client.R

enum class Permissions(
    val permissionString: String,
    val requestCode: Int,
    val titleRes: Int,
    val messageRes: Int,
    val icon: ImageVector
) {
    LOCATION(
        android.Manifest.permission.ACCESS_FINE_LOCATION,
        1001,
        R.string.permission_location_title,
        R.string.permission_location_message,
        Icons.Default.LocationOn
    ),
    CAMERA(
        android.Manifest.permission.CAMERA,
        1002,
        R.string.permission_camera_title,
        R.string.permission_camera_message,
        Icons.Default.Camera
    ),
//    STORAGE(
//        android.Manifest.permission.READ_EXTERNAL_STORAGE,
//        1003,
//        R.string.permission_storage_title,
//        R.string.permission_storage_message,
//        Icons.Default.Storage
//    ),
    NOTIFICATIONS(
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.TIRAMISU) {
            android.Manifest.permission.POST_NOTIFICATIONS
        } else {
            android.Manifest.permission.ACCESS_NOTIFICATION_POLICY
        },
        1004,
        R.string.permission_notifications_title,
        R.string.permission_notifications_message,
        Icons.Default.Notifications
    );

    fun isGranted(context: Context): Boolean =
        ContextCompat.checkSelfPermission(context, permissionString) ==
                android.content.pm.PackageManager.PERMISSION_GRANTED

    fun isDenied(context: Context): Boolean =
        ContextCompat.checkSelfPermission(context, permissionString) ==
                android.content.pm.PackageManager.PERMISSION_DENIED

    companion object {
        fun getGrantedPermissions(context: Context): Set<Permissions> =
            entries.filter { it.isGranted(context) }.toSet()

        fun getDeniedPermissions(context: Context): Set<Permissions> =
            entries.filter { it.isDenied(context) }.toSet()

        fun fromPermissionString(permission: String): Permissions? =
            entries.find { it.permissionString == permission }
    }
}