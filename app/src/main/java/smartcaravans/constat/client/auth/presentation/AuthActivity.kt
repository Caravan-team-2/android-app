package smartcaravans.constat.client.auth.presentation

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.material3.Surface
import org.koin.android.ext.android.get
import smartcaravans.constat.client.core.presentation.util.SetSystemBarColors
import smartcaravans.constat.client.ui.theme.AppTheme

class AuthActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            AppTheme {
                SetSystemBarColors(get())
                Surface {
                    AuthScreen()
                    PermissionsDialog()
                }
            }
        }
    }
}
