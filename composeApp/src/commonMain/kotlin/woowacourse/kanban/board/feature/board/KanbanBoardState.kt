package woowacourse.kanban.board.feature.board

import androidx.compose.runtime.Composable
import androidx.compose.runtime.Stable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import woowacourse.kanban.board.domain.CanDeleteResult
import woowacourse.kanban.board.domain.KanbanBoard
import woowacourse.kanban.board.domain.KanbanTask
import woowacourse.kanban.board.domain.MoveResult
import woowacourse.kanban.board.domain.TaskStatus
import woowacourse.kanban.board.feature.board.component.dialog.model.TaskFormResult
import woowacourse.kanban.board.feature.board.model.SnackbarEvent
import woowacourse.kanban.board.feature.board.model.SnackbarMessageType
import java.util.UUID

@Stable
class KanbanBoardState(initialBoard: KanbanBoard = KanbanBoard()) {

    var kanbanBoard by mutableStateOf(initialBoard)
        private set
    var isTaskDialogVisible by mutableStateOf(false)
        private set

    var isCardDialogVisible by mutableStateOf(false)
        private set

    var selectedTask by mutableStateOf<KanbanTask?>(null)
        private set

    var snackbarEvent: SnackbarEvent? by mutableStateOf(null)
        private set

    private var nextSnackbarId = 0L

    fun showTaskDialog() {
        isTaskDialogVisible = true
    }

    fun showCardDialog(task: KanbanTask) {
        selectedTask = task
        isCardDialogVisible = true
    }

    fun hideTaskDialog() {
        isTaskDialogVisible = false
    }

    fun hideCardDialog() {
        selectedTask = null
        isCardDialogVisible = false
    }

    fun clearSnackbar(consumedId: Long) {
        if (snackbarEvent?.id == consumedId) {
            snackbarEvent = null
        }
    }

    private fun emitSnackbar(type: SnackbarMessageType) {
        snackbarEvent = SnackbarEvent(
            id = ++nextSnackbarId,
            type = type,
        )
    }

    fun moveTask(task: KanbanTask, targetStatus: TaskStatus) {
        when (val result = kanbanBoard.moveTask(task.id, targetStatus)) {
            is MoveResult.MoveSuccess -> {
                kanbanBoard = result.updatedBoard
                emitSnackbar(SnackbarMessageType.TaskMoved)
            }

            is MoveResult.MoveFailed -> {
                emitSnackbar(SnackbarMessageType.TaskMoveFailed)
            }
        }
    }

    fun addTask(result: TaskFormResult) {
        runCatching {
            val newTask = KanbanTask(
                title = result.title,
                description = result.description,
                tags = result.tags,
                status = result.status,
                crewName = result.assignee,
            )
            kanbanBoard = kanbanBoard.copy(tasks = kanbanBoard.tasks + newTask)
            hideTaskDialog()
        }.onSuccess {
            emitSnackbar(SnackbarMessageType.TaskAdded)
        }.onFailure { e ->
            emitSnackbar(SnackbarMessageType.TaskAddFailed)
        }
    }

    fun deleteTask(task: KanbanTask) {
        when (val canDelete = kanbanBoard.canDelete(task)) {
            is CanDeleteResult.DeleteSuccess -> {
                kanbanBoard = canDelete.updatedBoard
                hideCardDialog()
                emitSnackbar(SnackbarMessageType.TaskDeleted)
            }
            is CanDeleteResult.DeleteFailed -> {
                hideCardDialog()
                emitSnackbar(SnackbarMessageType.TaskDeleteFailed)
            }
        }
    }

    fun updateTask(taskId: UUID, result: TaskFormResult) {
        runCatching {
            val updatedTask = kanbanBoard.tasks.first { it.id == taskId }.copy(
                title = result.title,
                description = result.description,
                tags = result.tags,
                status = result.status,
                crewName = result.assignee,
            )
            kanbanBoard = kanbanBoard.copy(
                tasks = kanbanBoard.tasks.map { task ->
                    if (task.id == taskId) updatedTask else task
                },
            )
            hideCardDialog()
        }.onSuccess {
            emitSnackbar(SnackbarMessageType.TaskUpdated)
        }.onFailure {
            emitSnackbar(SnackbarMessageType.TaskUpdateFailed)
        }
    }
}

@Composable
fun rememberKanbanBoardState(): KanbanBoardState = remember { KanbanBoardState() }
