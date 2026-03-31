package woowacourse.kanban.board.feature.board.component.card

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccountCircle
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import org.jetbrains.compose.resources.DrawableResource
import org.jetbrains.compose.resources.painterResource

@Composable
fun KanbanCardProfile(crewName: String, crewImage: DrawableResource, modifier: Modifier = Modifier) {
    KanbanCardProfileLayout(crewName = crewName, modifier = modifier) {
        Image(
            painter = painterResource(resource = crewImage),
            contentDescription = "사용자 이미지",
            modifier = Modifier.size(24.dp).clip(CircleShape),
        )
    }
}

@Composable
fun KanbanCardProfile(crewName: String, modifier: Modifier = Modifier) {
    KanbanCardProfileLayout(crewName = crewName, modifier = modifier) {
        Icon(
            imageVector = Icons.Default.AccountCircle,
            contentDescription = "기본 이미지",
            modifier = Modifier.size(24.dp).clip(CircleShape),
        )
    }
}

@Composable
private fun KanbanCardProfileLayout(crewName: String, modifier: Modifier = Modifier, imageContent: @Composable () -> Unit) {
    Row(
        modifier = modifier.padding(end = 20.dp, bottom = 10.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(8.dp),
    ) {
        imageContent()

        Text(
            text = crewName,
            fontSize = 14.sp,
            maxLines = 1,
            overflow = TextOverflow.Ellipsis,
        )
    }
}

@Preview
@Composable
private fun KanbanCardProfilePreview() {
    KanbanCardProfile(crewName = "바드")
}
