package woowacourse.kanban.board.feature.board

import androidx.compose.runtime.Composable
import androidx.compose.runtime.Stable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import woowacourse.kanban.board.domain.KanbanBoard
import woowacourse.kanban.board.domain.KanbanTask
import woowacourse.kanban.board.domain.SnackbarEvent
import woowacourse.kanban.board.domain.TaskStatus
import woowacourse.kanban.board.feature.board.component.dialog.model.TaskFormResult

@Stable
class KanbanBoardState(initialBoard: KanbanBoard = KanbanBoard()) {

    var kanbanBoard by mutableStateOf(initialBoard)
        private set
    var isTaskDialogVisible by mutableStateOf(false)
        private set

    var snackbarEvent: SnackbarEvent? by mutableStateOf(null)
        private set

    private var nextSnackbarId = 0L

    fun showTaskDialog() {
        isTaskDialogVisible = true
    }

    fun hideTaskDialog() {
        isTaskDialogVisible = false
    }

    fun clearSnackbar(consumedId: Long) {
        if (snackbarEvent?.id == consumedId) {
            snackbarEvent = null
        }
    }

    private fun emitSnackbar(message: String) {
        snackbarEvent = SnackbarEvent(
            id = ++nextSnackbarId,
            message = message,
        )
    }

    fun moveTask(task: KanbanTask, targetStatus: TaskStatus) {
        kanbanBoard = kanbanBoard.moveTask(task.id, targetStatus)
        emitSnackbar("태스크가 이동되었습니다.")
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
            emitSnackbar("새로운 태스크가 추가되었습니다.")
        }.onFailure { e ->
            emitSnackbar("태스크 추가에 실패했습니다.")
        }
    }
}

@Composable
fun rememberKanbanBoardState(): KanbanBoardState = remember { KanbanBoardState() }
