package smartcaravans.constat.client.core.util

import androidx.activity.compose.LocalOnBackPressedDispatcherOwner
import androidx.compose.runtime.Composable

@Composable
fun getBackDispatcher() = LocalOnBackPressedDispatcherOwner.current?.onBackPressedDispatcher

@Composable
fun PerformBack() = getBackDispatcher()?.onBackPressed()