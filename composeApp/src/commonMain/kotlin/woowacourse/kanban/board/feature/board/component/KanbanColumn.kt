package woowacourse.kanban.board.feature.board.component

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Rect
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.boundsInWindow
import androidx.compose.ui.layout.onGloballyPositioned
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Devices
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import woowacourse.kanban.board.core.designsystem.theme.KanbanDeepGreen
import woowacourse.kanban.board.core.designsystem.theme.KanbanLightBlue
import woowacourse.kanban.board.core.designsystem.theme.KanbanLightGreen
import woowacourse.kanban.board.core.designsystem.theme.KanbanLightYellow
import woowacourse.kanban.board.core.designsystem.theme.KanbanOrange
import woowacourse.kanban.board.domain.KanbanTask
import woowacourse.kanban.board.domain.Tag
import woowacourse.kanban.board.domain.TaskStatus
import woowacourse.kanban.board.feature.board.component.card.KanbanCard

@Composable
fun KanbanColumn(
    status: TaskStatus,
    tasks: List<KanbanTask>,
    modifier: Modifier = Modifier,
    getIsDropTarget: () -> Boolean = { false },
    onBoundsChanged: (Rect) -> Unit = {},
    onTaskDragStart: (KanbanTask) -> Unit = {},
    onTaskDragChange: (Offset) -> Unit = {},
    onTaskDragEnd: () -> Unit = {},
    onTaskDragCancel: () -> Unit = {},
) {
    val title = status.displayName
    val (headerBackgroundColor, contentBackgroundColor) = status.colors
    val isDropTarget by remember { derivedStateOf { getIsDropTarget() } }
    val lastBounds = remember { mutableStateOf<Rect?>(null) }

    Column(
        modifier = modifier
            .width(320.dp)
            .fillMaxHeight()
            .onGloballyPositioned {
                val newBounds = it.boundsInWindow()
                if (newBounds != lastBounds.value) {
                    lastBounds.value = newBounds
                    onBoundsChanged(newBounds)
                }
            }
            .then(
                if (isDropTarget) {
                    Modifier.border(2.dp, headerBackgroundColor, RoundedCornerShape(10.dp))
                } else {
                    Modifier
                },
            )
            .clip(RoundedCornerShape(10.dp)),
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .background(headerBackgroundColor)
                .padding(vertical = 12.dp, horizontal = 16.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween,
        ) {
            Text(
                text = title,
                fontSize = 16.sp,
                fontWeight = FontWeight.SemiBold,
                color = Color.White,
            )

            Text(
                text = tasks.size.toString(),
                fontSize = 14.sp,
                modifier = Modifier
                    .clip(RoundedCornerShape(10.dp))
                    .background(Color.White)
                    .padding(vertical = 2.dp, horizontal = 10.dp),
            )
        }

        LazyColumn(
            modifier = Modifier
                .weight(1f)
                .fillMaxWidth()
                .background(contentBackgroundColor),
            contentPadding = PaddingValues(16.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp),
        ) {
            items(items = tasks, key = { it.id }) { item ->
                KanbanCard(
                    title = item.title,
                    crewName = item.crewName,
                    tags = item.tags,
                    description = item.description,
                    onDragStart = { onTaskDragStart(item) },
                    onDragChange = onTaskDragChange,
                    onDragEnd = onTaskDragEnd,
                    onDragCancel = onTaskDragCancel,
                )
            }
        }
    }
}

private val TaskStatus.displayName: String
    get() = when (this) {
        TaskStatus.TODO -> "To Do"
        TaskStatus.IN_PROGRESS -> "In Progress"
        TaskStatus.DONE -> "Done"
    }

private val TaskStatus.colors: Pair<Color, Color>
    get() = when (this) {
        TaskStatus.TODO -> Color.Blue to Color.KanbanLightBlue
        TaskStatus.IN_PROGRESS -> Color.KanbanOrange to Color.KanbanLightYellow
        TaskStatus.DONE -> Color.KanbanDeepGreen to Color.KanbanLightGreen
    }

@Preview(device = Devices.TABLET)
@Composable
private fun KanbanColumnPreview() {
    val kanbanTask1 = KanbanTask(
        title = "제목",
        description = "본문",
        tags = listOf(Tag("태그")),
        status = TaskStatus.TODO,
        crewName = "다이노",
    )
    val kanbanTask2 = KanbanTask(
        title = "제목",
        status = TaskStatus.TODO,
        crewName = "페임스",
    )

    Row(
        modifier = Modifier.padding(10.dp),
        horizontalArrangement = Arrangement.spacedBy(10.dp),
    ) {
        KanbanColumn(
            status = TaskStatus.TODO,
            tasks = listOf(kanbanTask1, kanbanTask2),
        )

        KanbanColumn(
            status = TaskStatus.IN_PROGRESS,
            tasks = listOf(kanbanTask1, kanbanTask2),
        )

        KanbanColumn(
            status = TaskStatus.DONE,
            tasks = listOf(kanbanTask1, kanbanTask2),
        )
    }
}
