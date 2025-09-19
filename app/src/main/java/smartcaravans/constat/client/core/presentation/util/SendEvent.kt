package smartcaravans.constat.client.core.presentation.util

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.launch
import smartcaravans.constat.client.core.util.Event
import smartcaravans.constat.client.core.util.EventBus

fun ViewModel.sendEvent(event: Event) {
    viewModelScope.launch {
        EventBus.sendEvent(event)
    }
}