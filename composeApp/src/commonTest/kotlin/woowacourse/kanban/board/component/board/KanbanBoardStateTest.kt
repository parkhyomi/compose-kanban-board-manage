package woowacourse.kanban.board.component.board

import kotlin.test.Test
import org.assertj.core.api.Assertions.assertThat
import woowacourse.kanban.board.domain.KanbanBoard
import woowacourse.kanban.board.domain.KanbanTask
import woowacourse.kanban.board.domain.TaskFormResult
import woowacourse.kanban.board.domain.TaskStatus
import woowacourse.kanban.board.feature.board.KanbanBoardState
import woowacourse.kanban.board.feature.board.model.SnackbarMessageType

class KanbanBoardStateTest {

    @Test
    fun `초기 상태에서는 보드가 비어있고 다이얼로그가 보이지 않는다`() {
        // Given & When
        val state = KanbanBoardState()

        // Then
        assertThat(state.kanbanBoard.getTaskCountByTotal).isEqualTo(0)
        assertThat(state.isTaskDialogVisible).isFalse()
    }

    @Test
    fun `초기 보드 상태를 주입하면 해당 상태로 초기화된다`() {
        // Given
        val initialBoard = KanbanBoard()
        val state = KanbanBoardState(initialBoard)

        // Then
        assertThat(state.kanbanBoard).isEqualTo(initialBoard)
    }

    @Test
    fun `showTaskDialog를 호출하면 다이얼로그가 보이는 상태가 된다`() {
        // Given
        val state = KanbanBoardState()

        // When
        state.showTaskDialog()

        // Then
        assertThat(state.isTaskDialogVisible).isTrue()
    }

    @Test
    fun `hideTaskDialog를 호출하면 다이얼로그가 보이지 않는 상태가 된다`() {
        // Given
        val state = KanbanBoardState()
        state.showTaskDialog()

        // When
        state.hideTaskDialog()

        // Then
        assertThat(state.isTaskDialogVisible).isFalse()
    }

    @Test
    fun `addTask 성공 시 snackbarEvent의 TaskAdded가 호출된다`() {
        // Given
        val state = KanbanBoardState()
        val result = TaskFormResult(
            title = "새로운 태스크",
            description = null,
            tags = emptyList(),
            status = TaskStatus.TODO,
            assignee = "다이노",
        )

        // When
        state.addTask(result)

        // Then
        assertThat(state.snackbarEvent).isNotNull()
        assertThat(state.snackbarEvent?.type).isEqualTo(SnackbarMessageType.TaskAdded)
    }

    @Test
    fun `addTask 실패 snackbarEvent의 TaskAddFailed 호출된다`() {
        // Given
        val state = KanbanBoardState()
        val result = TaskFormResult(
            title = "",
            description = null,
            tags = emptyList(),
            status = TaskStatus.TODO,
            assignee = "다이노",
        )

        // When
        state.addTask(result)

        // Then
        assertThat(state.snackbarEvent?.type).isEqualTo(SnackbarMessageType.TaskAddFailed)
        assertThat(state.kanbanBoard.getTaskCountByTotal).isEqualTo(0)
    }

    @Test
    fun `clearSnackbar 호출 후 snackbarEvent가 null로 초기화된다`() {
        // Given
        val state = KanbanBoardState()
        state.addTask(
            TaskFormResult(
                title = "태스크",
                description = null,
                tags = emptyList(),
                status = TaskStatus.TODO,
                assignee = "다이노",
            ),
        )
        val event = state.snackbarEvent
        assertThat(event).isNotNull()

        // When
        state.clearSnackbar(event!!.id)

        // Then
        assertThat(state.snackbarEvent).isNull()
    }

    @Test
    fun `addTask를 호출하면 보드에 태스크가 추가되고 다이얼로그가 닫힌다`() {
        // Given
        val state = KanbanBoardState()
        state.showTaskDialog()

        val result = TaskFormResult(
            title = "새로운 태스크",
            description = "태스크 설명",
            tags = emptyList(),
            status = TaskStatus.TODO,
            assignee = "다이노",
        )

        // When
        state.addTask(result)

        // Then
        assertThat(state.kanbanBoard.getTaskCountByTotal).isEqualTo(1)

        val addedTask = state.kanbanBoard.allTasks().first()
        assertThat(addedTask.title).isEqualTo("새로운 태스크")
        assertThat(addedTask.description).isEqualTo("태스크 설명")
        assertThat(addedTask.status).isEqualTo(TaskStatus.TODO)
        assertThat(addedTask.crewName).isEqualTo("다이노")

        assertThat(state.isTaskDialogVisible).isFalse()
    }

    @Test
    fun `테스크 카드를 클릭하면 삭제,수정 다이어로그가 노출된다`() {
        val result = TaskFormResult(
            title = "새로운 태스크",
            description = "태스크 설명",
            tags = emptyList(),
            status = TaskStatus.TODO,
            assignee = "다이노",
        )
        val state = KanbanBoardState()
        state.addTask(result)

        val task = state.kanbanBoard.allTasks().first()
        state.showCardDialog(task)

        assertThat(state.selectedTask).isEqualTo(task)
        assertThat(state.isCardDialogVisible).isTrue()
    }

    @Test
    fun `카드를 클릭해서 제목, 내용, 태그, 상태, 담당자 변경 후 수정을 누르면 해당 카드의 수정사항이 반영되고 TaskUpdated 스낵바가 뜬다`() {
        val result = TaskFormResult(
            title = "새로운 태스크",
            description = "태스크 설명",
            tags = emptyList(),
            status = TaskStatus.TODO,
            assignee = "다이노",
        )
        val state = KanbanBoardState()
        state.addTask(result)

        val task = state.kanbanBoard.allTasks().first()
        state.showCardDialog(task)

        val updatedResult = TaskFormResult(
            title = "수정된 태스크",
            description = "수정된 태스크 설명",
            tags = emptyList(),
            status = TaskStatus.IN_PROGRESS,
            assignee = "페임스",
        )

        state.updateTask(task.id, updatedResult)

        val updatedTask = state.kanbanBoard.allTasks().first()
        assertThat(updatedTask.title).isEqualTo("수정된 태스크")
        assertThat(updatedTask.description).isEqualTo("수정된 태스크 설명")
        assertThat(updatedTask.status).isEqualTo(TaskStatus.IN_PROGRESS)
        assertThat(updatedTask.crewName).isEqualTo("페임스")
        assertThat(state.isCardDialogVisible).isFalse()

        assertThat(state.snackbarEvent?.type).isEqualTo(SnackbarMessageType.TaskUpdated)
    }

    @Test
    fun `카드의 상태가 To Do 라면 카드 삭제를 눌렀을 때 해당 id 값의 카드는 삭제되고 TaskDeleted가 뜬다`() {
        val result = TaskFormResult(
            title = "새로운 태스크",
            description = "태스크 설명",
            tags = emptyList(),
            status = TaskStatus.TODO,
            assignee = "다이노",
        )
        val state = KanbanBoardState()
        state.addTask(result)

        val task = state.kanbanBoard.allTasks().first()
        state.showCardDialog(task)

        state.deleteTask(task)

        assertThat(state.isCardDialogVisible).isFalse()

        assertThat(state.snackbarEvent?.type).isEqualTo(SnackbarMessageType.TaskDeleted)

        assertThat(state.kanbanBoard.allTasks()).isEmpty()
    }

    @Test
    fun `카드의 상태가 Review 라면 카드 삭제를 눌렀을 때 해당 id 값의 카드는 삭제되지 않고 TaskDeleteFailed가 뜬다`() {
        val result = TaskFormResult(
            title = "새로운 태스크",
            description = "태스크 설명",
            tags = emptyList(),
            status = TaskStatus.REVIEW,
            assignee = "다이노",
        )
        val state = KanbanBoardState()
        state.addTask(result)

        val task = state.kanbanBoard.allTasks().first()
        state.showCardDialog(task)

        state.deleteTask(task)

        assertThat(state.isCardDialogVisible).isFalse()

        assertThat(state.snackbarEvent?.type).isEqualTo(SnackbarMessageType.TaskDeleteFailed)

        assertThat(state.kanbanBoard.allTasks()).isNotEmpty()
    }

    @Test
    fun `카드의 상태가 In Progress이라면 카드 삭제를 눌렀을 때 해당 id 값의 카드는 삭제되고 TaskDeleted가 뜬다`() {
        val result = TaskFormResult(
            title = "새로운 태스크",
            description = "태스크 설명",
            tags = emptyList(),
            status = TaskStatus.IN_PROGRESS,
            assignee = "다이노",
        )
        val state = KanbanBoardState()
        state.addTask(result)

        val task = state.kanbanBoard.allTasks().first()
        state.showCardDialog(task)

        state.deleteTask(task)

        assertThat(state.isCardDialogVisible).isFalse()

        assertThat(state.snackbarEvent?.type).isEqualTo(SnackbarMessageType.TaskDeleted)

        assertThat(state.kanbanBoard.allTasks()).isEmpty()
    }

    @Test
    fun `카드의 상태가 Done 라면 카드 삭제를 눌렀을 때 해당 id 값의 카드는 삭제되지 않고 TaskDeleteFailed가 뜬다`() {
        val result = TaskFormResult(
            title = "새로운 태스크",
            description = "태스크 설명",
            tags = emptyList(),
            status = TaskStatus.DONE,
            assignee = "다이노",
        )
        val state = KanbanBoardState()
        state.addTask(result)

        val task = state.kanbanBoard.allTasks().first()
        state.showCardDialog(task)

        state.deleteTask(task)

        assertThat(state.isCardDialogVisible).isFalse()

        assertThat(state.snackbarEvent?.type).isEqualTo(SnackbarMessageType.TaskDeleteFailed)

        assertThat(state.kanbanBoard.allTasks()).isNotEmpty()
    }

    @Test
    fun `moveTask를 호출하여 상태 변경울 하면 To Do에서는 In Progress로만 가능하고 TaskMoved 스낵바가 뜬다`() {

        val taskToMove = KanbanTask(
            title = "A",
            status = TaskStatus.TODO,
            crewName = "다이노",
        )
        val otherTask = KanbanTask(
            title = "B",
            status = TaskStatus.IN_PROGRESS,
            crewName = "페임스",
        )
        val state = KanbanBoardState(KanbanBoard(listOf(taskToMove, otherTask)))

        state.moveTask(taskToMove, TaskStatus.IN_PROGRESS)

        val moved = state.kanbanBoard.allTasks().first { it.id == taskToMove.id }
        val untouched = state.kanbanBoard.allTasks().first { it.id == otherTask.id }

        assertThat(moved.status).isEqualTo(TaskStatus.IN_PROGRESS)
        assertThat(untouched.status).isEqualTo(TaskStatus.IN_PROGRESS)
        assertThat(state.snackbarEvent?.type).isEqualTo(SnackbarMessageType.TaskMoved)
    }

    @Test
    fun `moveTask를 호출하여 상태 변경울 하면 To Do에서는 Review로 불가능하고 TaskMoveFailed 스낵바가 뜬다`() {

        val taskToMove = KanbanTask(
            title = "A",
            status = TaskStatus.TODO,
            crewName = "다이노",
        )
        val otherTask = KanbanTask(
            title = "B",
            status = TaskStatus.IN_PROGRESS,
            crewName = "페임스",
        )
        val state = KanbanBoardState(KanbanBoard(listOf(taskToMove, otherTask)))

        /** To Do에서 Review로 이동 불가능 테스트 **/
        state.moveTask(taskToMove, TaskStatus.REVIEW)

        val movedReview = state.kanbanBoard.allTasks().first { it.id == taskToMove.id }
        val untouched = state.kanbanBoard.allTasks().first { it.id == otherTask.id }

        assertThat(movedReview.status).isNotEqualTo(TaskStatus.REVIEW)
        assertThat(untouched.status).isEqualTo(TaskStatus.IN_PROGRESS)
        assertThat(state.snackbarEvent?.type).isEqualTo(SnackbarMessageType.TaskMoveFailed)
    }

    @Test
    fun `moveTask를 호출하여 상태 변경울 하면 To Do에서는 Done으로 불가능하고 TaskMoveFailed 스낵바가 뜬다`() {

        val taskToMove = KanbanTask(
            title = "A",
            status = TaskStatus.TODO,
            crewName = "다이노",
        )
        val otherTask = KanbanTask(
            title = "B",
            status = TaskStatus.IN_PROGRESS,
            crewName = "페임스",
        )
        val state = KanbanBoardState(KanbanBoard(listOf(taskToMove, otherTask)))

        /** To Do에서 Done 이동 불가능 테스트 **/

        state.moveTask(taskToMove, TaskStatus.DONE)

        val untouched = state.kanbanBoard.allTasks().first { it.id == otherTask.id }
        val movedDone = state.kanbanBoard.allTasks().first { it.id == taskToMove.id }

        assertThat(movedDone.status).isNotEqualTo(TaskStatus.DONE)
        assertThat(untouched.status).isEqualTo(TaskStatus.IN_PROGRESS)
        assertThat(state.snackbarEvent?.type).isEqualTo(SnackbarMessageType.TaskMoveFailed)
    }

    @Test
    fun `moveTask를 호출하여 상태 변경울 하면 In Progress에서는 To Do로 가능하고 TaskMoved 스낵바가 뜬다`() {

        val taskToMove = KanbanTask(
            title = "A",
            status = TaskStatus.IN_PROGRESS,
            crewName = "다이노",
        )
        val otherTask = KanbanTask(
            title = "B",
            status = TaskStatus.DONE,
            crewName = "페임스",
        )
        val state = KanbanBoardState(KanbanBoard(listOf(taskToMove, otherTask)))

        state.moveTask(taskToMove, TaskStatus.TODO)

        val moved = state.kanbanBoard.allTasks().first { it.id == taskToMove.id }
        val untouched = state.kanbanBoard.allTasks().first { it.id == otherTask.id }

        assertThat(moved.status).isEqualTo(TaskStatus.TODO)
        assertThat(untouched.status).isEqualTo(TaskStatus.DONE)
        assertThat(state.snackbarEvent?.type).isEqualTo(SnackbarMessageType.TaskMoved)
    }

    @Test
    fun `moveTask를 호출하여 상태 변경울 하면 In Progress에서는 Review로 가능하고 TaskMoved 스낵바가 뜬다`() {

        val taskToMove = KanbanTask(
            title = "A",
            status = TaskStatus.IN_PROGRESS,
            crewName = "다이노",
        )
        val otherTask = KanbanTask(
            title = "B",
            status = TaskStatus.DONE,
            crewName = "페임스",
        )
        val state = KanbanBoardState(KanbanBoard(listOf(taskToMove, otherTask)))

        state.moveTask(taskToMove, TaskStatus.REVIEW)

        val moved = state.kanbanBoard.allTasks().first { it.id == taskToMove.id }
        val untouched = state.kanbanBoard.allTasks().first { it.id == otherTask.id }

        assertThat(moved.status).isEqualTo(TaskStatus.REVIEW)
        assertThat(untouched.status).isEqualTo(TaskStatus.DONE)
        assertThat(state.snackbarEvent?.type).isEqualTo(SnackbarMessageType.TaskMoved)
    }

    @Test
    fun `moveTask를 호출하여 상태 변경울 하면 In Progress에서는 Done으로 불가능하고 TaskMoveFailed 스낵바가 뜬다`() {

        val taskToMove = KanbanTask(
            title = "A",
            status = TaskStatus.IN_PROGRESS,
            crewName = "다이노",
        )
        val otherTask = KanbanTask(
            title = "B",
            status = TaskStatus.DONE,
            crewName = "페임스",
        )
        val state = KanbanBoardState(KanbanBoard(listOf(taskToMove, otherTask)))

        /** In Progress에서 DONE 이동 불가능 테스트 **/
        state.moveTask(taskToMove, TaskStatus.DONE)

        val movedReview = state.kanbanBoard.allTasks().first { it.id == taskToMove.id }
        val untouched = state.kanbanBoard.allTasks().first { it.id == otherTask.id }

        assertThat(movedReview.status).isNotEqualTo(TaskStatus.DONE)
        assertThat(untouched.status).isEqualTo(TaskStatus.DONE)
        assertThat(state.snackbarEvent?.type).isEqualTo(SnackbarMessageType.TaskMoveFailed)
    }

    @Test
    fun `moveTask를 호출하여 상태 변경울 하면 Review에서는 In Progress로 가능하고 TaskMoved 스낵바가 뜬다`() {

        val taskToMove = KanbanTask(
            title = "A",
            status = TaskStatus.REVIEW,
            crewName = "다이노",
        )
        val otherTask = KanbanTask(
            title = "B",
            status = TaskStatus.TODO,
            crewName = "페임스",
        )
        val state = KanbanBoardState(KanbanBoard(listOf(taskToMove, otherTask)))

        state.moveTask(taskToMove, TaskStatus.IN_PROGRESS)

        val moved = state.kanbanBoard.allTasks().first { it.id == taskToMove.id }
        val untouched = state.kanbanBoard.allTasks().first { it.id == otherTask.id }

        assertThat(moved.status).isEqualTo(TaskStatus.IN_PROGRESS)
        assertThat(untouched.status).isEqualTo(TaskStatus.TODO)
        assertThat(state.snackbarEvent?.type).isEqualTo(SnackbarMessageType.TaskMoved)
    }

    @Test
    fun `moveTask를 호출하여 상태 변경울 하면 Review에서는 Done으로 가능하고 TaskMoved 스낵바가 뜬다`() {

        val taskToMove = KanbanTask(
            title = "A",
            status = TaskStatus.REVIEW,
            crewName = "다이노",
        )
        val otherTask = KanbanTask(
            title = "B",
            status = TaskStatus.TODO,
            crewName = "페임스",
        )
        val state = KanbanBoardState(KanbanBoard(listOf(taskToMove, otherTask)))

        state.moveTask(taskToMove, TaskStatus.DONE)

        val moved = state.kanbanBoard.allTasks().first { it.id == taskToMove.id }
        val untouched = state.kanbanBoard.allTasks().first { it.id == otherTask.id }

        assertThat(moved.status).isEqualTo(TaskStatus.DONE)
        assertThat(untouched.status).isEqualTo(TaskStatus.TODO)
        assertThat(state.snackbarEvent?.type).isEqualTo(SnackbarMessageType.TaskMoved)
    }

    @Test
    fun `moveTask를 호출하여 상태 변경울 하면 Review에서는 To Do로 불가능하고 TaskMoveFailed 스낵바가 뜬다`() {

        val taskToMove = KanbanTask(
            title = "A",
            status = TaskStatus.REVIEW,
            crewName = "다이노",
        )
        val otherTask = KanbanTask(
            title = "B",
            status = TaskStatus.IN_PROGRESS,
            crewName = "페임스",
        )
        val state = KanbanBoardState(KanbanBoard(listOf(taskToMove, otherTask)))

        /** Review에서 To Do로 이동 불가능 테스트 **/
        state.moveTask(taskToMove, TaskStatus.TODO)

        val movedReview = state.kanbanBoard.allTasks().first { it.id == taskToMove.id }
        val untouched = state.kanbanBoard.allTasks().first { it.id == otherTask.id }

        assertThat(movedReview.status).isNotEqualTo(TaskStatus.TODO)
        assertThat(untouched.status).isEqualTo(TaskStatus.IN_PROGRESS)
        assertThat(state.snackbarEvent?.type).isEqualTo(SnackbarMessageType.TaskMoveFailed)
    }

    @Test
    fun `moveTask를 호출하여 상태 변경울 하면 Done에서는 To Do로만 가능하고 TaskMoved 스낵바가 뜬다`() {

        val taskToMove = KanbanTask(
            title = "A",
            status = TaskStatus.DONE,
            crewName = "다이노",
        )
        val otherTask = KanbanTask(
            title = "B",
            status = TaskStatus.IN_PROGRESS,
            crewName = "페임스",
        )
        val state = KanbanBoardState(KanbanBoard(listOf(taskToMove, otherTask)))

        state.moveTask(taskToMove, TaskStatus.TODO)

        val moved = state.kanbanBoard.allTasks().first { it.id == taskToMove.id }
        val untouched = state.kanbanBoard.allTasks().first { it.id == otherTask.id }

        assertThat(moved.status).isEqualTo(TaskStatus.TODO)
        assertThat(untouched.status).isEqualTo(TaskStatus.IN_PROGRESS)
        assertThat(state.snackbarEvent?.type).isEqualTo(SnackbarMessageType.TaskMoved)
    }

    @Test
    fun `moveTask를 호출하여 상태 변경울 하면 Done으로에서는 Review로 불가능하고 TaskMoveFailed 스낵바가 뜬다`() {

        val taskToMove = KanbanTask(
            title = "A",
            status = TaskStatus.DONE,
            crewName = "다이노",
        )
        val otherTask = KanbanTask(
            title = "B",
            status = TaskStatus.TODO,
            crewName = "페임스",
        )
        val state = KanbanBoardState(KanbanBoard(listOf(taskToMove, otherTask)))

        /** DONE에서 Review로 이동 불가능 테스트 **/
        state.moveTask(taskToMove, TaskStatus.REVIEW)

        val movedReview = state.kanbanBoard.allTasks().first { it.id == taskToMove.id }
        val untouched = state.kanbanBoard.allTasks().first { it.id == otherTask.id }

        assertThat(movedReview.status).isNotEqualTo(TaskStatus.REVIEW)
        assertThat(untouched.status).isEqualTo(TaskStatus.TODO)
        assertThat(state.snackbarEvent?.type).isEqualTo(SnackbarMessageType.TaskMoveFailed)
    }

    @Test
    fun `moveTask를 호출하여 상태 변경울 하면 Done으로에서는 In Progress으로 불가능하고 TaskMoveFailed 스낵바가 뜬다`() {

        val taskToMove = KanbanTask(
            title = "A",
            status = TaskStatus.DONE,
            crewName = "다이노",
        )
        val otherTask = KanbanTask(
            title = "B",
            status = TaskStatus.TODO,
            crewName = "페임스",
        )
        val state = KanbanBoardState(KanbanBoard(listOf(taskToMove, otherTask)))

        /** DONE에서 IN_PROGRESS 이동 불가능 테스트 **/
        state.moveTask(taskToMove, TaskStatus.IN_PROGRESS)

        val untouched = state.kanbanBoard.allTasks().first { it.id == otherTask.id }
        val movedDone = state.kanbanBoard.allTasks().first { it.id == taskToMove.id }

        assertThat(movedDone.status).isNotEqualTo(TaskStatus.IN_PROGRESS)
        assertThat(untouched.status).isEqualTo(TaskStatus.TODO)
        assertThat(state.snackbarEvent?.type).isEqualTo(SnackbarMessageType.TaskMoveFailed)
    }
}

private fun KanbanBoard.allTasks(): List<KanbanTask> = TaskStatus.entries.flatMap { status ->
    getTasksByStatus(status)
}
