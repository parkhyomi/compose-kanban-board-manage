package woowacourse.kanban.board.component.board

import kotlin.test.Test
import org.assertj.core.api.Assertions.assertThat
import woowacourse.kanban.board.domain.KanbanBoard
import woowacourse.kanban.board.domain.TaskStatus
import woowacourse.kanban.board.feature.board.KanbanBoardState
import woowacourse.kanban.board.feature.board.component.dialog.model.TaskFormResult
import woowacourse.kanban.board.feature.board.model.SnackbarMessageType

class KanbanBoardStateTest {

    @Test
    fun `초기 상태에서는 보드가 비어있고 다이얼로그가 보이지 않는다`() {
        // Given & When
        val state = KanbanBoardState()

        // Then
        assertThat(state.kanbanBoard.tasks).isEmpty()
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
    fun `addTask 성공 시 snackbarEvent가 세팅된다`() {
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
        assertThat(state.kanbanBoard.tasks).hasSize(1)

        val addedTask = state.kanbanBoard.tasks.first()
        assertThat(addedTask.title).isEqualTo("새로운 태스크")
        assertThat(addedTask.description).isEqualTo("태스크 설명")
        assertThat(addedTask.status).isEqualTo(TaskStatus.TODO)
        assertThat(addedTask.crewName).isEqualTo("다이노")

        assertThat(state.isTaskDialogVisible).isFalse()
    }
}
