package woowacourse.kanban.board.feature.board.component

import androidx.compose.foundation.background
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateMapOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Rect
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Devices
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import woowacourse.kanban.board.domain.KanbanBoard
import woowacourse.kanban.board.domain.KanbanTask
import woowacourse.kanban.board.domain.TaskStatus

@Composable
fun KanbanBoardContent(
    kanbanBoard: KanbanBoard,
    onTaskCreateClick: () -> Unit,
    onMoveTask: (KanbanTask, TaskStatus) -> Unit,
    modifier: Modifier = Modifier,
) {
    var draggedTask by remember { mutableStateOf<KanbanTask?>(null) }
    var currentDragPosition by remember { mutableStateOf<Offset?>(null) }
    val columnBounds = remember { mutableStateMapOf<TaskStatus, Rect>() }

    Column(
        modifier = modifier
            .background(Color.White)
            .fillMaxSize(),
    ) {
        KanbanBoardHeader(
            board = kanbanBoard,
            onClick = onTaskCreateClick,
            modifier = Modifier,
        )

        Row(
            modifier = Modifier
                .weight(1f)
                .padding(24.dp)
                .horizontalScroll(rememberScrollState()),
            horizontalArrangement = Arrangement.spacedBy(16.dp),
        ) {
            TaskStatus.entries.forEach { status ->
                KanbanColumn(
                    status = status,
                    tasks = kanbanBoard.getTasksByStatus(status),
                    getIsDropTarget = {
                        currentDragPosition?.let { columnBounds[status]?.contains(it) } ?: false
                    },
                    onBoundsChanged = { rect -> columnBounds[status] = rect },
                    onTaskDragStart = { task -> draggedTask = task },
                    onTaskDragChange = { pos -> currentDragPosition = pos },
                    onTaskDragEnd = {
                        val dropPosition = currentDragPosition
                        val targetStatus = dropPosition?.let { pos ->
                            columnBounds.entries.firstOrNull { (_, rect) -> rect.contains(pos) }?.key
                        }
                        draggedTask?.let { task ->
                            if (targetStatus != null && task.status != targetStatus) {
                                onMoveTask(task, targetStatus)
                            }
                        }
                        draggedTask = null
                        currentDragPosition = null
                    },
                    onTaskDragCancel = {
                        draggedTask = null
                        currentDragPosition = null
                    },
                )
            }
        }
    }
}

@Preview(device = Devices.TABLET)
@Composable
private fun KanbanBoardScreenPreview() {
    KanbanBoardContent(
        kanbanBoard = KanbanBoard(emptyList()),
        onTaskCreateClick = {},
        onMoveTask = { _, _ -> },
    )
}
