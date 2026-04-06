package woowacourse.kanban.board.feature.board

import androidx.compose.runtime.Composable
import androidx.compose.runtime.Stable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import java.util.UUID
import woowacourse.kanban.board.domain.AddResult
import woowacourse.kanban.board.domain.CanDeleteResult
import woowacourse.kanban.board.domain.KanbanBoard
import woowacourse.kanban.board.domain.KanbanTask
import woowacourse.kanban.board.domain.MoveResult
import woowacourse.kanban.board.domain.TaskFormResult
import woowacourse.kanban.board.domain.TaskStatus
import woowacourse.kanban.board.domain.UpdateResult
import woowacourse.kanban.board.feature.board.model.SnackbarEvent
import woowacourse.kanban.board.feature.board.model.SnackbarMessageType

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
        when (val addResult = kanbanBoard.addTask(result)) {
            is AddResult.AddSuccess -> {
                kanbanBoard = addResult.updatedBoard
                hideTaskDialog()
                emitSnackbar(SnackbarMessageType.TaskAdded)
            }

            is AddResult.AddFailed -> {
                emitSnackbar(SnackbarMessageType.TaskAddFailed)
            }
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
        when (val updateResult = kanbanBoard.updateTask(taskId, result)) {
            is UpdateResult.UpdateSuccess -> {
                kanbanBoard = updateResult.updatedBoard
                hideCardDialog()
                emitSnackbar(SnackbarMessageType.TaskUpdated)
            }

            is UpdateResult.UpdateFailed -> {
                hideCardDialog()
                emitSnackbar(SnackbarMessageType.TaskUpdateFailed)
            }
        }
    }
}

@Composable
fun rememberKanbanBoardState(): KanbanBoardState = remember { KanbanBoardState() }
