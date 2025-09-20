package smartcaravans.constat.client.main.presentation.navigation.routes.forms

import androidx.compose.ui.Alignment
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp

enum class DamagePlaces(
    val alignment: Alignment,
    val offset: Pair<Dp, Dp>
) {
    FRONT(Alignment.TopCenter, 0.dp to 0.dp),
    BACK(Alignment.BottomCenter, 0.dp to 0.dp),
    LEFT1(Alignment.TopStart, 20.dp to 60.dp),
    LEFT2(Alignment.CenterStart, 20.dp to -(60).dp),
    LEFT3(Alignment.CenterStart, 20.dp to 60.dp),
    LEFT4(Alignment.BottomStart, 20.dp to (-60).dp),
    RIGHT1(Alignment.TopEnd, (-20).dp to 60.dp),
    RIGHT2(Alignment.CenterEnd, (-20).dp to (-60).dp),
    RIGHT3(Alignment.CenterEnd, (-20).dp to 60.dp),
    RIGHT4(Alignment.BottomEnd, (-20).dp to (-60).dp),
    ROOF(Alignment.Center, 0.dp to 0.dp),
}